package com.simpleharmonics.kismis.uis.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseUser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener;
import com.simpleharmonics.kismis.R;
import com.simpleharmonics.kismis.classes.CustomGlobal;
import com.simpleharmonics.kismis.classes.CustomTools;
import com.simpleharmonics.kismis.uis.fragments.FragmentChat;
import com.simpleharmonics.kismis.uis.fragments.FragmentExplore;
import com.simpleharmonics.kismis.uis.fragments.FragmentHome;
import com.simpleharmonics.kismis.uis.fragments.FragmentProfile;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private final String fragmentHomeTAG = "fragmentHomeTAG";
    private final String fragmentSearchTAG = "fragmentSearchTAG";
    private final String fragmentChatTAG = "fragmentChatTAG";
    private final String fragmentProfileTAG = "fragmentProfileTAG";

    private FragmentHome fragmentHome;
    private FragmentExplore fragmentExplore;
    private FragmentChat fragmentChat;
    private FragmentProfile fragmentProfile;

    private ConstraintLayout clBottomBar;
    private String currentFragmentTAG = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlack, null));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorBlack, null));
        overridePendingTransition(R.anim.slide_up, R.anim.stay);
        setContentView(R.layout.activity_main);

        clBottomBar = findViewById(R.id.mja);
        clBottomBar.setOnClickListener(view -> Log.i(TAG, "Kismis: onCreate: clBottomBar tapped"));

        currentFragmentTAG = fragmentHomeTAG;
        fragmentHome = new FragmentHome();
        MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.znw, fragmentHome, fragmentHomeTAG).commit();

        final ImageButton ibHome = findViewById(R.id.naf);
        ibHome.setOnClickListener(view -> {
            if (currentFragmentTAG.equals(fragmentHomeTAG)) {
                Log.i(TAG, "Kismis: onCreate: Fragment already active");
            } else {
                openHomeFragment();
            }
        });

        final ImageButton ibSearch = findViewById(R.id.kpa);
        ibSearch.setOnClickListener(view -> {
            currentFragmentTAG = fragmentSearchTAG;
            fragmentExplore = new FragmentExplore();
            getSupportFragmentManager().beginTransaction().replace(R.id.znw, fragmentExplore, fragmentSearchTAG).commit();
            clBottomBar.setBackground(getDrawable(R.drawable.drawable_bottom_bar_dark));
        });

        ImageButton ibCreateVideo = findViewById(R.id.qao);
        ibCreateVideo.setOnClickListener(view -> {
            MultiplePermissionsListener permissionDeniedSnack =
                    SnackbarOnAnyDeniedMultiplePermissionsListener.Builder
                            .with(view, "Camera cannot work without those permissions.")
                            .withOpenSettingsButton("Settings")
                            .build();
            MultiplePermissionsListener multiplePermissionsListener = new CompositeMultiplePermissionsListener(permissionDeniedSnack, new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        startActivity(new Intent(MainActivity.this, CameraActivity.class));
                    }
                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                }
            });

            Dexter.withContext(MainActivity.this)
                    .withPermissions(
                            Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ).withListener(multiplePermissionsListener).check();
        });

        final ImageButton ibChat = findViewById(R.id.psp);
        ibChat.setOnClickListener(view -> {
            FirebaseUser currentUser = CustomTools.getFirebaseUser();
            if (currentUser != null) {//TODO make it == instead of !=
                showLoginPrompt();
            } else {
                currentFragmentTAG = fragmentChatTAG;
                fragmentChat = new FragmentChat();
                getSupportFragmentManager().beginTransaction().replace(R.id.znw, fragmentChat, fragmentChatTAG).commit();
                clBottomBar.setBackground(getDrawable(R.drawable.drawable_bottom_bar_dark));
            }
        });

        final ImageButton ibUserProfile = findViewById(R.id.lkf);
        ibUserProfile.setOnClickListener(view -> {
            FirebaseUser currentUser = CustomTools.getFirebaseUser();
            if (currentUser != null) {//TODO make it == instead of !=
                showLoginPrompt();
            } else {
                currentFragmentTAG = fragmentProfileTAG;
                fragmentProfile = new FragmentProfile();
                getSupportFragmentManager().beginTransaction().replace(R.id.znw, fragmentProfile, fragmentProfileTAG).commit();
                clBottomBar.setBackground(getDrawable(R.drawable.drawable_bottom_bar_dark));
            }
        });

//        File fileInput = new File(Environment.getExternalStorageDirectory() + "/video.mp4");
//        File fleOutput = new File(Environment.getExternalStorageDirectory() + "/video_output.avi");
//
//        DefaultAudioStrategy audioStrategy = DefaultAudioStrategy.builder()
//                .build();
//
//        DefaultVideoStrategy videoStrategy = new DefaultVideoStrategy.Builder()
//                .addResizer(new AspectRatioResizer(16F / 9F))
//                .addResizer(new FractionResizer(0.4F))
//                .addResizer(new AtMostResizer(1000))
//                .frameRate(60)
//                .build();
//
//
//        DataSource dataSourceLow = new UriDataSource(MainActivity.this, Uri.fromFile(fileInput));
////        DataSource clippedSource = new ClipDataSource(dataSourceLow, 0, 15 * 1000 * 1000);
//        Transcoder.into(fleOutput.getAbsolutePath())
//                .setAudioTrackStrategy(audioStrategy)
//                .setVideoTrackStrategy(videoStrategy)
//                .addDataSource(dataSourceLow)
//                .setListener(new TranscoderListener() {
//                    public void onTranscodeProgress(double progress) {
//                        Log.e(TAG, "PROGRESS " + progress);
//                    }
//
//                    public void onTranscodeCompleted(int successCode) {
//                        Log.e(TAG, "DONE");
//                    }
//
//                    public void onTranscodeCanceled() {
//                    }
//
//                    public void onTranscodeFailed(@NonNull Throwable exception) {
//                        Log.e(TAG, "ERROR: " + exception);
//                    }
//                }).transcode();
    }

    private void openHomeFragment() {
        currentFragmentTAG = fragmentHomeTAG;
        fragmentHome = new FragmentHome();
        getSupportFragmentManager().beginTransaction().replace(R.id.znw, fragmentHome, fragmentHomeTAG).commit();
        clBottomBar.setBackground(getDrawable(R.drawable.drawable_bottom_bar_light));
    }

    private void showLoginPrompt() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        bottomSheetDialog.setContentView(R.layout.layout_bottom_sheet_authentication);
        bottomSheetDialog.setCanceledOnTouchOutside(true);

        final MaterialButton mbtLogin = bottomSheetDialog.findViewById(R.id.tch);
        final MaterialTextView mtvTermsOfService = bottomSheetDialog.findViewById(R.id.xiu);
        if (mbtLogin == null || mtvTermsOfService == null) {
            CustomTools.showToast(MainActivity.this, "Something went wrong");
        } else {
            mbtLogin.setOnClickListener(view -> {
                bottomSheetDialog.cancel();
                Intent intent = new Intent(MainActivity.this, AuthenticateActivity.class);
                intent.putExtra("Trigger", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(new Intent(intent));
            });

            mtvTermsOfService.setOnClickListener(view -> CustomGlobal.openTermsOfUse(MainActivity.this, getString(R.string.terms_of_use_link)));
            bottomSheetDialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        if (currentFragmentTAG == null) {
            super.onBackPressed();
        } else {
            if (currentFragmentTAG.equals(fragmentHomeTAG)) {
                super.onBackPressed();
            } else {
                openHomeFragment();
            }
        }
    }
}