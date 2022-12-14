package com.task.CometTask.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;
import com.task.CometTask.R;

public class SignInPage extends AppCompatActivity {
    public static final String TAG = "signInActivity";
    Intent callingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_page);
        callingIntent = getIntent();

        setUpSignInForm();
    }

    public void setUpSignInForm(){
        String userEmail = callingIntent.getStringExtra(verificationPage.VERIFY_ACCOUNT_EMAIL_TAG);
        TextView userEmailTextView = findViewById(R.id.signInTextEmail);
        userEmailTextView.setText(userEmail);

        findViewById(R.id.signInButton).setOnClickListener(v -> {

            String finalEmail = ((EditText) userEmailTextView).getText().toString();
            String userPassword = ((EditText) findViewById(R.id.signInTextPassword)).getText().toString();


            Amplify.Auth.signIn(
                    finalEmail,
                    userPassword,
                    success -> {Log.i(TAG, "Sign in successful");
                        startActivity(new Intent(this, MainActivity.class));
                        },
                    failure -> Log.w(TAG, failure.toString())
            );
        });
    }

}