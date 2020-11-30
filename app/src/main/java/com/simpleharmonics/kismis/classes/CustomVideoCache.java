package com.simpleharmonics.kismis.classes;

import android.content.Context;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSink;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;

public class CustomVideoCache implements DataSource.Factory {

    private static final String PLAYER_TAG = "Kismis Player";
    private static final long maxCachePerVideo = 1024 * 1024 * 1024;
    private final Context context;
    private final DefaultDataSourceFactory defaultDatasourceFactory;

    public CustomVideoCache(Context context) {
        super();
        this.context = context;
        String userAgent = Util.getUserAgent(context, PLAYER_TAG);
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter.Builder(context).build();
        defaultDatasourceFactory = new DefaultDataSourceFactory(this.context, defaultBandwidthMeter, new DefaultHttpDataSourceFactory(userAgent, defaultBandwidthMeter));
    }

    @Override
    public DataSource createDataSource() {
        SimpleCache simpleCache = CustomGlobal.getSimpleCache(context);
        return new CacheDataSource(simpleCache, defaultDatasourceFactory.createDataSource(), new FileDataSource(), new CacheDataSink(simpleCache, maxCachePerVideo), CacheDataSource.FLAG_BLOCK_ON_CACHE | CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR, null);
    }
}