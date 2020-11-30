package com.simpleharmonics.kismis.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.simpleharmonics.kismis.R;
import com.simpleharmonics.kismis.interfaces.LanguageTileInterface;

import java.util.ArrayList;
import java.util.List;

public class LanguageTileAdapter extends RecyclerView.Adapter<LanguageTileAdapter.CustomViewHolder> {

    private final Context context;
    private final LanguageTileInterface listener;
    private final List<CustomLanguage> customLanguageList;
    private final List<String> selectedIndexList = new ArrayList<>();

    public LanguageTileAdapter(Context context, LanguageTileInterface listener, List<CustomLanguage> customLanguageList) {
        this.context = context;
        this.listener = listener;
        this.customLanguageList = customLanguageList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_language_tile, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, int position) {
        CustomLanguage customLanguage = customLanguageList.get(position);
        MaterialTextView mtvLanguage = customViewHolder.itemView.findViewById(R.id.zdy);
        if (customLanguage.getLanguage() == null || customLanguage.getLanguage().isEmpty()) {
            mtvLanguage.setText(context.getText(R.string.unknown));
        } else {
            mtvLanguage.setText(customLanguage.getLanguage());
        }

        MaterialCardView materialCardView = (MaterialCardView) customViewHolder.itemView;

        if (selectedIndexList.contains(String.valueOf(position))) {
            selectCard(materialCardView);
        } else {
            deSelectCard(materialCardView);
        }

        materialCardView.setOnClickListener(v -> {
            if (selectedIndexList.contains(String.valueOf(position))) {
                deSelectCard(materialCardView);
                selectedIndexList.remove(String.valueOf(position));
            } else {
                selectCard(materialCardView);
                selectedIndexList.add(String.valueOf(position));
            }
            listener.onLanguageTileClicked(selectedIndexList);
        });
    }

    private void deSelectCard(MaterialCardView materialCardView) {
        materialCardView.setStrokeWidth(0);
    }

    private void selectCard(MaterialCardView materialCardView) {
        materialCardView.setStrokeWidth(5);
    }

    @Override
    public int getItemCount() {
        return customLanguageList.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public static class CustomLanguage {

        private final String language;

        public CustomLanguage(String language) {
            this.language = language;
        }

        public String getLanguage() {
            return language;
        }
    }
}
