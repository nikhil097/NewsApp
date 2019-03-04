package com.app.nikhil.newsapp.Adapter;


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

public class SavedNewsAdapter extends RecyclerView.Adapter<SavedNewsAdapter.SavedNewsViewHolder> {


    ArrayList<Article> savedArticlesList;
    Context context;

    @NonNull
    @Override
    public SavedNewsAdapter.SavedNewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        context=viewGroup.getContext();
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.saved_news_article_item,viewGroup,false);
        return new SavedNewsAdapter.SavedNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedNewsAdapter.SavedNewsViewHolder savedNewsViewHolder, final int i) {

        Article article=savedArticlesList.get(i);
        savedNewsViewHolder.articleTitleTv.setText(article.getTitle());
        Glide.with(context).load(article.getUrlToImage()).into(savedNewsViewHolder.articleImageView);
        savedNewsViewHolder.articleDescriptionTv.setText(article.getDescription());
        savedNewsViewHolder.deleteNewsRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteRecordFromDataBase("TITLE",savedArticlesList.get(i).getTitle(),i);

            }
        });

    }

    public void deleteRecordFromDataBase(String key,String value,int position)
    {
        SQLiteDB sqlHelper=new SQLiteDB(context);
        SQLiteDatabase sqLiteDatabase=sqlHelper.getWritableDatabase();


        sqLiteDatabase.delete("ARTICLEDETAILS",key+"=?",new String[]{value});

        savedArticlesList.remove(position);
        notifyDataSetChanged();

    }


    @Override
    public int getItemCount() {
        return savedArticlesList.size();
    }

    public SavedNewsAdapter(ArrayList<Article> savedArticlesList)
    {
        this.savedArticlesList=savedArticlesList;
    }

    public class SavedNewsViewHolder extends RecyclerView.ViewHolder {

        ImageView articleImageView;
        TextView articleTitleTv, articleDescriptionTv;
        Button deleteNewsRecord;

        public SavedNewsViewHolder(@NonNull View itemView) {
            super(itemView);

            articleImageView = itemView.findViewById(R.id.savedNewsImage);
            articleTitleTv = itemView.findViewById(R.id.savedArticleTitleTv);
            articleDescriptionTv = itemView.findViewById(R.id.savedArticleDescriptionTv);
            deleteNewsRecord=itemView.findViewById(R.id.deleteNewsRecordBtn);
        }


    }

    }
