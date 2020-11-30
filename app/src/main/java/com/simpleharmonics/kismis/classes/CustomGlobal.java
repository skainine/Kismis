package com.simpleharmonics.kismis.classes;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.room.Room;

import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.simpleharmonics.kismis.R;
import com.simpleharmonics.kismis.databases.CustomDatabase;

import java.io.File;

public class CustomGlobal extends Application {

    private static final String TAG = "CustomGlobal";
    private static SimpleCache simpleCache;

    public static DocumentReference getUniqueUserReference(Activity activity, String uniqueID) {
        return getFireStoreInstance()
                .collection(activity.getString(R.string.firestore_collection_users))
                .document(uniqueID);
    }

    public static String getUniqueID(Activity activity) {
        FirebaseUser firebaseUser = CustomTools.getFirebaseUser();
        if (firebaseUser == null) {
            return null;
        } else {
            return firebaseUser.getUid();
        }
    }

    public static DocumentReference getUniqueUserReference(Activity activity) {
        FirebaseUser firebaseUser = CustomTools.getFirebaseUser();
        if (firebaseUser == null) {
            return null;
        } else {
            String uniqueID = firebaseUser.getUid();
            return getFireStoreInstance()
                    .collection(activity.getString(R.string.firestore_collection_users))
                    .document(uniqueID);
        }
    }

    public static DocumentReference getUserDetailsReference(Activity activity, String uniqueID) {
        return getFireStoreInstance()
                .collection(activity.getString(R.string.firestore_collection_users))
                .document(uniqueID)
                .collection(activity.getString(R.string.firestore_collection_user))
                .document(activity.getString(R.string.firestore_document_details));
    }

    public static CollectionReference getVideoListReference(Activity activity) {
        return getFireStoreInstance()
                .collection(activity.getString(R.string.firestore_collection_videos));
    }

    public static DocumentReference getUserDetailsReference(Activity activity) {
        FirebaseUser firebaseUser = CustomTools.getFirebaseUser();
        if (firebaseUser == null) {
            return null;
        } else {
            String uniqueID = firebaseUser.getUid();
            return getFireStoreInstance()
                    .collection(activity.getString(R.string.firestore_collection_users))
                    .document(uniqueID)
                    .collection(activity.getString(R.string.firestore_collection_user))
                    .document(activity.getString(R.string.firestore_document_details));
        }
    }

    public static CollectionReference getCommentListReference(Activity activity, String videoID) {
        return getFireStoreInstance()
                .collection(activity.getString(R.string.firestore_collection_videos))
                .document(videoID)
                .collection(activity.getString(R.string.firestore_collection_comments));
    }

    private static FirebaseFirestore getFireStoreInstance() {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings firebaseFirestoreSettings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build();
        firebaseFirestore.setFirestoreSettings(firebaseFirestoreSettings);
        return firebaseFirestore;
    }

    public static SimpleCache getSimpleCache(Context context) {
        if (simpleCache == null) {
            long maxCacheSize = 1024 * 1024 * 1024;
            simpleCache = new SimpleCache(new File(context.getCacheDir(), "media"), new LeastRecentlyUsedCacheEvictor(maxCacheSize), new ExoDatabaseProvider(context));
        }
        return simpleCache;
    }

    public static void openTermsOfUse(Activity activity, String link) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        activity.startActivity(browserIntent);
    }


    public static CustomDatabase getDatabaseAccess(Context context) {
        return Room.databaseBuilder(context, CustomDatabase.class, "Kismis_Database").build();
    }
}
