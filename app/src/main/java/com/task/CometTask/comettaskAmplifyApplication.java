package com.task.CometTask;

import android.app.Application;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.core.Amplify;

public class comettaskAmplifyApplication extends Application {

    public static String TAG = "comettaskAmplifyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        try{
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());
        } catch (AmplifyException ae) {
            Log.e(TAG, "Error Initializing Amplify: " + ae.getMessage(), ae);
        }
    }

}
