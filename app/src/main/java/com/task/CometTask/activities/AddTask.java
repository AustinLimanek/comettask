package com.task.CometTask.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.task.CometTask.R;
import com.task.CometTask.database.TaskDatabase;
import com.task.CometTask.models.Task;

import java.util.Date;

public class AddTask extends AppCompatActivity {

    TaskDatabase taskDatabase;
    Spinner taskStatusSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        taskStatusSpinner = findViewById(R.id.AddTaskSpinnerStatus);
        //TODO Step: 5-6 instantiate the DB wherever you need it
        taskDatabase = Room.databaseBuilder(
                        getApplicationContext(),
                        TaskDatabase.class,
                        MainActivity.DATABASE_NAME)
                .fallbackToDestructiveMigration() // If Room gets confused, it tosses your database; don't use this in production!
                .allowMainThreadQueries()
                .build();
        setupTypeSpinner();
        setupSaveBttn();
    }

    public void setupTypeSpinner(){

        taskStatusSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                Task.TaskStatusEnum.values()
        ));
    }

    //TODO Step: 5-5 save superPet to database onClick with the DAO
    public void setupSaveBttn(){

        Button submitButton = findViewById(R.id.AddTaskSubmitButton);

        submitButton.setOnClickListener(v -> {

            Task newTask = new Task(
                    ((EditText) findViewById(R.id.AddTaskTaskName)).getText().toString(),
                    ((EditText) findViewById(R.id.AddTaskTaskBody)).getText().toString(),
                    Task.TaskStatusEnum.fromString(taskStatusSpinner.getSelectedItem().toString()),
                    new Date()
            );

            taskDatabase.taskDao().insertASuperPet(newTask);
            Toast.makeText(this, "Task Saved!", Toast.LENGTH_SHORT).show();

        });
    }
}