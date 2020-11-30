package com.simpleharmonics.kismis.interfaces;

import com.simpleharmonics.kismis.entities.CustomVideo;

import java.util.List;

public interface RefreshRecommendationsInterface {

    void onRefreshRecommendationsStart();

    void onRefreshRecommendationsComplete(List<CustomVideo> customVideoList);

    void onRefreshRecommendationsAlreadyUpdated();

    void onRefreshRecommendationsError();
}
