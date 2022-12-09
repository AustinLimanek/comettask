package com.task.CometTask.activities;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskStateEnum;
import com.task.CometTask.R;



import java.util.Date;

public class AddTask extends AppCompatActivity {
    public final static String TAG = "AddTaskActivity";
    Spinner taskStatusSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        taskStatusSpinner = findViewById(R.id.AddTaskSpinnerStatus);

        setupTypeSpinner();
        setupSaveBttn();
    }

    public void setupTypeSpinner(){

        taskStatusSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TaskStateEnum.values()
        ));
    }

    //TODO Step: 5-5 save superPet to database onClick with the DAO
    public void setupSaveBttn(){

        Button submitButton = findViewById(R.id.AddTaskSubmitButton);

        submitButton.setOnClickListener(v -> {

            Task newTask = Task.builder()
                    .taskTitle(((EditText)findViewById(R.id.AddTaskTaskName)).getText().toString())
                    .taskDescription(((EditText)findViewById(R.id.AddTaskTaskBody)).getText().toString())
                    .state((TaskStateEnum)taskStatusSpinner.getSelectedItem())
                    .build();

            Amplify.API.mutate(
                    ModelMutation.create(newTask),
                    success -> Log.i(TAG, "AddTaskActivity.onCreate(): made a task successfully!"),
                    failure -> Log.w(TAG, "AddTaskActivity.onCreate(): failed to make a task", failure)
            );

            Toast.makeText(this, "Task Saved!", Toast.LENGTH_SHORT).show();

        });
    }
}