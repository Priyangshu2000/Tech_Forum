package com.example.techforum;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techforum.Message.MessageAdapter;
import com.example.techforum.Message.MessageModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class DetailedChat extends AppCompatActivity {
String username,profilePic;
ImageView recieverProfilePic;
TextView recieverUsername;
String sender_id,reciever_id;
RecyclerView recyclerView;
EditText messageBox;
ImageView send;
ImageView hide;
ImageView back;
FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_chat);
        Intent intent=getIntent();
        username=intent.getStringExtra("username");
        reciever_id=intent.getStringExtra("userId");
        profilePic=intent.getStringExtra("profilePic");
        recieverProfilePic=findViewById(R.id.chat_userpic);
        recieverUsername=findViewById(R.id.chat_username);
        sender_id= FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()+"";
        recieverUsername.setText(username);
        send=findViewById(R.id.chat_sendButton);
        messageBox=findViewById(R.id.chat_box);
        Picasso.get().load(profilePic).placeholder(R.drawable.ic_baseline_person_24).into(recieverProfilePic);
        recyclerView=findViewById(R.id.detailedChat_recycler);
        hide=findViewById(R.id.chat_toggle);
        back=findViewById(R.id.backButton);



        database=FirebaseDatabase.getInstance();



        String senderRoom=sender_id+reciever_id;
        String recieverRoom=reciever_id+sender_id;
        ArrayList<MessageModel>messageModels=new ArrayList<>();
        MessageAdapter adapter=new MessageAdapter(messageModels,this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true);
        recyclerView.setLayoutManager(layoutManager);
        database.getReference().child("chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(messageModels.size()!=0)
//                    recyclerView.setLayoutManager(new LinearLayoutManager(DetailedChat.this));

                messageModels.clear();

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    MessageModel model=dataSnapshot.getValue(MessageModel.class);
//                    Toast.makeText(DetailedChat.this,"s", Toast.LENGTH_SHORT).show();
                   messageModels.add(model);
                }
                Collections.reverse(messageModels);
                adapter.notifyDataSetChanged();
//                recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String message=messageBox.getText().toString();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
                LocalDateTime now = LocalDateTime.now();
                String time=dtf.format(now);
                MessageModel messageModel= new MessageModel(sender_id,message,time);
                messageBox.setText("");
                database.getReference().child("chats").child(senderRoom).push().setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if (!senderRoom.equals(recieverRoom)) {
                            database.getReference().child("chats").child(recieverRoom).push().setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("Priyangshu","Successfull");
                                }
                            });
                        }
                    }
                });
            }
        });


        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recyclerView.getVisibility()==View.VISIBLE)
                    recyclerView.setVisibility(View.GONE);
                else
                    recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onResume() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        super.onResume();
    }
}