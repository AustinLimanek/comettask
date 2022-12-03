package com.task.CometTask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String TASK_EXTRA_TAG = "mainTask";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       setupBttns();
       setupUserProfileImageButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupGreeting();
        setupTaskButtons();
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
        String userNameMessage = username + "'s tasks";
        ((TextView)findViewById(R.id.UserName)).setText(userNameMessage);
    }

    public void setupTaskButtons(){
        Button codeJavaButton = findViewById(R.id.JavaButton);
        Button codePythonButton = findViewById(R.id.PythonButton);
        Button codeMetaButton = findViewById(R.id.GeneralButton);
        Intent goToTaskDetailIntent = new Intent(this, TaskDetail.class);

        codeJavaButton.setOnClickListener(v -> {
            goToTaskDetailIntent.putExtra(TASK_EXTRA_TAG, codeJavaButton.getText().toString());
            startActivity(goToTaskDetailIntent);
        });

        codePythonButton.setOnClickListener(v -> {
            goToTaskDetailIntent.putExtra(TASK_EXTRA_TAG, codePythonButton.getText().toString());
            startActivity(goToTaskDetailIntent);
        });

        codeMetaButton.setOnClickListener(v -> {
            goToTaskDetailIntent.putExtra(TASK_EXTRA_TAG, codeMetaButton.getText().toString());
            startActivity(goToTaskDetailIntent);
        });

    }
}