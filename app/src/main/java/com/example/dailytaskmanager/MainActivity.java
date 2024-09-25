package com.example.dailytaskmanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.dailytaskmanager.databinding.ActivityMainBinding;
import com.example.dailytaskmanager.model.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskListener {
    private ActivityMainBinding binding;
    private List<Task> taskList;
    private TaskAdapter taskAdapter;

    private final ActivityResultLauncher<Intent> taskLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String taskName = result.getData().getStringExtra("taskName");
                    String taskTime = result.getData().getStringExtra("taskTime");
                    int position = result.getData().getIntExtra("position", -1);

                    if (taskName != null && taskTime != null) {
                        if (position != -1) {
                            // Edit existing task
                            Task task = taskList.get(position);
                            task.setName(taskName);
                            task.setTime(taskTime);
                        } else {
                            // Add new task
                            Task newTask = new Task(taskName, taskTime);
                            taskList.add(newTask);
                        }
                        taskAdapter.notifyDataSetChanged();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList, this);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(taskAdapter);

        binding.addTaskButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            taskLauncher.launch(intent);
        });
    }

    @Override
    public void onTaskEdit(int position) {
        Task task = taskList.get(position);
        Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
        intent.putExtra("taskName", task.getName());
        intent.putExtra("taskTime", task.getTime());
        intent.putExtra("position", position);
        taskLauncher.launch(intent);
    }

    @Override
    public void onTaskDelete(int position) {
        taskList.remove(position);
        taskAdapter.notifyItemRemoved(position);
    }
}