package com.example.v3.member.exerciseList.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.v3.R;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> implements OnExerciseItemClickListener {

    ArrayList<ExerciseItem> items = new ArrayList<>();
    OnExerciseItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.exercise_item,parent,false);
        return new ViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExerciseItem item = items.get(position);
        holder.setItem(item);
        holder.setImageItem(item);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickListener(OnExerciseItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView exerciseNameItem;

        ImageView exerciseImage;
        public ViewHolder(@NonNull View itemView, final OnExerciseItemClickListener listener) {
            super(itemView);

            exerciseNameItem = itemView.findViewById(R.id.exerciseNameItem);
            exerciseImage = itemView.findViewById(R.id.exerciseImageItem);


            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(ExerciseItem item){
            exerciseNameItem.setText(item.getName());
        }
        public void setImageItem(ExerciseItem item){
            exerciseImage.setImageResource(item.getImage());
            Glide.with(itemView).load(item.getImage()).into(exerciseImage);


        }
    }

    public void addItem(ExerciseItem item){
        items.add(item);
    }

    public void setItems(ArrayList<ExerciseItem> items){
        this.items = items;
    }

    public ExerciseItem getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, ExerciseItem item){
        items.set(position, item);
    }
}
