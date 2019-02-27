package com.app.nikhil.newsapp.NewsResponseBody;

import com.app.nikhil.newsapp.Pojo.Article;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TopHeadlinesResponse {


    @SerializedName("status")
    String status;

    @SerializedName("source")
    String source;

    @SerializedName("totalResults")
    int totalResults;

    @SerializedName("articles")
    List<Article> articles;



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList articles) {
        this.articles = articles;
    }


}
