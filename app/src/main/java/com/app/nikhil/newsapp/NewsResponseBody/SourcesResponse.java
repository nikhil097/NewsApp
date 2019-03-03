package com.app.nikhil.newsapp.NewsResponseBody;

import com.app.nikhil.newsapp.Pojo.NewsSource;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SourcesResponse {

    @SerializedName("status")
    String id;

    @SerializedName("sources")
    List<NewsSource> sources;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<NewsSource> getSources() {
        return sources;
    }

    public void setSources(List<NewsSource> sources) {
        this.sources = sources;
    }
}
