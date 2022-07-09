package com.example.techforum;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class home_fragment extends Fragment {
    DatabaseReference mbase;
    RecyclerView recyclerView;
    home_recycler_adapter adapter;
    ImageView likes;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home_fragment, container, false);
        mbase= FirebaseDatabase.getInstance().getReference().child("posts");
        recyclerView = view.findViewById(R.id.home_recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        FirebaseRecyclerOptions<posts> options
                = new FirebaseRecyclerOptions.Builder<posts>().setQuery(mbase, posts.class).build();
        adapter = new home_recycler_adapter(options);
//        recyclerView.scrollToPosition(4);
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