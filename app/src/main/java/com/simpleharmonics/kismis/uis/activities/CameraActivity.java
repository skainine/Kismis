package com.simpleharmonics.kismis.uis.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.simpleharmonics.kismis.R;
import com.simpleharmonics.kismiscamera.KismisCamera;

public class CameraActivity extends AppCompatActivity {

    private KismisCamera kismisCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlack, null));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorBlack, null));
        overridePendingTransition(R.anim.slide_up, R.anim.stay);
        setContentView(R.layout.activity_camera);

        kismisCamera = findViewById(R.id.oqc);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }

    @Override
    protected void onStart() {
        super.onStart();
        kismisCamera.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        kismisCamera.onResume();
    }

    @Override
    protected void onPause() {
        kismisCamera.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        kismisCamera.onStop();
        super.onStop();
    }
}