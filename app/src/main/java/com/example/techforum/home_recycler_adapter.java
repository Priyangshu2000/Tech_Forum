package com.example.techforum;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.Locale;

public class home_recycler_adapter extends FirebaseRecyclerAdapter<posts,home_recycler_adapter.viewHolder> {
FirebaseDatabase firebaseDatabase;
DatabaseReference databaseReference,postDetails;
FirebaseUser user;
Context context;
int flag;
String likes;
String phoneNO;
String profilePic;
    public home_recycler_adapter(@NonNull FirebaseRecyclerOptions<posts> options) {
        super(options);
        user = FirebaseAuth.getInstance().getCurrentUser();
        phoneNO = user.getPhoneNumber();
    }

    @Override
    protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull posts model) {
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context,commentActivity.class);
                intent.putExtra("postId",getRef(holder.getBindingAdapterPosition()).getKey()+"");
                context.startActivity(intent);
            }
        });
        flag=0;
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("likeStore");
        postDetails= firebaseDatabase.getReference().child("postDetails");
        holder.likesNO.setText("0");
        postDetails.child(getRef(holder.getBindingAdapterPosition()).getKey()+"").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int pos=holder.getBindingAdapterPosition();
                String s=snapshot.child("likes").getValue(String.class)+"";
                holder.likesNO.setText(s);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos=holder.getBindingAdapterPosition();
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(getRef(pos).getKey()+"").hasChild(phoneNO)){
                            holder.likes.setImageResource(R.drawable.ic_baseline_thumb_up_off_alt_24);
//                            String like= getItem(pos).getLikes();
                            postDetails.child(getRef(pos).getKey()+"").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    likes=snapshot.child("likes").getValue(String.class);
                                    int newLikes=Integer.parseInt(likes)-1;
                                    postDetails.child(getRef(pos).getKey()+"").child("likes").setValue(newLikes+"");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                            databaseReference.child(getRef(pos).getKey()+"").child(phoneNO).removeValue();
                        }
                        else{
                            holder.likes.setImageResource(R.drawable.ic_baseline_thumb_up_24);

                            postDetails.child(getRef(pos).getKey()+"").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    likes=snapshot.child("likes").getValue(String.class);
                                    int newLikes=Integer.parseInt(likes)+1;
                                    postDetails.child(getRef(pos).getKey()+"").child("likes").setValue(newLikes+"");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            databaseReference.child(getRef(pos).getKey()+"").child(phoneNO).setValue(true);

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Toast.makeText(context, "here is some eror", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        databaseReference.child(getRef(holder.getBindingAdapterPosition()).getKey()+"").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(phoneNO)){
                    holder.likes.setImageResource(R.drawable.ic_baseline_thumb_up_24);
                }
                else if(!snapshot.hasChild(phoneNO))
                    holder.likes.setImageResource(R.drawable.ic_baseline_thumb_up_off_alt_24);
                else
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.caption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToDetailPost(holder,holder.getBindingAdapterPosition(),model,v);
            }
        });
        holder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToDetailPost(holder,holder.getBindingAdapterPosition(),model,v);
            }
        });






        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference("userDetails").child(phoneNO);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDetails details=snapshot.getValue(userDetails.class);
                assert details != null;
                profilePic=details.getProfilePic();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context,"Cannot reach server "+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });






        holder.username.setText(model.getUserName());
        holder.caption.setText(model.getCaption());
        holder.time.setText(model.getTime());
        Picasso.get().load(profilePic).into(holder.profilePic);
        Picasso.get().load(model.getPostImage()).into(holder.postImage);
    }

    private void sendDataToDetailPost(viewHolder holder, int position, posts model,View v) {
        Intent intent=new Intent(context,ViewPostImage.class);
        intent.putExtra("username",model.getUserName());
        intent.putExtra("imageUrl",model.getPostImage());
        intent.putExtra("profilePic",model.getProfilePic());
        intent.putExtra("post",model.getCaption());
        intent.putExtra("time",model.getTime());
        context.startActivity(intent);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.singlepost, parent, false);
        context=parent.getContext();
        return new home_recycler_adapter.viewHolder(view);
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        ImageView profilePic,postImage;
        TextView username,time,caption;
        ImageView likes,comment;
        TextView likesNO;
        LinearLayout commentLayout;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            likes=itemView.findViewById(R.id.post_likes);
            username=itemView.findViewById(R.id.postusername);
            time=itemView.findViewById(R.id.time);
            caption=itemView.findViewById(R.id.post);
            profilePic=itemView.findViewById(R.id.userpic);
            postImage=itemView.findViewById(R.id.postpic);
            likesNO=itemView.findViewById(R.id.post_likes_no);
            comment=itemView.findViewById(R.id.post_comments);
        }
    }
}
