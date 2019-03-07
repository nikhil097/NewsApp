package com.app.nikhil.newsapp.UI.Fragments.OnlineFragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.nikhil.newsapp.Adapter.TrendingNewsAdapter;
import com.app.nikhil.newsapp.NewsResponseBody.TopHeadlinesResponse;
import com.app.nikhil.newsapp.Pojo.Article;
import com.app.nikhil.newsapp.R;
import com.app.nikhil.newsapp.Rest.ApiCredentals;
import com.app.nikhil.newsapp.Rest.ApiService;
import com.app.nikhil.newsapp.Rest.ResponseCallback;
import com.app.nikhil.newsapp.Rest.SQLiteDB;
import com.app.nikhil.newsapp.UI.Activity.ArticleDetailActivity;
import com.chootdev.recycleclick.RecycleClick;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrendingNewsFragment extends Fragment {

    ApiService apiService;

    SQLiteDatabase sqLiteDatabase;
    TrendingNewsAdapter trendingNewsAdapter;

    RecyclerView trendingNewsRv;

    SwipeRefreshLayout newsRefreshLayout;

    RotateLoading trendingFragmentLoadingProgress;

    ArrayList<Article> savedArticlesList;


    public TrendingNewsFragment() {
        // Required empty public constructor
    }

    public static TrendingNewsFragment newInstance(String title) {
        TrendingNewsFragment fragment = new TrendingNewsFragment();

        Bundle args = new Bundle();
        args.putCharSequence("title", title);
        fragment.setArguments(args);

        return fragment;
    }


    public String getTitle() {
        Bundle args = getArguments();
        return args.getCharSequence("title", "NO TITLE FOUND").toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        apiService=new ApiService();

        SQLiteDB sqLiteDB=new SQLiteDB(getActivity());
        sqLiteDatabase=sqLiteDB.getWritableDatabase();

        savedArticlesList=new ArrayList<>();

        fetchSavedNewsFromDatabase();

        View view= inflater.inflate(R.layout.fragment_trending_news, container, false);

        trendingNewsRv=view.findViewById(R.id.trendingNewsRv);
        trendingFragmentLoadingProgress=view.findViewById(R.id.trendingFragmentProgress);
        trendingFragmentLoadingProgress.setLoadingColor(getResources().getColor(android.R.color.holo_blue_dark));
        trendingFragmentLoadingProgress.start();

        newsRefreshLayout=view.findViewById(R.id.newsRefreshLayout);
        newsRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                fetchTrendingNews();

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fetchTrendingNews();

    }

    public void fetchTrendingNews()
    {
        SharedPreferences mPreferences = getActivity().getSharedPreferences("NewsDB", Context.MODE_PRIVATE);
        String countryCode=mPreferences.getString("userCountry","in");
        Log.v("tag1",getTag().toString());
        apiService.getTopHeadlines(ApiCredentals.API_KEY, countryCode,getTitle(),"",20, new ResponseCallback<TopHeadlinesResponse>() {
                    @Override
                    public void success(TopHeadlinesResponse topHeadlinesResponse) {
                        newsRefreshLayout.setRefreshing(false);

                        List<Article> articles=topHeadlinesResponse.getArticles();
                        int totalResults=topHeadlinesResponse.getTotalResults();

                        if(totalResults>20)
                        {
                            totalResults=20;
                        }

                        ArrayList<Article> trendingArticlesList=new ArrayList<>();

                        for(int i=0;i<totalResults;i++)
                        {

                            if(checkIfArticleAlreadySaved(articles.get(i)))
                            {
                                articles.get(i).setIsSaved(true);
                            }
                            trendingArticlesList.add(articles.get(i));
                        }

                        populateTrendingNewsView(trendingArticlesList);

                        trendingFragmentLoadingProgress.stop();


            }

            @Override
            public void failure(TopHeadlinesResponse topHeadlinesResponse) {

            }
        });
    }

    public boolean checkIfArticleAlreadySaved(Article article)
    {
        for(Article article1:savedArticlesList)
        {
            if(article1.getTitle().equals(article.getTitle()))
            {
                return true;
            }
        }
        return false;

    }

    public void fetchSavedNewsFromDatabase() {
        SQLiteDB sqlhelper = new SQLiteDB(getActivity());
        SQLiteDatabase sqLiteDatabase = sqlhelper.getWritableDatabase();


        String[] columns = {"_id", SQLiteDB.TITLE, SQLiteDB.DESCRIPTION, SQLiteDB.URLTOIMAGE, SQLiteDB.CONTENT};
        Cursor cursor = sqLiteDatabase.query("ARTICLEDETAILS", columns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int cid = cursor.getInt(0);
            String articleTitle = cursor.getString(1);
            String articleDescription = cursor.getString(2);
            String articleUrlToImage = cursor.getString(3);
            String articleContent = cursor.getString(4);
            Log.v("activity2", cid + " " + articleTitle + " " + articleDescription);


            Article article = new Article();
            article.setTitle(articleTitle);
            article.setDescription(articleDescription);
            article.setUrlToImage(articleUrlToImage);
            article.setContent(articleContent);

            savedArticlesList.add(article);
        }
    }

    public void populateTrendingNewsView(final ArrayList<Article> trendingArticlesList)
    {
        trendingNewsAdapter=new TrendingNewsAdapter(trendingArticlesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        trendingNewsRv.setLayoutManager(mLayoutManager);
        trendingNewsRv.setItemAnimator(new DefaultItemAnimator());
        trendingNewsRv.setAdapter(trendingNewsAdapter);

        RecycleClick.addTo(trendingNewsRv).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                startActivity(new Intent(getActivity(),ArticleDetailActivity.class).putExtra("article",trendingArticlesList.get(position)));

            }
        });

    }



}
