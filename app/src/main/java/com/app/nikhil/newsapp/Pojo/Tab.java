package com.app.nikhil.newsapp.Pojo;

public class Tab implements Comparable<Tab>{

    String categoryName;

    String urlToFirstPostImage;

    int id;

    public Tab(String categoryName, String urlToFirstPostImage,int id) {
        this.categoryName = categoryName;
        this.urlToFirstPostImage = urlToFirstPostImage;
        this.id=id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getUrlToFirstPostImage() {
        return urlToFirstPostImage;
    }

    public void setUrlToFirstPostImage(String urlToFirstPostImage) {
        this.urlToFirstPostImage = urlToFirstPostImage;
    }

    @Override
    public int compareTo(Tab tab) {

        return this.id-tab.getId();
    }
}
