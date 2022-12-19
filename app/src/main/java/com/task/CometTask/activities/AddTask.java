package com.task.CometTask.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskStateEnum;
import com.amplifyframework.datastore.generated.model.Team;
import com.task.CometTask.R;


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AddTask extends AppCompatActivity {
    public final static String TAG = "AddTaskActivity";
    Spinner taskStatusSpinner;
    Spinner teamSpinner;
    CompletableFuture<List<Team>> teamFuture = new CompletableFuture<>();
    ActivityResultLauncher<Intent> activityResultLauncher;
    String s3ImageKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        activityResultLauncher = getImagePickingActivityResultLauncher();

//        Team blue = Team.builder()
//                .name("blue")
//                .build();
//        Amplify.API.mutate(
//                ModelMutation.create(blue),
//                success -> {},
//                failure -> {}


        teamSpinner = findViewById(R.id.teamSpinner);

        Amplify.API.query(
                ModelQuery.list(Team.class),
                success -> {
                    Log.i(TAG, "Read Team Successful");
                    ArrayList<String> teamNames = new ArrayList<>();
                    ArrayList<Team> teams = new ArrayList<>();
                    int count = 0;
                    for(Team team : success.getData()){
                        teamNames.add(team.getName());
                        teams.add(team);
                        count += 1;
                    }
                    if(count < 1){
                        initTeams();
                        startActivity(new Intent(this, AddTask.class));
                    }

                    teamFuture.complete(teams);
                    runOnUiThread(() -> setupTeamSpinner(teamNames));
                },
                failure -> {
                    teamFuture.complete(null);
                    Log.w(TAG, "Did not read teams successfully from database");
                }
        );

        taskStatusSpinner = findViewById(R.id.AddTaskSpinnerStatus);

        setupTypeSpinner();
        setupSaveBttn();
        setUpAddImageButton();
    }

    private void initTeams(){

        Team blue = Team.builder()
                .name("blue")
                .build();
        Amplify.API.mutate(
                ModelMutation.create(blue),
                achieved -> {
                },
                failure -> {
                });
        Team red = Team.builder()
                .name("red")
                .build();
        Amplify.API.mutate(
                ModelMutation.create(red),
                achieved -> {
                },
                failure -> {
                });
        Team green = Team.builder()
                .name("green")
                .build();
        Amplify.API.mutate(
                ModelMutation.create(green),
                achieved -> {
                },
                failure -> {
                });

    }

    public void setupTeamSpinner(ArrayList<String> teamNames){

        teamSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                teamNames
        ));
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
            if(((EditText)findViewById(R.id.AddTaskTaskName)).getText().toString().equals("")){
                Toast.makeText(this, "Please enter a task name", Toast.LENGTH_SHORT).show();
            }
            else{
            saveTask();
            }
        });
    }


    private void setUpAddImageButton(){
        findViewById(R.id.addImageButton).setOnClickListener(v->{
            launchImageSelectionIntent();
        });
    }

    private void launchImageSelectionIntent(){
        Intent imageFilePickingIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageFilePickingIntent.setType("*/*");
        imageFilePickingIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg","image/png"});

        activityResultLauncher.launch(imageFilePickingIntent);
    }

    private ActivityResultLauncher<Intent> getImagePickingActivityResultLauncher(){
        ActivityResultLauncher<Intent> imagePickingActivityResultLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                                Uri pickedImageFileUri = null;
                            if(result.getData() == null) {
                                pickedImageFileUri = Uri.parse("No File");
                            }
                            else{
                                pickedImageFileUri = result.getData().getData();
                            }
                            try{
                                InputStream pickedImageInputStream = getContentResolver().openInputStream(pickedImageFileUri);
                                String pickedImageFilename = getFileNameFromUri(pickedImageFileUri);
                                Log.i(TAG, "success in getting input " + pickedImageFilename);

                                uploadInputStreamToS3(pickedImageInputStream, pickedImageFilename, pickedImageFileUri);
                            } catch (FileNotFoundException fileNotFoundException){
                                Log.e(TAG, "could not get file from file picker" + fileNotFoundException.getMessage());
                            }
                        }
                );
        return imagePickingActivityResultLauncher;
    }

    private void uploadInputStreamToS3(InputStream pickedImageInputStream, String pickedImageFilename, Uri pickedImageFileUri){
        Amplify.Storage.uploadInputStream(
                pickedImageFilename,
                pickedImageInputStream,
                success -> {
                    Log.i(TAG, "succeeded in getting file uploaded to S3! Key is " + success.getKey());
                    s3ImageKey = success.getKey();
                    ImageView taskImage = findViewById(R.id.imageViewContent);
                    InputStream pickedImageInputStreamCopy = null;
                    try{
                        pickedImageInputStreamCopy = getContentResolver().openInputStream(pickedImageFileUri);
                        Log.i(TAG, "This is the uri" + pickedImageFileUri.toString());
                    }catch(FileNotFoundException fileNotFoundException){
                        Log.e(TAG, "could not find file" + fileNotFoundException.getMessage(), fileNotFoundException);
                    }
                  taskImage.setImageBitmap(BitmapFactory.decodeStream(pickedImageInputStreamCopy));
                },
                failure -> Log.e(TAG, "failure in uploading file to S3")
        );
    }

    private void saveTask(){
        String selectedTeamName = teamSpinner.getSelectedItem().toString();
        List<Team> teams = null;
        try{
            teams = teamFuture.get();
        }
        catch(InterruptedException interruptedException){
            Log.e(TAG, "InterruptedExcepetion while getting teams");
            Thread.currentThread().interrupt();
        }
        catch(ExecutionException executionException){
            Log.e(TAG, "ExecutionException while getting teams");
        }

        Team selectedTeam = teams.stream().filter(team -> team.getName().equals(selectedTeamName)).findAny().orElseThrow(RuntimeException::new);

        Task newTask = Task.builder()
                .taskTitle(((EditText)findViewById(R.id.AddTaskTaskName)).getText().toString())
                .taskDescription(((EditText)findViewById(R.id.AddTaskTaskBody)).getText().toString())
                .state((TaskStateEnum)taskStatusSpinner.getSelectedItem())
                .team(selectedTeam)
                .s3ImageKey(s3ImageKey)
                .build();

        Amplify.API.mutate(
                ModelMutation.create(newTask),
                success -> Log.i(TAG, "AddTaskActivity.onCreate(): made a task successfully!"),
                failure -> Log.w(TAG, "AddTaskActivity.onCreate(): failed to make a task", failure)
        );

        Toast.makeText(this, "Task Saved!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, AddTask.class));
    }

    // Taken from https://stackoverflow.com/a/25005243/16889809
    @SuppressLint("Range")
    public String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

}