package com.task.CometTask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button gotToViewAllTasksButton = this.findViewById(R.id.ActivityMainToViewAllTasksButton);
        gotToViewAllTasksButton.setOnClickListener(view -> {
            startActivity(new Intent(this, ViewAllTasks.class));
        });

        Button gotToTaskButton = this.findViewById(R.id.ActivityMainToAddTaskButton);
        gotToTaskButton.setOnClickListener(view -> {
            startActivity(new Intent(this, AddTask.class));
        });
    }
}