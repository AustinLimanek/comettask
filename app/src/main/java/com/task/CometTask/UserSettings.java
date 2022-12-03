package com.task.CometTask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserSettings extends AppCompatActivity {

    SharedPreferences preferences;
    public static final String USERNAME_TAG = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        saveValuesToSharedPrefs();
    }

    public void saveValuesToSharedPrefs(){
        // Setup the editor -> sharedPrefs is read only by default
        SharedPreferences.Editor preferenceEditor = preferences.edit();
        Button saveButton = this.findViewById(R.id.SaveUserNameButton);
        saveButton.setOnClickListener(v -> {
            String usernameText = ((EditText) findViewById(R.id.UserNameText)).getText().toString();

            preferenceEditor.putString(USERNAME_TAG, usernameText);
            preferenceEditor.apply(); // TODO: Nothing saves unless you do this, DONT FORGET!!

            Toast.makeText(this, "Username Saved!", Toast.LENGTH_SHORT).show();
        });
    }
}