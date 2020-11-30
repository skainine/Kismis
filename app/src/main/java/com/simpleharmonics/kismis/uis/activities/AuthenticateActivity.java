package com.simpleharmonics.kismis.uis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.simpleharmonics.kismis.R;
import com.simpleharmonics.kismis.classes.CustomFirebase;
import com.simpleharmonics.kismis.classes.CustomGlobal;
import com.simpleharmonics.kismis.classes.CustomPreference;
import com.simpleharmonics.kismis.classes.CustomTools;
import com.simpleharmonics.kismis.interfaces.LoginInterface;

public class AuthenticateActivity extends AppCompatActivity implements LoginInterface {

    private static final String TAG = "AuthenticateActivityTAG";
    private static final int REQUEST_CODE_GOOGLE_LOGIN = 325;
    private ContentLoadingProgressBar pbProgress;
    private LinearLayout llButtons;
    private  final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);

        pbProgress = findViewById(R.id.qrb);
        llButtons = findViewById(R.id.ltw);
        MaterialButton mbtLogin = findViewById(R.id.lok);
        mbtLogin.setOnClickListener(view -> googleLogin());

        MaterialTextView mtvTermsOfUse = findViewById(R.id.ino);
        mtvTermsOfUse.setOnClickListener(view -> {
            CustomGlobal.openTermsOfUse(AuthenticateActivity.this, getString(R.string.terms_of_use_link));
        });

        MaterialTextView mtvLoginLater = findViewById(R.id.ddv);
        Intent intent = AuthenticateActivity.this.getIntent();
        if (intent == null) {
            mtvLoginLater.setVisibility(View.VISIBLE);
            llButtons.setVisibility(View.VISIBLE);
        } else {
            if (intent.getBooleanExtra("Trigger", false)) {
                mtvLoginLater.setVisibility(View.GONE);
                new Handler().postDelayed(this::googleLogin, 200);
            } else {
                mtvLoginLater.setVisibility(View.VISIBLE);
                llButtons.setVisibility(View.VISIBLE);
                mtvLoginLater.setOnClickListener(view -> {
                    CustomPreference.setUserSkippedLogin(AuthenticateActivity.this, true);
                    startActivity(new Intent(AuthenticateActivity.this, LanguageActivity.class));
                    AuthenticateActivity.this.finish();
                });
            }
        }
    }

    private void googleLogin() {
        CustomTools.showToast(AuthenticateActivity.this, "Implement your own Firebase Account!");
//        showProgress();
//        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken("")//.requestIdToken(getString(R.string.default_web_client_id))TODO use your id here
//                .requestEmail()
//                .build();
//
//        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(AuthenticateActivity.this, googleSignInOptions);
//        Intent signInIntent = googleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GOOGLE_LOGIN) {
            if (resultCode == RESULT_OK) {
                Task<GoogleSignInAccount> googleSignInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount googleSignInAccount = googleSignInAccountTask.getResult(ApiException.class);
                    if (googleSignInAccount == null) {
                        Log.e(TAG, "Kismis: onActivityResult: googleSignInAccount is null");
                        hideProgress();
                    } else {
                        firebaseLogin(googleSignInAccount.getIdToken());
                    }
                } catch (ApiException e) {
                    Log.e(TAG, "Kismis: onActivityResult: ApiException: " + e);
                    hideProgress();
                    CustomTools.showToast(AuthenticateActivity.this, "Something went wrong");
                }
            } else {
                hideProgress();
                if (resultCode == 0) {
                    Log.e(TAG, "Kismis: onActivityResult: resultCode 0: User cancelled the Login flow");
                } else {
                    Log.e(TAG, "Kismis: onActivityResult: resultCode Unknown: " + resultCode);
                    CustomTools.showToast(AuthenticateActivity.this, "Something went wrong");
                }
            }
        }
    }

    private void firebaseLogin(String idToken) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(authCredential).addOnSuccessListener(authResult -> {
            FirebaseUser firebaseUser = CustomTools.getFirebaseUser();
            if (firebaseUser == null) {
                Log.e(TAG, "Kismis: firebaseLogin: firebaseUser is null");
                hideProgress();
                CustomTools.showToast(AuthenticateActivity.this, "Something went wrong");
            } else {
                CustomFirebase.checkUserAccount(AuthenticateActivity.this, AuthenticateActivity.this, firebaseUser.getUid());
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Kismis: firebaseLogin: addOnFailureListener: " + e);
            hideProgress();
            CustomTools.showToast(AuthenticateActivity.this, "Something went wrong");
        });
    }

    private void showProgress() {
        llButtons.setVisibility(View.GONE);
        pbProgress.show();
    }

    private void hideProgress() {
        pbProgress.hide();
        llButtons.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoginStart() {
        handler.post(() -> Log.i(TAG, "Kismis: onLoginStart: Checking Account..."));
    }

    @Override
    public void onLoginComplete() {
        handler.post(() -> {
            CustomPreference.setUserSkippedLogin(AuthenticateActivity.this, false);
            startActivity(new Intent(AuthenticateActivity.this, LanguageActivity.class));
            AuthenticateActivity.this.finish();
        });
    }

    @Override
    public void onLoginError() {
        handler.post(() -> {
            hideProgress();
            CustomTools.showToast(AuthenticateActivity.this, "Something went wrong");
        });
    }
}