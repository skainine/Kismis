package com.simpleharmonics.kismiscamera;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CustomTools {

    private static final String TAG = "CustomTools";

    public static File getVideoFile(Context context) {
        try {
            File externalFile = context.getExternalFilesDir(null);
            if (externalFile == null) {
                Log.e(TAG, "Kismis: getVideoFile: externalFile is null");
                return null;
            } else {
                File videoFolder = new File(externalFile.getAbsolutePath() + "/ShotVideos");
                Log.i(TAG, "Kismis: getVideoFile: Folder created: " + videoFolder.mkdir());
                File videoFile = new File(videoFolder, "kaymra_video_" + getCurrentTime() + ".mp4");
                Log.i(TAG, "Kismis: getVideoFile: File created: " + videoFile.createNewFile());
                return videoFile;
            }
        } catch (IOException e) {
            Log.e(TAG, "Kismis: getVideoFilePath: IOException: " + e);
            return null;
        }
    }

    private static String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.ENGLISH);
        return simpleDateFormat.format(new Date());
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
