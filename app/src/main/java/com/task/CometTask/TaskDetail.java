package com.task.CometTask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
        if(callingIntent != null){
            taskName = callingIntent.getStringExtra(MainActivity.TASK_EXTRA_TAG);
        }
        TextView taskView = findViewById(R.id.TaskName);
        if(taskName != null) {
            taskView.setText(taskName);
        }
        else {
            taskView.setText("No Task");
        }
    }

}