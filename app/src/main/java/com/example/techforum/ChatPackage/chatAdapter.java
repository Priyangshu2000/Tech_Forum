package com.example.techforum.ChatPackage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techforum.DetailedChat;
import com.example.techforum.R;
import com.example.techforum.userDetails;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class chatAdapter extends FirebaseRecyclerAdapter<userDetails,chatAdapter.viewHolder>{
    Context context;
    public chatAdapter(@NonNull FirebaseRecyclerOptions<userDetails> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull userDetails model) {
        holder.userName.setText(model.getName());
        Picasso.get().load(model.getProfilePic()).placeholder(R.drawable.ic_baseline_person_24).into(holder.userProfile);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DetailedChat.class);
                intent.putExtra("userId",model.getPhoneNo());
                intent.putExtra("username",model.getName());
                intent.putExtra("profilePic",model.getProfilePic());
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.singlechat, parent, false);
        context=parent.getContext();
        return new chatAdapter.viewHolder(view);
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        ImageView userProfile;
        LinearLayout linearLayout;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            userProfile=itemView.findViewById(R.id.chat_userpic);
            userName=itemView.findViewById(R.id.chat_username);
            linearLayout=itemView.findViewById(R.id.singleChat_linearLayout);
        }
    }
}
