package com.example.techforum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.techforum.comment.commentAdapter;
import com.example.techforum.comment.commentModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class commentActivity extends AppCompatActivity {
    DatabaseReference mbase;
    RecyclerView recyclerView;
    commentAdapter adapter;
    DatabaseReference userDbRef;
    FirebaseUser firebaseUser;
    String phoneNo;
    ImageView send;
    EditText typeComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Intent intent=getIntent();
        String postId=intent.getStringExtra("postId");
        mbase= FirebaseDatabase.getInstance().getReference().child("comments").child(postId);
        recyclerView = findViewById(R.id.comment_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(commentActivity.this));
        FirebaseRecyclerOptions<commentModel> options
                = new FirebaseRecyclerOptions.Builder<commentModel>().setQuery(mbase, commentModel.class).build();
        adapter = new commentAdapter(options);
        recyclerView.setAdapter(adapter);
        typeComment=findViewById(R.id.comment_writeComment);
        userDbRef= FirebaseDatabase.getInstance().getReference();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        phoneNo=firebaseUser.getPhoneNumber();
        send=findViewById(R.id.comment_send);
                send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time= Calendar.getInstance().getTime().toString();
                String key=phoneNo+time;
                String comment=typeComment.getText().toString();
                commentModel cm=new commentModel();
                cm.setComment(comment);
                userDbRef.child("comments").child(postId).child(key).setValue(cm);
                typeComment.setText("");
                Toast.makeText(commentActivity.this, "Reply sent", Toast.LENGTH_SHORT).show();
            }
        });


    }
    @Override
    public void onStart()
    {
        super.onStart();
        adapter.startListening();

    }
    @Override public void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }

}