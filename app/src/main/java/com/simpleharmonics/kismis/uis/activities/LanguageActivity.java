package com.simpleharmonics.kismis.uis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.simpleharmonics.kismis.R;
import com.simpleharmonics.kismis.adapters.LanguageTileAdapter;
import com.simpleharmonics.kismis.classes.CustomPreference;
import com.simpleharmonics.kismis.interfaces.LanguageTileInterface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LanguageActivity extends AppCompatActivity implements LanguageTileInterface {

    private static final String TAG = "LanguageActivity";

    private RecyclerView rvLanguages;
    private LanguageTileAdapter languageTileAdapter;
    private List<LanguageTileAdapter.CustomLanguage> customLanguageList = new ArrayList<>();
    private List<Integer> selectedIndexIntegerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        MaterialButton mtbNext = findViewById(R.id.zpq);
        mtbNext.setOnClickListener(v -> {
            if (selectedIndexIntegerList.isEmpty()) {
                Log.e(TAG, "Kismis: onCreate: No language is selected");
            } else {
                Set<String> languageSet = new HashSet<>();
                for (int index : selectedIndexIntegerList) {
                    languageSet.add(customLanguageList.get(index).getLanguage());
                }
                CustomPreference.setLanguageSet(LanguageActivity.this, languageSet);
            }
            startActivity(new Intent(LanguageActivity.this, MainActivity.class));
            LanguageActivity.this.finish();
        });

        rvLanguages = findViewById(R.id.plu);
        rvLanguages.setLayoutManager(new GridLayoutManager(LanguageActivity.this, 2));
        rvLanguages.setHasFixedSize(true);

        languageTileAdapter = new LanguageTileAdapter(LanguageActivity.this, LanguageActivity.this, customLanguageList);
        rvLanguages.setAdapter(languageTileAdapter);

        getLanguages();
    }

    private void getLanguages() {
        customLanguageList = new ArrayList<>();
        String[] languageArray = getResources().getStringArray(R.array.languages);

        for (String language : languageArray) {
            LanguageTileAdapter.CustomLanguage customLanguage = new LanguageTileAdapter.CustomLanguage(language);
            customLanguageList.add(customLanguage);
        }
        languageTileAdapter = new LanguageTileAdapter(LanguageActivity.this, LanguageActivity.this, customLanguageList);
        rvLanguages.setAdapter(languageTileAdapter);
    }

    @Override
    public void onLanguageTileClicked(List<String> selectedIndexList) {
        selectedIndexIntegerList = new ArrayList<>();
        for (String index : selectedIndexList) {
            selectedIndexIntegerList.add(Integer.parseInt(index));
        }
    }
}