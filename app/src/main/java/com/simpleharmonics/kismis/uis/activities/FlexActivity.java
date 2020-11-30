package com.simpleharmonics.kismis.uis.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.github.appintro.AppIntro2;
import com.github.appintro.AppIntroFragment;
import com.github.appintro.AppIntroPageTransformerType;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.simpleharmonics.kismis.R;
import com.simpleharmonics.kismis.classes.CustomPreference;
import com.simpleharmonics.kismis.classes.CustomTools;

public class FlexActivity extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance("Welcome", "Hello There", R.drawable.ic_appintro_done));
        addSlide(AppIntroFragment.newInstance("Welcome", "Hello There", R.drawable.ic_appintro_arrow));
        addSlide(AppIntroFragment.newInstance("Welcome", "Hello There", R.drawable.ic_like_primary_24));

        setImmersive(true);
        setWizardMode(true);
        setColorTransitionsEnabled(true);
        setStatusBarColor(getColor(R.color.colorBlack));
        setBackgroundDrawable(getDrawable(R.drawable.drawable_black_background));
        setNavBarColor(getColor(R.color.colorBlack));

        AppIntroPageTransformerType appIntroPageTransformerType = new AppIntroPageTransformerType.Parallax();
        setTransformer(appIntroPageTransformerType);
    }

    private void proceedFurther() {
        CustomPreference.setShowFlexActivity(FlexActivity.this, false);
        FirebaseUser currentUser = CustomTools.getFirebaseUser();
        if (currentUser == null) {
            FlexActivity.this.startActivity(new Intent(FlexActivity.this, AuthenticateActivity.class));
        } else {
            FlexActivity.this.startActivity(new Intent(FlexActivity.this, SplashActivity.class));
        }
        FlexActivity.this.finish();
    }

    @Override
    protected void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        proceedFurther();
    }

    @Override
    protected void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        proceedFurther();
    }
}

