package com.app.nikhil.newsapp.Rest;

import android.util.Log;

import com.app.nikhil.newsapp.NewsRequestBody.SearchNewsRequestBody;
import com.app.nikhil.newsapp.NewsResponseBody.SearchNewsResponseBody;
import com.app.nikhil.newsapp.NewsResponseBody.TopHeadlinesResponse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    public static final String BASE_URL="https://newsapi.org/";

    NewsAPI newsAPI;

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }

    public ApiService()
    {
        newsAPI=ApiService.getClient().create(NewsAPI.class);
    }



    public void getTopHeadlines(String apiKey,String country, String category,String query,int noOfResults,final ResponseCallback<TopHeadlinesResponse> callback)
    {
        Call<TopHeadlinesResponse> call=newsAPI.getTopHeadLines(apiKey,country,query,noOfResults,category);

        call.enqueue(new Callback<TopHeadlinesResponse>() {
            @Override
            public void onResponse(Call<TopHeadlinesResponse> call, Response<TopHeadlinesResponse> response){
                if(response.isSuccessful()) {
                    callback.success(response.body());
                }

            }

            @Override
            public void onFailure(Call<TopHeadlinesResponse> call, Throwable t) {

            }
        });

    }

    public void getSearchNewsResults(String apiKey,String query,String sources,String sortBy,String language,int page,int pageSize, final ResponseCallback<SearchNewsResponseBody> callback)
    {
        Call<SearchNewsResponseBody> call=newsAPI.getSearchNewsResponse(apiKey,query,sources,sortBy,language,page,pageSize);

        call.enqueue(new Callback<SearchNewsResponseBody>() {
            @Override
            public void onResponse(Call<SearchNewsResponseBody> call, Response<SearchNewsResponseBody> response) {
                if (response.isSuccessful())
                {
                    callback.success(response.body());
                }
            }

            @Override
            public void onFailure(Call<SearchNewsResponseBody> call, Throwable t) {

            }
        });
    }


}
