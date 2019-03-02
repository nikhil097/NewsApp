package com.app.nikhil.newsapp.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.nikhil.newsapp.Pojo.Article;
import com.app.nikhil.newsapp.R;
import com.app.nikhil.newsapp.Rest.SQLiteDB;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class TrendingNewsAdapter extends RecyclerView.Adapter<TrendingNewsAdapter.TrendingNewsViewHolder>{

    ArrayList<Article> trendingArticlesList;
    Context context;

    SQLiteDatabase sqLiteDatabase;

    @NonNull
    @Override
    public TrendingNewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        context=viewGroup.getContext();
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_article_item,viewGroup,false);
        SQLiteDB sqLiteDB=new SQLiteDB(context);
        sqLiteDatabase=sqLiteDB.getWritableDatabase();
        return new TrendingNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingNewsViewHolder trendingNewsViewHolder, final int i) {

        Article article=trendingArticlesList.get(i);
        trendingNewsViewHolder.articleTitleTv.setText(article.getTitle());
        Glide.with(context).load(article.getUrlToImage()).into(trendingNewsViewHolder.articleImageView);
        trendingNewsViewHolder.articleDescriptionTv.setText(article.getDescription());
        trendingNewsViewHolder.saveNewsOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveNewsArticle(trendingArticlesList.get(i));

            }
        });

    }

    public void saveNewsArticle(Article article)
    {
        ContentValues contentValues=new ContentValues();

        contentValues.put(SQLiteDB.TITLE,article.getTitle());
        contentValues.put(SQLiteDB.DESCRIPTION,article.getDescription());
        contentValues.put(SQLiteDB.URLTOIMAGE,article.getUrlToImage());
        contentValues.put(SQLiteDB.CONTENT,article.getContent());
        sqLiteDatabase.insert("ARTICLEDETAILS",null,contentValues);
    }

    @Override
    public int getItemCount() {
        return trendingArticlesList.size();
    }

    public TrendingNewsAdapter(ArrayList<Article> trendingArticlesList)
    {
        this.trendingArticlesList=trendingArticlesList;
    }

    public class TrendingNewsViewHolder extends RecyclerView.ViewHolder{

        ImageView articleImageView;
        TextView articleTitleTv,articleDescriptionTv;
        Button saveNewsOffline;

        public TrendingNewsViewHolder(@NonNull View itemView) {
            super(itemView);

            articleImageView=itemView.findViewById(R.id.newsImage);
            articleTitleTv=itemView.findViewById(R.id.articleTitleTv);
            articleDescriptionTv=itemView.findViewById(R.id.articleDescriptionTv);
       //     saveNewsOffline=itemView.findViewById(R.id.saveImageOfflineBtn);
        }




    }


}
