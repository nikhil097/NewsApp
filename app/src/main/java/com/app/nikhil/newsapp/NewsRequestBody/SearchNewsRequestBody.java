package com.app.nikhil.newsapp.NewsRequestBody;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchNewsRequestBody {

    @SerializedName("q")
    String query;

    @SerializedName("language")
    String language;

    @SerializedName("sortBy")
    String sortBy;

    @SerializedName("sources")
    List<String> sources;

    @SerializedName("pageSize")
    int pageSize;

    @SerializedName("page")
    int page;

    @SerializedName("apiKey")
    String apiKey;

    public SearchNewsRequestBody(String query, String language, String sortBy, List<String> sources, int pageSize, int page, String apiKey) {
        this.query = query;
        this.language = language;
        this.sortBy = sortBy;
        this.sources = sources;
        this.pageSize = pageSize;
        this.page = page;
        this.apiKey = apiKey;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
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
