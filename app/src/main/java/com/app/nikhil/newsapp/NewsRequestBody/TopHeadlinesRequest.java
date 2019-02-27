package com.app.nikhil.newsapp.NewsRequestBody;

import com.google.gson.annotations.SerializedName;

public class TopHeadlinesRequest {

    @SerializedName("country")
    String country;

    @SerializedName("category")
    String category;

    @SerializedName("sources")
    String sources;

    @SerializedName("q")
    String q;

    @SerializedName("pageSize")
    int pageSize;

    @SerializedName("page")
    int page;

    @SerializedName("apiKey")
    String apiKey;


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSources() {
        return sources;
    }

    public void setSources(String sources) {
        this.sources = sources;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

}
