package com.example.techforum.apiPackage;

import java.util.ArrayList;

public class NewsModal {
    int result;
    String status;
    ArrayList<Articles>articles;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Articles> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<Articles> articles) {
        this.articles = articles;
    }

    public NewsModal(int result, String status, ArrayList<Articles> articles) {
        this.result = result;
        this.status = status;
        this.articles = articles;
    }
}
