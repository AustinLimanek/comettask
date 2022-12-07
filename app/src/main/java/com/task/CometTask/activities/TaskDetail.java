package com.task.CometTask.activities;

import static com.task.CometTask.activities.MainActivity.DATABASE_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.task.CometTask.R;
import com.task.CometTask.activities.MainActivity;
import com.task.CometTask.database.TaskDatabase;
import com.task.CometTask.models.Task;

public class TaskDetail extends AppCompatActivity {
    TaskDatabase taskDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        taskDatabase = Room.databaseBuilder(
                        getApplicationContext(),
                        TaskDatabase.class,
                        DATABASE_NAME)
                .fallbackToDestructiveMigration() // If Room gets confused, it tosses your database; don't use this in production!
                .allowMainThreadQueries()
                .build();

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
            long id = Long.parseLong(taskId);
            Task task = taskDatabase.taskDao().findById(id);
            String taskBody = task.getBody();
            if (!taskBody.equals("")) taskViewLong.setText(taskBody);
        }
        else {
            taskView.setText("No Task");
        }
    }

}