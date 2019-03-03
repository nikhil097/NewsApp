package com.app.nikhil.newsapp.UI.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.nikhil.newsapp.Adapter.NewsSwipeAdapter;
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
import com.daimajia.swipe.util.Attributes;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EntertainmentNewsFragment extends Fragment {

    ApiService apiService;

    SQLiteDatabase sqLiteDatabase;
    TrendingNewsAdapter businessNewsAdapter;

    RecyclerView entertainmentNewsRv;
    private ArrayList<Article> savedArticlesList;

    public EntertainmentNewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            apiService=new ApiService();

            SQLiteDB sqLiteDB=new SQLiteDB(getActivity());
            sqLiteDatabase=sqLiteDB.getWritableDatabase();

        savedArticlesList=new ArrayList<>();

        fetchSavedNewsFromDatabase();


            View view= inflater.inflate(R.layout.fragment_entertainment_news, container, false);

            entertainmentNewsRv =view.findViewById(R.id.entertainmentNewsRv);
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
        apiService.getTopHeadlines(ApiCredentals.API_KEY, countryCode,"entertainment","",20, new ResponseCallback<TopHeadlinesResponse>() {
            @Override
            public void success(TopHeadlinesResponse topHeadlinesResponse) {

                List<Article> articles=topHeadlinesResponse.getArticles();
                int totalResults=topHeadlinesResponse.getTotalResults();

                if(totalResults>20)
                {
                    totalResults=20;
                }

                ArrayList<Article> trendingArticlesList=new ArrayList<>();

                for(int i=0;i<totalResults;i++)
                {
                    trendingArticlesList.add(articles.get(i));
                    if(checkIfArticleAlreadySaved(articles.get(i)))
                    {
                        articles.get(i).setIsSaved(true);
                    }
                }

                populateTrendingNewsView(trendingArticlesList);


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
        businessNewsAdapter =new TrendingNewsAdapter(trendingArticlesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        entertainmentNewsRv.setLayoutManager(mLayoutManager);
        entertainmentNewsRv.setItemAnimator(new DefaultItemAnimator());
        entertainmentNewsRv.setAdapter(businessNewsAdapter);

        RecycleClick.addTo(entertainmentNewsRv).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                startActivity(new Intent(getActivity(),ArticleDetailActivity.class).putExtra("article",trendingArticlesList.get(position)));

            }
        });

    }


}
