package com.app.nikhil.newsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.text.TextUtils;

import com.app.nikhil.newsapp.Rest.ApiService;
import com.app.nikhil.newsapp.Rest.News;

public class MainActivity extends AppCompatActivity {


    private static final String API_KEY="5f13303ba2034ca5ab641e4849ac9b55";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        News apiService =
                ApiService.getClient().create(News.class);


    }
}
