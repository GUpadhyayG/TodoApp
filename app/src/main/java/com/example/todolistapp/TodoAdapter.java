package com.example.todolistapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {
    private List<String> todoList;
    private OnItemCheckListener onItemCheckListener;


    public interface OnItemCheckListener {
        void onItemCheck(String item);
    }

    public TodoAdapter(List<String> todoList, OnItemCheckListener onItemCheckListener) {
        this.todoList = todoList;
        this.onItemCheckListener = onItemCheckListener;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        String task = todoList.get(position);
        holder.textView.setText(task);
        holder.itemView.setOnClickListener(v -> {
            if (onItemCheckListener != null) {
                onItemCheckListener.onItemCheck(task);
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}

