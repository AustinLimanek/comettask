package com.task.CometTask.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.task.CometTask.R;
import com.task.CometTask.adapter.TaskRecyclerViewAdapter;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    public static final String DATABASE_NAME = "task_db";
    public static final String TASK_EXTRA_TAG = "mainTask";
    public static final String TASK_EXTRA_ID = "mainTaskId";

    TaskRecyclerViewAdapter adapter;
    List<Task> tasks;

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
       setupRecyclerview();
        //setupTaskButtons();
        Amplify.API.query(
                ModelQuery.list(Task.class),
                success -> {
                    Log.i(DATABASE_NAME, "Read tasks successfully");
                    List<Task> newTasks = new ArrayList<>();
                    for (Task databaseSuperPet : success.getData()) {
                        newTasks.add(databaseSuperPet);
                    }
                    newTasks = newTasks.stream().sorted((a, b) -> {
                        int aTime = (int)(a.getCreatedAt().toDate().getTime()/10000);
                        int bTime = (int)(b.getCreatedAt().toDate().getTime()/10000);
                        if (aTime == bTime)
                            return 0;
                        else if (aTime > bTime)
                            return 1;
                        else return -1;
                    }).collect(Collectors.toList());

                    tasks.addAll(newTasks);

                    runOnUiThread(() -> adapter.notifyDataSetChanged()); // since this runs asynchronously, the adapter may already have rendered, so we have to tell it to update
                },
                failure -> Log.e(DATABASE_NAME, "Failed to read Super Pets from database")
        );

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
        tasks = new ArrayList<>();

        RecyclerView taskRV = findViewById(R.id.TaskRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        taskRV.setLayoutManager(layoutManager);

        adapter = new TaskRecyclerViewAdapter(tasks, this);
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
