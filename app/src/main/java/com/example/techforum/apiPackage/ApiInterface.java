package com.example.techforum.apiPackage;

import com.example.techforum.apiPackage.NewsModal;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {


    @GET("top-headlines")
    Call<NewsModal>get_tech_news(
            @Query("country") String country,
            @Query("pageSize") int pageSize,
            @Query("apiKey") String apiKey,
            @Query("category") String category
);
}
