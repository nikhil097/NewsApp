package com.app.nikhil.newsapp.Rest;

public interface ResponseCallback<T> {

    void success(T t);

    void  failure(T t);



}
