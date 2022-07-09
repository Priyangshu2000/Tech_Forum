package com.example.techforum.comment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techforum.ChatPackage.chatAdapter;
import com.example.techforum.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class commentAdapter extends FirebaseRecyclerAdapter<commentModel,commentAdapter.viewHolder> {
    DatabaseReference userDbRef;
   Context context;
   FirebaseUser firebaseUser;
   String phoneNo;
    public commentAdapter(@NonNull FirebaseRecyclerOptions<commentModel> options) {
        super(options);
        userDbRef= FirebaseDatabase.getInstance().getReference();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        phoneNo=firebaseUser.getPhoneNumber();
    }

    @Override
    protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull commentModel model) {

        holder.userComment.setText(model.getComment());
        String rawUser=getRef(position).getKey()+"";
        String user=rawUser.substring(0,13);
//        Toast.makeText(context, user, Toast.LENGTH_SHORT).show();
        userDbRef.child("userDetails").child(user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.userName.setText(snapshot.child("name").getValue(String.class));
                Picasso.get().load(snapshot.child("profilePic").getValue(String.class)).placeholder(R.drawable.ic_baseline_person_24).into(holder.userProfilePic);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "unknown error", Toast.LENGTH_SHORT).show();
            }
        });
//        holder.send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String time= Calendar.getInstance().getTime().toString();
//                String key=phoneNo+time;
//                String comment=holder.typeComment.getText().toString();
//                commentModel cm=new commentModel();
//                cm.setComment(comment);
//                userDbRef.child("commentActivity").setValue(cm);
//            }
//        });
    }
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.singlecomment, parent, false);
        context=parent.getContext();
        return new commentAdapter.viewHolder(view);
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView userProfilePic;
        TextView userName,userComment;
        EditText typeComment;
        Button send;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            userProfilePic=itemView.findViewById(R.id.comment_user_profile_pic);
            userName=itemView.findViewById(R.id.username);
            userComment=itemView.findViewById(R.id.comment_userComment);
//            typeComment=itemView.findViewById(R.id.comment_writeComment);
//            send=itemView.findViewById(R.id.comment_send);
        }
    }
}
