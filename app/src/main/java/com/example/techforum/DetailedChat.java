package com.example.techforum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
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

import java.util.ArrayList;
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



        database=FirebaseDatabase.getInstance();



        String senderRoom=sender_id+reciever_id;
        String recieverRoom=reciever_id+sender_id;
        ArrayList<MessageModel>messageModels=new ArrayList<>();
        MessageAdapter adapter=new MessageAdapter(messageModels,this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        database.getReference().child("chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModels.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    MessageModel model=dataSnapshot.getValue(MessageModel.class);
//                    Toast.makeText(DetailedChat.this,"s", Toast.LENGTH_SHORT).show();
                   messageModels.add(model);
                }
                adapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=messageBox.getText().toString();
                MessageModel messageModel= new MessageModel(sender_id,message,new Date().getTime());
                messageBox.setText("");
                database.getReference().child("chats").child(senderRoom).push().setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if (!senderRoom.equals(recieverRoom)) {
                            database.getReference().child("chats").child(recieverRoom).push().setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

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
}