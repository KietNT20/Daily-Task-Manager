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
    public static final String EXTRA_TASK_NAME = "taskName";
    public static final String EXTRA_TASK_TIME = "taskTime";
    public static final String EXTRA_POSITION = "position";

    private List<Task> taskList;
    private TaskAdapter taskAdapter;

    private final ActivityResultLauncher<Intent> taskLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String taskName = result.getData().getStringExtra(EXTRA_TASK_NAME);
                    String taskTime = result.getData().getStringExtra(EXTRA_TASK_TIME);
                    int position = result.getData().getIntExtra(EXTRA_POSITION, -1);

                    if (taskName != null && taskTime != null) {
                        if (position != -1) {
                            // Edit existing task
                            Task task = taskList.get(position);
                            task.setName(taskName);
                            task.setTime(taskTime);
                            taskAdapter.notifyItemChanged(position);
                        } else {
                            // Add new task
                            Task newTask = new Task(taskName, taskTime);
                            taskList.add(newTask);
                            taskAdapter.notifyItemInserted(taskList.size() - 1);
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.dailytaskmanager.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
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
        intent.putExtra(EXTRA_TASK_NAME, task.getName());
        intent.putExtra(EXTRA_TASK_TIME, task.getTime());
        intent.putExtra(EXTRA_POSITION, position);
        taskLauncher.launch(intent);
    }

    @Override
    public void onTaskDelete(int position) {
        taskList.remove(position);
        taskAdapter.notifyItemRemoved(position);
    }
}