package com.task.CometTask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Button submitButton = this.findViewById(R.id.AddTaskSubmitButton);
        submitButton.setOnClickListener(view -> {
            TextView cheers = this.findViewById(R.id.viewAfterSubmit);
            cheers.setText("Successfully Submitted");
        });

    }
}