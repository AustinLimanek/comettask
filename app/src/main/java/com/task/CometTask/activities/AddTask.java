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
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskStateEnum;
import com.amplifyframework.datastore.generated.model.Team;
import com.task.CometTask.R;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AddTask extends AppCompatActivity {
    public final static String TAG = "AddTaskActivity";
    Spinner taskStatusSpinner;
    Spinner teamSpinner;
    CompletableFuture<List<Team>> teamFuture = new CompletableFuture<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

//        Team blue = Team.builder()
//                .name("blue")
//                .build();
//        Team red = Team.builder()
//                .name("red")
//                .build();
//        Team green = Team.builder()
//                .name("green")
//                .build();
//        Amplify.API.mutate(
//                ModelMutation.create(blue),
//                success -> {},
//                failure -> {}
//        );
//        Amplify.API.mutate(
//                ModelMutation.create(red),
//                success -> {},
//                failure -> {}
//        );
//        Amplify.API.mutate(
//                ModelMutation.create(green),
//                success -> {},
//                failure -> {}
//        );

        teamSpinner = findViewById(R.id.teamSpinner);

        Amplify.API.query(
                ModelQuery.list(Team.class),
                success -> {
                    Log.i(TAG, "Read Team Successful");
                    ArrayList<String> teamNames = new ArrayList<>();
                    ArrayList<Team> teams = new ArrayList<>();
                    for(Team team : success.getData()){
                        teamNames.add(team.getName());
                        teams.add(team);
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