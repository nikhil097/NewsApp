package com.app.nikhil.newsapp.UI.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.nikhil.newsapp.Adapter.SavedNewsAdapter;
import com.app.nikhil.newsapp.Adapter.TrendingNewsAdapter;
import com.app.nikhil.newsapp.Pojo.Article;
import com.app.nikhil.newsapp.R;
import com.app.nikhil.newsapp.Rest.SQLiteDB;
import com.app.nikhil.newsapp.UI.Activity.ArticleDetailActivity;
import com.chootdev.recycleclick.RecycleClick;

import java.util.ArrayList;


public class SavedNewsFragment extends Fragment {

    ArrayList<Article> savedArticlesList;
    RecyclerView savedNewsRv;
    SavedNewsAdapter savedNewsAdapter;

    public SavedNewsFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        savedArticlesList=new ArrayList<>();
        fetchSavedNewsFromDatabase();

        View view= inflater.inflate(R.layout.fragment_saved_news, container, false);
        savedNewsRv=view.findViewById(R.id.savedNewsRv);
        populateTrendingNewsView(savedArticlesList);
        return  view;
    }


    public void populateTrendingNewsView(final ArrayList<Article> savedArticlesList)
    {
        savedNewsAdapter=new SavedNewsAdapter(savedArticlesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        savedNewsRv.setLayoutManager(mLayoutManager);
        savedNewsRv.setItemAnimator(new DefaultItemAnimator());
        savedNewsRv.setAdapter(savedNewsAdapter);

        RecycleClick.addTo(savedNewsRv).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                startActivity(new Intent(getActivity(),ArticleDetailActivity.class).putExtra("article",savedArticlesList.get(position)));

            }
        });

    }


    public void fetchSavedNewsFromDatabase()
    {
        SQLiteDB sqlhelper=new SQLiteDB(getActivity());
        SQLiteDatabase sqLiteDatabase=sqlhelper.getWritableDatabase();


        String[] columns={"_id",SQLiteDB.TITLE,SQLiteDB.DESCRIPTION,SQLiteDB.URLTOIMAGE,SQLiteDB.CONTENT};
        Cursor cursor=sqLiteDatabase.query("ARTICLEDETAILS",columns,null,null,null,null,null);

        while(cursor.moveToNext())
        {
            int cid=cursor.getInt(0);
            String articleTitle=cursor.getString(1);
            String articleDescription=cursor.getString(2);
            String articleUrlToImage=cursor.getString(3);
            String articleContent=cursor.getString(4);
            Log.v("activity2",cid+" "+articleTitle+" "+articleDescription);


            Article article=new Article();
            article.setTitle(articleTitle);
            article.setDescription(articleDescription);
            article.setUrlToImage(articleUrlToImage);
            article.setContent(articleContent);

            savedArticlesList.add(article);
        }

    }


}
