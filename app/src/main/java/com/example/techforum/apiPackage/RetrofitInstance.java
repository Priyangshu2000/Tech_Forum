package com.example.techforum.apiPackage;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    public static Retrofit retrofit;
    private static final String BASEURL="https://newsapi.org/v2/";

    public static ApiInterface getApiInterface() {
        if(retrofit==null)
            retrofit= new Retrofit.Builder().baseUrl(BASEURL).addConverterFactory(GsonConverterFactory.create()).build();

        return retrofit.create(ApiInterface.class);
    }
}
