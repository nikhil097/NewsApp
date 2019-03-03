package com.app.nikhil.newsapp.UI.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.app.nikhil.newsapp.Adapter.TrendingNewsAdapter;
import com.app.nikhil.newsapp.NewsRequestBody.SearchNewsRequestBody;
import com.app.nikhil.newsapp.NewsResponseBody.SearchNewsResponseBody;
import com.app.nikhil.newsapp.Pojo.Article;
import com.app.nikhil.newsapp.R;
import com.app.nikhil.newsapp.Rest.ApiCredentals;
import com.app.nikhil.newsapp.Rest.ApiService;
import com.app.nikhil.newsapp.Rest.ResponseCallback;
import com.app.nikhil.newsapp.Rest.SQLiteDB;
import com.app.nikhil.newsapp.UI.Activity.ArticleDetailActivity;
import com.chootdev.recycleclick.RecycleClick;

import java.util.ArrayList;
import java.util.List;

public class SearchNewsFragment extends Fragment {

    EditText searchNewsEt;
    ApiService apiService;

    RecyclerView searchNewsRv;
    TrendingNewsAdapter searchNewsAdapter;

    ArrayList<Article> savedArticlesList;

    public SearchNewsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_search_news, container, false);

        searchNewsEt=view.findViewById(R.id.searchNewsET);
        apiService=new ApiService();
        searchNewsRv=view.findViewById(R.id.resultNewsRv);

        savedArticlesList=new ArrayList<>();
        fetchSavedNewsFromDatabase();

        searchNewsEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length() > 3) {
                    apiService.getSearchNewsResults(ApiCredentals.API_KEY, searchNewsEt.getText().toString(), "", "", "en", 1, 20, new ResponseCallback<SearchNewsResponseBody>() {
                        @Override
                        public void success(SearchNewsResponseBody searchNewsResponseBody) {

                            List<Article> articles=searchNewsResponseBody.getArticles();

                            int totalResults=searchNewsResponseBody.getTotalResults();

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

                        }

                        @Override
                        public void failure(SearchNewsResponseBody searchNewsResponseBody) {

                        }
                    });
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return view;
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
        searchNewsAdapter=new TrendingNewsAdapter(trendingArticlesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        searchNewsRv.setLayoutManager(mLayoutManager);
        searchNewsRv.setItemAnimator(new DefaultItemAnimator());
        searchNewsRv.setAdapter(searchNewsAdapter);

        RecycleClick.addTo(searchNewsRv).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                startActivity(new Intent(getActivity(),ArticleDetailActivity.class).putExtra("article",trendingArticlesList.get(position)));

            }
        });

    }


}
