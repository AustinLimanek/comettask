package com.task.CometTask.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.task.CometTask.R;
import com.task.CometTask.adapter.TaskRecyclerViewAdapter;
import com.task.CometTask.database.TaskDatabase;
import com.task.CometTask.models.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TaskDatabase taskDatabase;
    public static final String DATABASE_NAME = "task_db";
    public static final String TASK_EXTRA_TAG = "mainTask";
    public static final String TASK_EXTRA_ID = "mainTaskId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       setupBttns();
       setupUserProfileImageButton();

        taskDatabase = Room.databaseBuilder(
                        getApplicationContext(),
                        TaskDatabase.class,
                        DATABASE_NAME)
                .fallbackToDestructiveMigration() // If Room gets confused, it tosses your database; don't use this in production!
                .allowMainThreadQueries()
                .build();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupGreeting();
        setupRecyclerview();
        //setupTaskButtons();

    }

   public void setupBttns(){
        Button gotToViewAllTasksButton = this.findViewById(R.id.ActivityMainToViewAllTasksButton);
        gotToViewAllTasksButton.setOnClickListener(view -> {
            startActivity(new Intent(this, ViewAllTasks.class));
        });

        Button gotToTaskButton = this.findViewById(R.id.ActivityMainToAddTaskButton);
        gotToTaskButton.setOnClickListener(view -> {
            startActivity(new Intent(this, AddTask.class));
        });
    }

    public void setupUserProfileImageButton(){
        ImageView userSettingsLink = MainActivity.this.findViewById(R.id.UserSettings);
        userSettingsLink.setOnClickListener(v -> {
            Intent goToUserSettings = new Intent(this, UserSettings.class);
            startActivity(goToUserSettings);
        });
    }

    public void setupGreeting(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = preferences.getString(UserSettings.USERNAME_TAG, "No username");
        String userNameMessage = username.substring(0,1).toUpperCase() + username.substring(1).toLowerCase() + "'s" + " tasks";
        ((TextView)findViewById(R.id.UserName)).setText(userNameMessage);
    }

    public void setupRecyclerview(){
        List<Task> tasks = taskDatabase.taskDao().findAll();

        RecyclerView taskRV = findViewById(R.id.TaskRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        taskRV.setLayoutManager(layoutManager);

        TaskRecyclerViewAdapter adapter = new TaskRecyclerViewAdapter(tasks, this);
        taskRV.setAdapter(adapter);

    }

}

//    public void setupTaskButtons(){
//        Button codeJavaButton = findViewById(R.id.JavaButton);
//        Button codePythonButton = findViewById(R.id.PythonButton);
//        Button codeMetaButton = findViewById(R.id.GeneralButton);
//        Intent goToTaskDetailIntent = new Intent(this, TaskDetail.class);
//
//        codeJavaButton.setOnClickListener(v -> {
//            goToTaskDetailIntent.putExtra(TASK_EXTRA_TAG, codeJavaButton.getText().toString());
//            startActivity(goToTaskDetailIntent);
//        });
//
//        codePythonButton.setOnClickListener(v -> {
//            goToTaskDetailIntent.putExtra(TASK_EXTRA_TAG, codePythonButton.getText().toString());
//            startActivity(goToTaskDetailIntent);
//        });
//
//        codeMetaButton.setOnClickListener(v -> {
//            goToTaskDetailIntent.putExtra(TASK_EXTRA_TAG, codeMetaButton.getText().toString());
//            startActivity(goToTaskDetailIntent);
//        });
//    }
