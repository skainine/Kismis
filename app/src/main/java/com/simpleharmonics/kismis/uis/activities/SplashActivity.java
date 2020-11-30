package com.simpleharmonics.kismis.uis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import com.amplifyframework.AmplifyException;
//import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
//import com.amplifyframework.core.Amplify;
//import com.amplifyframework.storage.s3.AWSS3StoragePlugin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.simpleharmonics.kismis.R;
import com.simpleharmonics.kismis.classes.CustomFirebase;
import com.simpleharmonics.kismis.classes.CustomPreference;
import com.simpleharmonics.kismis.classes.CustomTools;
import com.simpleharmonics.kismis.interfaces.LoginInterface;

public class SplashActivity extends AppCompatActivity implements LoginInterface {

    private static final String TAG = "SplashActivityTAG";
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Thread(this::initialize).start();
    }

    private void initialize() {
        //TODO If you are using AWS uncomment this
//        try {
//            Amplify.addPlugin(new AWSS3StoragePlugin());
//            Amplify.addPlugin(new AWSCognitoAuthPlugin());
//            Amplify.configure(getApplicationContext());
//        } catch (AmplifyException error) {
//            Log.e(TAG, "Kismis: onCreate: Could not initialize Amplify: ", error);
//        }

        FirebaseUser currentUser = CustomTools.getFirebaseUser();
        if (CustomPreference.getShowFlexActivity(SplashActivity.this)) {
            handler.postDelayed(() -> {
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, FlexActivity.class));
                SplashActivity.this.finish();
            }, 400);
        } else if (currentUser == null) {
            handler.postDelayed(() -> {
                if (CustomPreference.getUserSkippedLogin(SplashActivity.this)) {
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, AuthenticateActivity.class));
                }
                SplashActivity.this.finish();
            }, 400);
        } else {
            if (CustomPreference.getUserSkippedLogin(SplashActivity.this)) {
                handler.postDelayed(() -> SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class)), 400);
            } else {
                CustomFirebase.checkUserAccount(SplashActivity.this, SplashActivity.this, currentUser.getUid());
            }
        }
    }

    @Override
    public void onLoginStart() {
        handler.post(() -> Log.i(TAG, "Kismis: onLoginStart: Login Started"));
    }

    @Override
    public void onLoginComplete() {
        handler.post(() -> {
            SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
            SplashActivity.this.finish();
        });
    }

    @Override
    public void onLoginError() {
        handler.post(() -> Toast.makeText(SplashActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show());
    }
}