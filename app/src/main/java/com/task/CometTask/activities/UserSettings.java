package com.task.CometTask.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;
import com.task.CometTask.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UserSettings extends AppCompatActivity {

    SharedPreferences preferences;
    public final static String TAG = "AddTaskActivity";
    public static final String USERNAME_TAG = "username";
    public static final String TEAM_TAG = "teamname";
    Spinner settingsTeamSpinner;
    CompletableFuture<List<Team>> teamFuture = new CompletableFuture<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        saveValuesToSharedPrefs();

        TextView preName = (TextView) findViewById(R.id.UserNameText);
        preName.setText(preferences.getString(USERNAME_TAG, "user"));

        settingsTeamSpinner = findViewById(R.id.spinner3);

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
                        startActivity(new Intent(this, UserSettings.class));
                    }
                    teamFuture.complete(teams);
                    runOnUiThread(() -> setupSettingsTeamSpinner(teamNames));
                },
                failure -> {
                    teamFuture.complete(null);
                    Log.w(TAG, "Did not read teams successfully from database");
                }
        );
    }

    private void initTeams() {
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

    public void saveValuesToSharedPrefs(){
        // Setup the editor -> sharedPrefs is read only by default
        SharedPreferences.Editor preferenceEditor = preferences.edit();
        Button saveButton = this.findViewById(R.id.SaveUserNameButton);
        saveButton.setOnClickListener(v -> {
            String usernameText = ((EditText) findViewById(R.id.UserNameText)).getText().toString();
            String teamName = settingsTeamSpinner.getSelectedItem().toString();

            preferenceEditor.putString(USERNAME_TAG, usernameText);
            preferenceEditor.putString(TEAM_TAG, teamName);
            preferenceEditor.apply(); // TODO: Nothing saves unless you do this, DONT FORGET!!

            Toast.makeText(this, "Username Saved!", Toast.LENGTH_SHORT).show();
        });
    }

    public void setupSettingsTeamSpinner(ArrayList<String> teamNames){

        settingsTeamSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                teamNames
        ));
    }
}