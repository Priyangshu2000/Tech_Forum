package com.example.techforum.ChatBot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techforum.R;

import java.util.List;

public class MessageAdapter extends  RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    List<Message>messageList;

    public MessageAdapter(List<Message>messageList) {
        this.messageList=messageList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chatView= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,null);
        return new MyViewHolder(chatView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Message message=messageList.get(position);
        Boolean sendByUser=message.sendByUser;
        if(sendByUser){
            holder.leftView.setVisibility(View.GONE);
            holder.rightView.setVisibility(View.VISIBLE);
            holder.rightText.setText(message.message);
        }else{
            holder.rightView.setVisibility(View.GONE);
            holder.leftView.setVisibility(View.VISIBLE);
            holder.leftText.setText(message.message);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftView,rightView;
        TextView leftText,rightText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
             leftView=itemView.findViewById(R.id.left_chat_view);
             rightView=itemView.findViewById(R.id.right_chat_view);
             leftText=itemView.findViewById(R.id.left_chat_text_view);
             rightText=itemView.findViewById(R.id.right_chat_text_view);
        }
    }

}
