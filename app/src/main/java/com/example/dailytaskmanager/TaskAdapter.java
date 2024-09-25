package com.example.dailytaskmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailytaskmanager.databinding.ItemTaskBinding;
import com.example.dailytaskmanager.model.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> taskList;
    private OnTaskListener onTaskListener;

    public TaskAdapter(List<Task> taskList, OnTaskListener onTaskListener) {
        this.taskList = taskList;
        this.onTaskListener = onTaskListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTaskBinding binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TaskViewHolder(binding, onTaskListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ItemTaskBinding binding;
        OnTaskListener onTaskListener;

        public TaskViewHolder(ItemTaskBinding binding, OnTaskListener onTaskListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.onTaskListener = onTaskListener;

            binding.editTaskButton.setOnClickListener(this);
            binding.deleteTaskButton.setOnClickListener(this);
        }

        public void bind(Task task) {
            binding.taskNameTextView.setText(task.getName());
            binding.taskTimeTextView.setText(task.getTime());
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.editTaskButton) {
                onTaskListener.onTaskEdit(getAdapterPosition());
            } else if (v.getId() == R.id.deleteTaskButton) {
                onTaskListener.onTaskDelete(getAdapterPosition());
            }
        }
    }

    public interface OnTaskListener {
        void onTaskEdit(int position);
        void onTaskDelete(int position);
    }
}