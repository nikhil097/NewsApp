package com.app.nikhil.newsapp.Rest;

import com.app.nikhil.newsapp.NewsResponseBody.TopHeadlinesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsAPI {


    @GET("/v2/top-headlines")
    Call<TopHeadlinesResponse> getTopHeadLines(@Query("apiKey") String apiKey, @Query("country") String country, @Query("q") String query,@Query("pageSize") int noOfResults);




}
