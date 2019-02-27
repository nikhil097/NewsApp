package com.app.nikhil.newsapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.nikhil.newsapp.Pojo.Article;
import com.app.nikhil.newsapp.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class TrendingNewsAdapter extends RecyclerView.Adapter<TrendingNewsAdapter.TrendingNewsViewHolder>{

    ArrayList<Article> trendingArticlesList;
    Context context;


    @NonNull
    @Override
    public TrendingNewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        context=viewGroup.getContext();
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_article_item,viewGroup,false);

        return new TrendingNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingNewsViewHolder trendingNewsViewHolder, int i) {

        Article article=trendingArticlesList.get(i);
        trendingNewsViewHolder.articleTitleTv.setText(article.getTitle());
        Glide.with(context).load(article.getUrlToImage()).into(trendingNewsViewHolder.articleImageView);
        trendingNewsViewHolder.articleDescriptionTv.setText(article.getDescription());

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

        public TrendingNewsViewHolder(@NonNull View itemView) {
            super(itemView);

            articleImageView=itemView.findViewById(R.id.newsImage);
            articleTitleTv=itemView.findViewById(R.id.articleTitleTv);
            articleDescriptionTv=itemView.findViewById(R.id.articleDescriptionTv);

        }




    }


}
