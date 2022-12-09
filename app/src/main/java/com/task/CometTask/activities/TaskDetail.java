package com.task.CometTask.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.amplifyframework.datastore.generated.model.Task;
import com.task.CometTask.R;
import com.task.CometTask.activities.MainActivity;


public class TaskDetail extends AppCompatActivity {


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
            String id = taskId;
            //Task task = taskDatabase.taskDao().findById(id);
            //String taskBody = task.getBody();
            //if (!taskBody.equals("")) taskViewLong.setText(taskBody);
        }
        else {
            taskView.setText("No Task");
        }
    }

}