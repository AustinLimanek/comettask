package com.task.CometTask;

import android.app.Application;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

public class comettaskAmplifyApplication extends Application {

    public static String TAG = "comettaskAmplifyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        try{
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());
            Amplify.configure(getApplicationContext());
        } catch (AmplifyException ae) {
            Log.e(TAG, "Error Initializing Amplify: " + ae.getMessage(), ae);
        }
    }

}
