package com.task.CometTask.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.task.CometTask.R;
import com.task.CometTask.activities.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class TaskDetail extends AppCompatActivity {
    public static final String DATABASE_NAME = "task_db";
    public static final String TAG = "TaskDetailPage";

    String fileName ="water.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        consumeProductExtra();
    }

    @Override
    protected void onResume() {
        super.onResume();

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
                        fileName = success.getData().getS3ImageKey();
                        if(!(fileName == null)){
                            fileName = fileName.substring(7);
                            try {
                                setupTaskImage();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.i(TAG, "successfully looked up task. This is the fileName: "+ fileName);
                    },
                    failure -> Log.w(DATABASE_NAME, "Failed to read description from database")
            );
        }
        else {
            taskView.setText("No Task");
        }

    }

    public void setupTaskImage() throws FileNotFoundException {
            Amplify.Storage.downloadFile(
                    fileName,
                    new File(getApplicationContext().getFilesDir() + "/" + fileName),
                    result -> {
                        ImageView taskImage = findViewById(R.id.imageView);
                        Bitmap bitmap = BitmapFactory.decodeFile(result.getFile().getAbsolutePath());
                        taskImage.setImageBitmap(bitmap);
                        Log.i("MyAmplifyApp", "Successfully downloaded: " + fileName + " Please tells me this is true: " + "charmander.png");
                    },
                    error -> Log.e("MyAmplifyApp", "Download Failure " + fileName + " " + "charmander.png", error)
            );
    }

}