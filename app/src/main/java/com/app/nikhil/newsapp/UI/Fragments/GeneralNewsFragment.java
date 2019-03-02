package com.app.nikhil.newsapp.UI.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.nikhil.newsapp.Adapter.NewsSwipeAdapter;
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
public class GeneralNewsFragment extends Fragment {

    ApiService apiService;

    SQLiteDatabase sqLiteDatabase;
    NewsSwipeAdapter generalNewsAdapter;

    RecyclerView generalNewsRv;

    public GeneralNewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        apiService=new ApiService();

        SQLiteDB sqLiteDB=new SQLiteDB(getActivity());
        sqLiteDatabase=sqLiteDB.getWritableDatabase();



        View view= inflater.inflate(R.layout.fragment_general_news, container, false);

        generalNewsRv =view.findViewById(R.id.generalNewsRv);
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
        apiService.getTopHeadlines(ApiCredentals.API_KEY, countryCode,"general","",20, new ResponseCallback<TopHeadlinesResponse>() {
            @Override
            public void success(TopHeadlinesResponse topHeadlinesResponse) {

                List<Article> articles=topHeadlinesResponse.getArticles();
                int totalResults=topHeadlinesResponse.getTotalResults();

                ArrayList<Article> trendingArticlesList=new ArrayList<>();

                for(int i=0;i<20;i++)
                {
                    trendingArticlesList.add(articles.get(i));
                }

                populateTrendingNewsView(trendingArticlesList);


            }

            @Override
            public void failure(TopHeadlinesResponse topHeadlinesResponse) {

            }
        });
    }


    public void populateTrendingNewsView(final ArrayList<Article> trendingArticlesList)
    {
        generalNewsAdapter =new NewsSwipeAdapter(getActivity(),trendingArticlesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        generalNewsRv.setLayoutManager(mLayoutManager);
        generalNewsRv.setItemAnimator(new DefaultItemAnimator());
        ((NewsSwipeAdapter) generalNewsAdapter).setMode(Attributes.Mode.Single);
        generalNewsRv.setAdapter(generalNewsAdapter);

        RecycleClick.addTo(generalNewsRv).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                startActivity(new Intent(getActivity(),ArticleDetailActivity.class).putExtra("article",trendingArticlesList.get(position)));

            }
        });

    }


}
