package com.simpleharmonics.kismis.classes;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.util.Hex;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class CustomTools {

    private static final String TAG = "CustomToolsTAG";
    private static final String numericString = "0123456789";
    private static final String alphaNumericString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWZYZ0123456789";

    public static String getReducedNumber(long videoLikesLong) {
        float videoLikes = (float) videoLikesLong;
        float oneMillion = 1000000;
        float oneThousand = 1000;
        if (videoLikes >= oneMillion) {
            float likes = videoLikes / oneMillion;
            return String.format(Locale.ENGLISH, "%.1f", likes).concat("k");
        } else if (videoLikes >= oneThousand) {
            float likes = videoLikes / oneThousand;
            return String.format(Locale.ENGLISH, "%.1f", likes).concat("k");
        } else {
            return String.valueOf(videoLikes);
        }
    }

    public static void showToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    private static String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.ENGLISH);
        return simpleDateFormat.format(new Date());
    }

    static String getSHA256(String inputString) {
        String output = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashByteArray = messageDigest.digest(inputString.getBytes(StandardCharsets.UTF_8));
            output = Hex.bytesToStringLowercase(hashByteArray);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static String convertToEasyFormat(String inputDate) {
        try {
            DateFormat originalFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("dd MMMM, yyyy hh:mm a", Locale.ENGLISH);
            Date date = originalFormat.parse(inputDate);
            if (date == null) {
                return null;
            } else {
                return targetFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

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

    public static File getVideoFileTranscoded(Context context, String videoUriString) {
        try {
            String fileName = videoUriString.substring(videoUriString.lastIndexOf("/"));
            fileName = fileName.concat("_transcoded.mp4");

            File externalFile = context.getExternalFilesDir(null);
            if (externalFile == null) {
                Log.e(TAG, "Kismis: getVideoFileTranscoded: externalFile is null");
                return null;
            } else {
                File videoFolder = new File(externalFile.getAbsolutePath() + "/ShotVideos");
                Log.i(TAG, "Kismis: getVideoFileTranscoded: Folder created: " + videoFolder.mkdir());
                File videoFile = new File(videoFolder, fileName);
                Log.i(TAG, "Kismis: getVideoFileTranscoded: File created: " + videoFile.createNewFile());
                return videoFile;
            }
        } catch (IOException e) {
            Log.e(TAG, "Kismis: getVideoFileTranscoded: IOException: " + e);
            return null;
        }
    }

    public static String getRandomStringNumeric(int size) {
        StringBuilder stringBuilder = new StringBuilder(size);
        int length = numericString.length();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            stringBuilder.append(numericString.charAt(random.nextInt(length)));
        }
        return String.valueOf(stringBuilder);
    }

    public static String getRandomStringAlphaNumeric(int size) {
        StringBuilder stringBuilder = new StringBuilder(size);
        int length = alphaNumericString.length();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            stringBuilder.append(alphaNumericString.charAt(random.nextInt(length)));
        }
        return String.valueOf(stringBuilder);
    }

    public static String getKaymraEmail(String userName) {
        return userName.concat("@kaymra.com");
    }

    public static FirebaseUser getFirebaseUser() {
        return null;
//       return FirebaseAuth.getInstance().getCurrentUser();//TODO change this
    }
}
