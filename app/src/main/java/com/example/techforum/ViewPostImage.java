package com.example.techforum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewPostImage extends AppCompatActivity {
ImageView profile,postImage,backButton;
TextView username,post,time,toolbarUsername;
String profileUrl,imageUrl,userName,postText,timeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post_image);
        profile=findViewById(R.id.viewPost_userpic);
        postImage=findViewById(R.id.viewPost_postpic);
        username=findViewById(R.id.viewPost_username);
        post=findViewById(R.id.viewPost_post);
        time=findViewById(R.id.viewPost_time);
        toolbarUsername=findViewById(R.id.toolbar_username);
        backButton=findViewById(R.id.backButton);
        Intent intent=getIntent();
        profileUrl=intent.getStringExtra("profileUrl");
        imageUrl=intent.getStringExtra("imageUrl");
        userName=intent.getStringExtra("username");
        postText=intent.getStringExtra("post");
        timeText=intent.getStringExtra("time");
        username.setText(userName);
        post.setText(postText);
        time.setText(timeText);
        Picasso.get().load(profileUrl).placeholder(R.drawable.ic_baseline_person_24).into(profile);
        Picasso.get().load(imageUrl).into(postImage);
        String newUsername;
        int i;
        for(i=0;i<username.length();i++){
            if(userName.charAt(i)==' ')
                break;
        }
        newUsername=userName.substring(0,i)+"'s"+" Post";
        toolbarUsername.setText(newUsername);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}