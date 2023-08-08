package com.example.techforum.Message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techforum.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class MessageAdapter extends RecyclerView.Adapter{
    ArrayList<MessageModel>messages;
    Context context;
    final int SENDER_VIEW_TYPE=1;
    final int RECIEVER_VIEW_TYPE=0;

    public MessageAdapter(ArrayList<MessageModel> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==SENDER_VIEW_TYPE){
            View view= LayoutInflater.from(context).inflate(R.layout.send_message_container,parent,false);
            return new SenderViewHolder(view);
        }
        View view= LayoutInflater.from(context).inflate(R.layout.recieve_container,parent,false);
        return new RecieverViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).getUid().equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber())){
            return SENDER_VIEW_TYPE;
        }
        return RECIEVER_VIEW_TYPE;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
     MessageModel model= messages.get(position);
     if(holder.getClass()==SenderViewHolder.class){
         ((SenderViewHolder) holder).senderMsg.setText(model.getMessage());
         ((SenderViewHolder) holder).timestamp.setText(String.valueOf(model.getTime()));
     }
     else{
         ((RecieverViewHolder) holder).recieverMsg.setText(model.getMessage());
         ((RecieverViewHolder) holder).timestamp.setText(String.valueOf(model.getTime()));
     }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class RecieverViewHolder extends RecyclerView.ViewHolder{
        TextView recieverMsg,timestamp;
        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            recieverMsg=itemView.findViewById(R.id.recieve_textmesssage);
            timestamp=itemView.findViewById(R.id.recieve_time);
        }
    }
    public static class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView senderMsg,timestamp;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg=itemView.findViewById(R.id.send_textmesssage);
            timestamp=itemView.findViewById(R.id.send_time);
        }
    }
}
