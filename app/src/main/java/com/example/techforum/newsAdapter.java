package com.example.techforum;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techforum.apiPackage.Articles;
import com.example.techforum.apiPackage.NewsModal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class newsAdapter extends RecyclerView.Adapter<newsAdapter.viewHolder> {
    Context context;
    ArrayList<Articles>modelClass;

    public newsAdapter(Context context, ArrayList<Articles> modelClass) {
        this.context = context;
        this.modelClass = modelClass;
    }

    @NonNull
    @Override
    public newsAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.single_news,null,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull newsAdapter.viewHolder holder, int position) {
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context,DetailedNews.class);
                intent.putExtra("url",modelClass.get(holder.getBindingAdapterPosition()).getUrl());
                context.startActivity(intent);
            }
        });
        holder.heading.setText(modelClass.get(position).getTitle());
        holder.content.setText(modelClass.get(position).getDescription());
        holder.publishedAt.setText("Published At: "+modelClass.get(position).getPublishedAt());
        Picasso.get().load(modelClass.get(position).getUrlToImage()).placeholder(R.drawable.ic_outline_image_24).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return modelClass.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder {
        TextView heading,content,publishedAt;
        ImageView image;
        CardView cardView;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            heading=itemView.findViewById(R.id.heading);
            content=itemView.findViewById(R.id.content);
            publishedAt=itemView.findViewById(R.id.publishedAt);
            image=itemView.findViewById(R.id.image);
            cardView=itemView.findViewById(R.id.newsCardview);
        }
    }
}