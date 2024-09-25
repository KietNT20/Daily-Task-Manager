package com.example.dailytaskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dailytaskmanager.databinding.ActivityAddTaskBinding;

import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {
    private ActivityAddTaskBinding binding;
    private int editPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Check if editing an existing task
        String taskName = getIntent().getStringExtra("taskName");
        String taskTime = getIntent().getStringExtra("taskTime");
        editPosition = getIntent().getIntExtra("position", -1);

        if (taskName != null && taskTime != null) {
            binding.taskNameEditText.setText(taskName);
            setTimePickerFromString(taskTime);
            binding.addTaskButton.setText("Save");
        }

        binding.addTaskButton.setOnClickListener(v -> {
            String newTaskName = binding.taskNameEditText.getText().toString().trim();
            String newTaskTime = getTimeFromPicker();

            if (!TextUtils.isEmpty(newTaskName)) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("taskName", newTaskName);
                resultIntent.putExtra("taskTime", newTaskTime);
                resultIntent.putExtra("position", editPosition);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Please enter a task name", Toast.LENGTH_SHORT).show();
                binding.taskNameEditText.setError("Task name is required");
            }
        });
    }

    private String getTimeFromPicker() {
        int hour = binding.taskTimePicker.getHour();
        int minute = binding.taskTimePicker.getMinute();
        return String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
    }

    private void setTimePickerFromString(String time) {
        String[] parts = time.split(":");
        if (parts.length == 2) {
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);
            binding.taskTimePicker.setHour(hour);
            binding.taskTimePicker.setMinute(minute);
        }
    }
}