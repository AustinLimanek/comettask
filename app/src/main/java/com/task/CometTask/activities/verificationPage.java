package com.task.CometTask.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.amplifyframework.core.Amplify;
import com.task.CometTask.R;

public class verificationPage extends AppCompatActivity {
    public static final String TAG = "verifyAccountActivity";
    public static final String VERIFY_ACCOUNT_EMAIL_TAG = "verifyAccountActivity";
    Intent callingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_page);
        callingIntent = getIntent();

        setUpVerifyForm();
    }

    public void setUpVerifyForm(){

        String userEmail = callingIntent.getStringExtra(SignUpActivity.SIGNUP_EMAIL_TAG);

        findViewById(R.id.verifySubmitButton).setOnClickListener(v -> {

            String verificationCode = ((EditText) findViewById(R.id.verifyTextVerificationCode)).getText().toString();

            Amplify.Auth.confirmSignUp(userEmail, verificationCode,
                success -> {Log.i(TAG, "Signup succeded");
                    Intent goToSignIn = new Intent(this, SignInPage.class);
                    goToSignIn.putExtra(VERIFY_ACCOUNT_EMAIL_TAG, userEmail);
                    startActivity(goToSignIn);
                },
                failure-> Log.w(TAG, "signup failed with email")
                );
        });
    }
}