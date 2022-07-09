package com.example.techforum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

public class DetailedNews extends AppCompatActivity {
WebView webView;
Toolbar toolbar;
ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_news);
        Intent intent=getIntent();
        String url=intent.getStringExtra("url");
        webView=findViewById(R.id.webView);
        backButton=findViewById(R.id.news_backButton);
        toolbar=findViewById(R.id.toolbar);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent= new Intent(DetailedNews.this,Dashboard.class);
//                intent.putExtra("fragment","news");
//                startActivity(intent);
//                finish();
                onBackPressed();
            }
        });
    }
}