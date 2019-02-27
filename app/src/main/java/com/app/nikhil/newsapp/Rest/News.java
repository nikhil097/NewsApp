package com.app.nikhil.newsapp.Rest;

import com.app.nikhil.newsapp.NewsResponseBody.TopHeadlinesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface News {


    @GET("/v2/top-headlines")
    Call<TopHeadlinesResponse> getTopHeadLines(@Query("") String apiKey);



}
