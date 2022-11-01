package com.example.v3.member.plan_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.v3.R;

import java.io.Serializable;
import java.util.LinkedList;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> implements Serializable {
    private LinkedList<PlanItem> items = new LinkedList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.mplan_item,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlanItem item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView plan_name_date;
        TextView plan_name_weight;
        TextView plan_name_exercise;
        TextView plan_name_reps;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            plan_name_date = itemView.findViewById(R.id.plan_name_date);
            plan_name_weight = itemView.findViewById(R.id.plan_name_weight);
            plan_name_exercise = itemView.findViewById(R.id.plan_name_exercise);
            plan_name_reps = itemView.findViewById(R.id.plan_name_reps);

        }

        public void setItem(PlanItem item){
            plan_name_date.setText(item.getDate());
            plan_name_weight.setText(item.getWeight());
            plan_name_exercise.setText(item.getExercise());
            plan_name_reps.setText(item.getReps());
        }
    }
    public void addItem(PlanItem item){
        items.add(0, item);
    }

    public LinkedList<PlanItem> getItems() {
        return items;
    }

    public void setItems(LinkedList<PlanItem> items){
        this.items = items;
    }

    public PlanItem getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, PlanItem item){
        items.set(position, item);
    }
}

