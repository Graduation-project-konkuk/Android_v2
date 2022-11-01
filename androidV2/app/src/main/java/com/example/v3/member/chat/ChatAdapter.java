package com.example.v3.member.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.v3.R;

import java.io.Serializable;
import java.util.LinkedList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> implements Serializable, OnChatItemClickListener {
    LinkedList<ChatItem> items = new LinkedList<>();
    OnChatItemClickListener listener;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.mchat_item,parent,false);

//        itemView.setOnClickListener();

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatItem chatItem = items.get(position);
        holder.setItem(chatItem);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void setOnItemClickListener(OnChatItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(ChatAdapter.ViewHolder holder, View view, int position, String id) {
        if(listener != null){
            listener.onItemClick(holder, view, position, id);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView trainerImg;
        TextView trainerName;
        TextView trainerSex;
        TextView trainerHistoryPeriod;

        public ViewHolder(@NonNull View itemView, final OnChatItemClickListener listener) {
            super(itemView);

            trainerImg = (ImageView) itemView.findViewById(R.id.trainer_img);
            trainerName = (TextView) itemView.findViewById(R.id.trainer_name);
            trainerSex = (TextView) itemView.findViewById(R.id.trainer_sex);
            trainerHistoryPeriod = (TextView) itemView.findViewById(R.id.trainer_historyPeriod);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    ChatItem chatItem = items.get(position);
                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, view, position, chatItem.getId());
                    }
                }
            });

        }

        public void setItem(ChatItem item){
            trainerImg.setImageBitmap(item.trainerImg);
            trainerName.setText(item.getTrainerName());
            trainerSex.setText(item.getTrainerSex());
            trainerHistoryPeriod.setText(item.getTrainerHistoryPeriod());
        }
    }

    public void addItem(ChatItem item){
        items.add(0, item);
    }

    public LinkedList<ChatItem> getItems() {
        return items;
    }

    public void setItems(LinkedList<ChatItem> items) {
        this.items = items;
    }
}
