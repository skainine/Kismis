package com.simpleharmonics.kismis.uis.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.simpleharmonics.kismis.R;
import com.simpleharmonics.kismis.adapters.ProfileTileAdapter;
import com.simpleharmonics.kismis.interfaces.ProfileTileInterface;

import java.util.ArrayList;
import java.util.List;

public class FragmentExplore extends Fragment implements ProfileTileInterface {

    private static final String TAG = "FragmentSearchTAG";
    private RelativeLayout rlSearch;
    private ImageButton ibCloseSearch;
    private RecyclerView rvSearchProfileTiles, rvRecommendedProfileTiles;
    private ProfileTileAdapter searchProfileTileAdapter, recommendedProfileTileAdapter;
    private ContentLoadingProgressBar pbProgressSearch, pbProgressRecommendations;
    private List<ProfileTileAdapter.CustomProfileTile> customSearchProfileTileList = new ArrayList<>();
    private List<ProfileTileAdapter.CustomProfileTile> customRecommendedProfileTileList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_explore, container, false);

        FragmentActivity fragmentActivity = FragmentExplore.this.getActivity();
        if (fragmentActivity == null) {
            Log.e(TAG, "Kismis: onCreateView: fragmentActivity is null");
        } else {
            final Toolbar toolbar = fragment.findViewById(R.id.rcl);
            ((AppCompatActivity) fragmentActivity).setSupportActionBar(toolbar);
            ActionBar actionBar = ((AppCompatActivity) fragmentActivity).getSupportActionBar();
            if (actionBar == null) {
                Log.e(TAG, "Kismis: onCreate: actionBar is null");
            } else {
                actionBar.setTitle("Explore");
            }

            setHasOptionsMenu(true);

            final MaterialTextView mtvRecommendations = fragment.findViewById(R.id.vjv);

            rlSearch = fragment.findViewById(R.id.yth);
            ibCloseSearch = fragment.findViewById(R.id.ssz);

            rvSearchProfileTiles = fragment.findViewById(R.id.cee);
            rvSearchProfileTiles.setLayoutManager(new GridLayoutManager(fragmentActivity, 2));
            rvSearchProfileTiles.setHasFixedSize(true);
            pbProgressSearch = fragment.findViewById(R.id.zlo);

            rvRecommendedProfileTiles = fragment.findViewById(R.id.wtb);
            rvRecommendedProfileTiles.setLayoutManager(new GridLayoutManager(fragmentActivity, 2));
            rvRecommendedProfileTiles.setHasFixedSize(true);
            pbProgressRecommendations = fragment.findViewById(R.id.uzx);

            searchProfileTileAdapter = new ProfileTileAdapter(fragmentActivity, FragmentExplore.this, customSearchProfileTileList);
            rvSearchProfileTiles.setAdapter(searchProfileTileAdapter);

            recommendedProfileTileAdapter = new ProfileTileAdapter(fragmentActivity, FragmentExplore.this, customRecommendedProfileTileList);
            rvRecommendedProfileTiles.setAdapter(recommendedProfileTileAdapter);

            new Handler().postDelayed(() -> {
                mtvRecommendations.setVisibility(View.VISIBLE);
                getRecommendations();
            }, 200);
        }
        return fragment;
    }

    private void getRecommendations() {
        FragmentActivity fragmentActivity = FragmentExplore.this.getActivity();
        if (fragmentActivity == null) {
            Log.e(TAG, "Kismis: getRecommendations: fragmentActivity is null");
        } else {
            new Handler().postDelayed(() -> {
                pbProgressRecommendations.hide();
                customRecommendedProfileTileList = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    ProfileTileAdapter.CustomProfileTile customProfileTile = new ProfileTileAdapter.CustomProfileTile("", "Kiki", "@noob", "", false, 24198, 2352, 235);
                    customRecommendedProfileTileList.add(customProfileTile);
                }
                recommendedProfileTileAdapter = new ProfileTileAdapter(fragmentActivity, FragmentExplore.this, customRecommendedProfileTileList);
                rvRecommendedProfileTiles.setAdapter(recommendedProfileTileAdapter);
            }, 500);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        FragmentActivity fragmentActivity = FragmentExplore.this.getActivity();
        if (fragmentActivity == null) {
            Log.e(TAG, "Kismis: onCreateOptionsMenu: fragmentActivity is null");
        } else {
            fragmentActivity.getMenuInflater().inflate(R.menu.menu_search, menu);
            final MenuItem menuItem = menu.findItem(R.id.sfe);

            SearchView searchView = (SearchView) menuItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    search(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
            ibCloseSearch.setOnClickListener(v -> {
                rlSearch.setVisibility(View.GONE);
                menuItem.collapseActionView();
            });
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void search(String searchQuery) {
        FragmentActivity fragmentActivity = FragmentExplore.this.getActivity();
        if (fragmentActivity == null) {
            Log.e(TAG, "Kismis: getRecommendations: fragmentActivity is null");
        } else {
            resetSearchLayout(fragmentActivity);
            new Handler().postDelayed(() -> {
                pbProgressSearch.hide();
                customSearchProfileTileList = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    ProfileTileAdapter.CustomProfileTile customProfileTile = new ProfileTileAdapter.CustomProfileTile("", "Kiki", "@noob", "", i % 3 == 0, 24198, 2352, 235);
                    customSearchProfileTileList.add(customProfileTile);
                }
                searchProfileTileAdapter = new ProfileTileAdapter(fragmentActivity, FragmentExplore.this, customSearchProfileTileList);
                rvSearchProfileTiles.setAdapter(searchProfileTileAdapter);
            }, 800);
        }
    }

    private void resetSearchLayout(FragmentActivity fragmentActivity) {
        customSearchProfileTileList = new ArrayList<>();
        searchProfileTileAdapter = new ProfileTileAdapter(fragmentActivity, FragmentExplore.this, customSearchProfileTileList);
        rvSearchProfileTiles.setAdapter(searchProfileTileAdapter);
        pbProgressSearch.show();
        rlSearch.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProfileTileClicked(ProfileTileAdapter.CustomProfileTile customProfileTile) {

    }
}
