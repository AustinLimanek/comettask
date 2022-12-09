package com.task.CometTask.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.task.CometTask.R;
import com.task.CometTask.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class TaskDetail extends AppCompatActivity {
    public static final String DATABASE_NAME = "task_db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);



        consumeProductExtra();
    }

    public void consumeProductExtra(){
        Intent callingIntent = getIntent();
        String taskName = null;
        String taskId = null;
        if(callingIntent != null){
            taskName = callingIntent.getStringExtra(MainActivity.TASK_EXTRA_TAG);
            taskId = callingIntent.getStringExtra(MainActivity.TASK_EXTRA_ID);
        }
        TextView taskView = findViewById(R.id.TaskName);
        TextView taskViewLong = findViewById(R.id.TaskDescription);
        if(taskName != null) {
            taskView.setText(taskName);
            Amplify.API.query(
                    ModelQuery.get(Task.class, taskId),
                    success -> {
                        final String taskBody = success.getData().getTaskDescription();
                        if (!taskBody.equals("")) taskViewLong.setText(taskBody);
                    },
                    failure -> Log.w(DATABASE_NAME, "Failed to read description from database")
            );
        }
        else {
            taskView.setText("No Task");
        }
    }

}