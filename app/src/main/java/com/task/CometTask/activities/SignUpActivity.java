package com.task.CometTask.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.task.CometTask.R;

public class SignUpActivity extends AppCompatActivity {
    public static final String TAG = "signupActivityTag";
    public static final String SIGNUP_EMAIL_TAG = "EMAIL";
    SharedPreferences preferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setUpSignUpForm();
    }


    public void setUpSignUpForm() {

        findViewById(R.id.signUpButton).setOnClickListener(v -> {

            String userEmail = ((EditText) findViewById(R.id.signUpTextEmail)).getText().toString();
            String userPassword = ((EditText) findViewById(R.id.signUpTextPassword)).getText().toString();
            String userName = ((EditText) findViewById(R.id.signUpTextUsername)).getText().toString();

            Amplify.Auth.signUp(
                    userEmail,
                    userPassword,
                    AuthSignUpOptions.builder()
                            .userAttribute(AuthUserAttributeKey.email(), userEmail)
                            .userAttribute(AuthUserAttributeKey.preferredUsername(), userName)
                            .build(),
                    success -> {Log.i(TAG, success.toString());
                        Intent goToVerification = new Intent(this, verificationPage.class);
                        goToVerification.putExtra(SIGNUP_EMAIL_TAG, userEmail);
                        startActivity(goToVerification);
                        },
                    failure-> {Log.w(TAG, "signup failed with email: " + userEmail + " With message: "+ failure);
                    runOnUiThread(() -> Toast.makeText(this, "SignUp Failed", Toast.LENGTH_SHORT).show());
                    }
            );
        });

    }
}