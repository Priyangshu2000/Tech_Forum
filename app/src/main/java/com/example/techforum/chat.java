package com.example.techforum;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.techforum.ChatPackage.chatAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class chat extends Fragment {
    DatabaseReference mbase;
    RecyclerView recyclerView;
    chatAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_chat, container, false);
        mbase= FirebaseDatabase.getInstance().getReference().child("userDetails");
        recyclerView = view.findViewById(R.id.chat_recyclerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        FirebaseRecyclerOptions<userDetails> options
                = new FirebaseRecyclerOptions.Builder<userDetails>().setQuery(mbase, userDetails.class).build();
        adapter = new chatAdapter(options);
        recyclerView.setAdapter(adapter);
        return view;
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