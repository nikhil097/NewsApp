package com.app.nikhil.newsapp.Rest;

import com.app.nikhil.newsapp.NewsRequestBody.SearchNewsRequestBody;
import com.app.nikhil.newsapp.NewsResponseBody.SearchNewsResponseBody;
import com.app.nikhil.newsapp.NewsResponseBody.SourcesResponse;
import com.app.nikhil.newsapp.NewsResponseBody.TopHeadlinesResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface NewsAPI {


    @GET("/v2/top-headlines")
    Call<TopHeadlinesResponse> getTopHeadLines(@Query("apiKey") String apiKey, @Query("country") String country, @Query("q") String query,@Query("pageSize") int noOfResults,@Query("category") String category);



    @GET("/v2/everything")
    Call<SearchNewsResponseBody> getSearchNewsResponse(@Query("apiKey") String apiKey,@Query("q") String query,@Query("sorces") String sources,@Query("sortBy")String sortBy,@Query("language")String langauge,@Query("page")int page,@Query("pageSize")int pageSize);

    @GET("/v2/sources")
    Call<SourcesResponse> getSources(@Query("apiKey") String apiKey);


}
