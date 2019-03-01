package com.app.nikhil.newsapp.UI.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.nikhil.newsapp.Pojo.Article;
import com.app.nikhil.newsapp.R;
import com.bumptech.glide.Glide;

public class ArticleDetailActivity extends AppCompatActivity {


    Article article;

    ImageView articleDetailImage;
    TextView articleDetailTitleTv;
    TextView articleDetailDescriptionTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        article= (Article) getIntent().getSerializableExtra("article");

        articleDetailImage=findViewById(R.id.articleDetailImage);
        articleDetailTitleTv=findViewById(R.id.articleTitleDetailTv);
        articleDetailDescriptionTv=findViewById(R.id.articleDescriptionDetailTv);

        Glide.with(ArticleDetailActivity.this).load(article.getUrlToImage()).into(articleDetailImage);

        articleDetailTitleTv.setText(article.getTitle());
        articleDetailDescriptionTv.setText(article.getContent());






    }
}
