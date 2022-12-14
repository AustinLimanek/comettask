package com.task.CometTask.activities;

import static com.amplifyframework.core.Amplify.Auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthSession;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoUserPoolTokens;
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult;
import com.amplifyframework.auth.cognito.result.GlobalSignOutError;
import com.amplifyframework.auth.cognito.result.HostedUIError;
import com.amplifyframework.auth.cognito.result.RevokeTokenError;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.task.CometTask.R;
import com.task.CometTask.adapter.TaskRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.SignOutOptions;
import com.amazonaws.mobile.client.UserState;
import com.amazonaws.mobile.client.UserStateDetails;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "mainActivity";
    public static final String DATABASE_NAME = "task_db";
    public static final String TASK_EXTRA_TAG = "mainTask";
    public static final String TASK_EXTRA_ID = "mainTaskId";

    TaskRecyclerViewAdapter adapter;
    List<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       setupBttns();
       setupUserProfileImageButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupGreeting();
        setupRecyclerview();
        silenceButtons();
        //setupTaskButtons();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String teamName = preferences.getString(UserSettings.TEAM_TAG, "No Team");

        Amplify.API.query(
                ModelQuery.list(Task.class),
                success -> {
                    Log.i(DATABASE_NAME, "Read tasks successfully");
                    List<Task> newTasks = new ArrayList<>();
                    for (Task databaseSuperPet : success.getData()) {
                        newTasks.add(databaseSuperPet);
                    }
                    newTasks = newTasks.stream().sorted((a, b) -> {
                        int aTime = (int)(a.getCreatedAt().toDate().getTime()/10000);
                        int bTime = (int)(b.getCreatedAt().toDate().getTime()/10000);
                        if (aTime == bTime)
                            return 0;
                        else if (aTime > bTime)
                            return 1;
                        else return -1;
                    }).filter(task -> task.getTeam().getName().equals(teamName)).collect(Collectors.toList());

                    tasks.addAll(newTasks);

                    runOnUiThread(() -> adapter.notifyDataSetChanged()); // since this runs asynchronously, the adapter may already have rendered, so we have to tell it to update
                },
                failure -> Log.e(DATABASE_NAME, "Failed to read Super Pets from database")
        );

    }

   public void setupBttns(){
        Button gotToViewAllTasksButton = this.findViewById(R.id.ActivityMainToViewAllTasksButton);
        gotToViewAllTasksButton.setOnClickListener(view -> {
            startActivity(new Intent(this, ViewAllTasks.class));
        });

        Button gotToTaskButton = this.findViewById(R.id.ActivityMainToAddTaskButton);
        gotToTaskButton.setOnClickListener(view -> {
            startActivity(new Intent(this, AddTask.class));
        });

       Button gotToUpButton = this.findViewById(R.id.MainSignUp);
       gotToUpButton.setOnClickListener(view -> {
           startActivity(new Intent(this, SignUpActivity.class));
       });

       Button gotToInButton = this.findViewById(R.id.MainSignIn);
       gotToInButton.setOnClickListener(view -> {
           startActivity(new Intent(this, SignInPage.class));
       });

       Button signOutButton = this.findViewById(R.id.mainSignOutButton);
       signOutButton.setOnClickListener(view ->

               Auth.signOut( signOutResult -> {
                   if (signOutResult instanceof AWSCognitoAuthSignOutResult.CompleteSignOut) {
                       // Sign Out completed fully and without errors.
                       Log.i("AuthQuickStart", "Signed out successfully");
                       startActivity(new Intent(this, MainActivity.class));
                   } else if (signOutResult instanceof AWSCognitoAuthSignOutResult.PartialSignOut) {
                       // Sign Out completed with some errors. User is signed out of the device.
                       AWSCognitoAuthSignOutResult.PartialSignOut partialSignOutResult =
                               (AWSCognitoAuthSignOutResult.PartialSignOut) signOutResult;

                       HostedUIError hostedUIError = partialSignOutResult.getHostedUIError();
                       if (hostedUIError != null) {
                           Log.e("AuthQuickStart", "HostedUI Error", hostedUIError.getException());
                           // Optional: Re-launch hostedUIError.getUrl() in a Custom tab to clear Cognito web session.
                       }

                       GlobalSignOutError globalSignOutError = partialSignOutResult.getGlobalSignOutError();
                       if (globalSignOutError != null) {
                           Log.e("AuthQuickStart", "GlobalSignOut Error", globalSignOutError.getException());
                           // Optional: Use escape hatch to retry revocation of globalSignOutError.getAccessToken().
                       }

                       RevokeTokenError revokeTokenError = partialSignOutResult.getRevokeTokenError();
                       if (revokeTokenError != null) {
                           Log.e("AuthQuickStart", "RevokeToken Error", revokeTokenError.getException());
                           // Optional: Use escape hatch to retry revocation of revokeTokenError.getRefreshToken().
                       }
                   } else if (signOutResult instanceof AWSCognitoAuthSignOutResult.FailedSignOut) {
                       AWSCognitoAuthSignOutResult.FailedSignOut failedSignOutResult =
                               (AWSCognitoAuthSignOutResult.FailedSignOut) signOutResult;
                       // Sign Out failed with an exception, leaving the user signed in.
                       Log.e("AuthQuickStart", "Sign out Failed", failedSignOutResult.getException());
                   }
               })

       );

   }

   public void silenceButtons(){

       Button gotToUpButton = this.findViewById(R.id.MainSignUp);
       Button gotToInButton = this.findViewById(R.id.MainSignIn);
       Button signOutButton = this.findViewById(R.id.mainSignOutButton);

       Amplify.Auth.getCurrentUser(
               success->{
                   String username = success.getUsername();
                   Log.i(TAG, "the user is logged in and the sing in and sign up buttons are now gone");
                   gotToInButton.setVisibility(View.GONE);
                   gotToUpButton.setVisibility(View.GONE);
               },
               failure->{
                   signOutButton.setVisibility(View.GONE);
               }
       );

   }

    public void setupUserProfileImageButton(){
        ImageView userSettingsLink = MainActivity.this.findViewById(R.id.UserSettings);
        userSettingsLink.setOnClickListener(v -> {
            Intent goToUserSettings = new Intent(this, UserSettings.class);
            startActivity(goToUserSettings);
        });
    }

    public void setupGreeting(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = preferences.getString(UserSettings.USERNAME_TAG, "No username");
        String userNameMessage;
        if(username.length() > 0){
            userNameMessage = username.substring(0,1).toUpperCase() + username.substring(1).toLowerCase() + "'s" + " tasks";
        }
        else{
            userNameMessage = "No username";
        }
        String teamName = preferences.getString(UserSettings.TEAM_TAG, "No Team");
        userNameMessage += ": Team " + teamName;
        ((TextView)findViewById(R.id.UserName)).setText(userNameMessage);
    }

    public void setupRecyclerview(){
        tasks = new ArrayList<>();

        RecyclerView taskRV = findViewById(R.id.TaskRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        taskRV.setLayoutManager(layoutManager);

        adapter = new TaskRecyclerViewAdapter(tasks, this);
        taskRV.setAdapter(adapter);

    }

}

