package com.example.techforum;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.techforum.apiPackage.Articles;
import com.example.techforum.apiPackage.NewsModal;
import com.example.techforum.apiPackage.RetrofitInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class news extends Fragment {
    String apikey="863cd1f9eb124d799145f8b49faabd34";
    ArrayList<Articles>articles;
    String country="in";
    newsAdapter adapter;
    private RecyclerView newsRecyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_news, container, false);
        newsRecyclerView=view.findViewById(R.id.news_recyclerView);
        articles=new ArrayList<>();
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new newsAdapter(getContext(), articles);
        newsRecyclerView.setAdapter(adapter);
        findNews();
        return view;
    }

    private void findNews() {
        RetrofitInstance.getApiInterface().get_tech_news(country,100,apikey,"technology").enqueue(new Callback<NewsModal>() {
            @Override
            public void onResponse(Call<NewsModal> call, Response<NewsModal> response) {
                if(response.isSuccessful()){
                    articles.addAll(response.body().getArticles());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<NewsModal> call, Throwable t) {
                Toast.makeText(getContext(), "Error retrieving news", Toast.LENGTH_SHORT).show();
            }
        });

    }
}