package com.simpleharmonics.kismis.interfaces;

import com.simpleharmonics.kismis.entities.CustomVideo;

import java.util.List;

public interface VideoTileInterface {

    void onVideoTileClicked(List<CustomVideo> customVideoList, int index);
}
