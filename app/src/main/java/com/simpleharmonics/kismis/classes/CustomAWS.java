package com.simpleharmonics.kismis.classes;

import android.app.Activity;

import com.simpleharmonics.kismis.interfaces.LoginInterface;

/**
 * Use this for AWS implementation
 **/
public class CustomAWS {

    private static final String TAG = "CustomAWSTAG";

    //    public static void uploadFile(String fileKey, File targetFile) {
//        StorageUploadFileOptions options =
//                StorageUploadFileOptions.builder()
//                        .accessLevel(StorageAccessLevel.PROTECTED)
//                        .build();
//
//        Amplify.Storage.uploadFile(
//                fileKey,
//                targetFile,
//                options,
//                result -> Log.i(TAG, "Kismis: uploadFile: result: " + fileKey),
//                error -> Log.e(TAG, "Kismis: uploadFile: error: ", error)
//        );
//    }
//
    public static void awsCreateProfile(Activity activity, LoginInterface listener, String userName, String password) {
//        Amplify.Auth.signUp(
//                userName,
//                password,
//                AuthSignUpOptions.builder().userAttribute(AuthUserAttributeKey.email(), CustomTools.getKaymraEmail(userName)).build(),
//                result -> {
//                    if (result.isSignUpComplete()) {
//                        CustomPreference.setUserSkippedLogin(activity, false);
//                        listener.onLoginComplete();
//                    } else {
//                        Log.e(TAG, "Kismis: awsCreateProfile: result: " + result);
//                        listener.onLoginError();
//                    }
//                },
//                error -> {
//                    Log.e(TAG, "Kismis: awsCreateProfile: error: ", error);
//                    listener.onLoginError();
//                }
//        );
    }

    //
    public static boolean userNotLoggedIn() {
        return false;
//        return Amplify.Auth.getCurrentUser() == null;
    }

    //
    public static void awsLoginProfile(Activity activity, LoginInterface listener, String userName, String password) {
//        Amplify.Auth.signIn(
//                userName,
//                password,
//                result -> {
//                    if (result.isSignInComplete()) {
//                        CustomPreference.setUserSkippedLogin(activity, false);
//                        listener.onLoginComplete();
//                    } else {
//                        Log.e(TAG, "Kismis: awsLoginProfile: result: " + result);
//                        listener.onLoginError();
//                    }
//                },
//                error -> {
//                    Log.e(TAG, "Kismis: awsLoginProfile: error: ", error);
//                    if (error.getCause() instanceof UserNotFoundException) {
//                        awsCreateProfile(activity, listener, userName, password);
//                    } else {
//                        listener.onLoginError();
//                    }
//                }
//        );
    }
//
//    private void getUserVideosList(String userID) {
//        StorageListOptions options = StorageListOptions.builder()
//                .accessLevel(StorageAccessLevel.PROTECTED)
//                .targetIdentityId(userID)
//                .build();
//
//        Amplify.Storage.list(
//                "/videos",
//                options,
//                result -> {
//                    for (StorageItem item : result.getItems()) {
//                        Log.i(TAG, "Kismis: getUserVideosList: Item: " + item.getKey());
//                    }
//                },
//                error -> Log.e(TAG, "Kismis: getUserVideosList: List failure: ", error)
//        );
//    }
//
//    private void deleteUserVideo(String fileKey) {
//        Amplify.Storage.remove(
//                fileKey,
//                result -> Log.i(TAG, "Kismis: deleteUserVideo: Successfully removed: " + result.getKey()),
//                error -> Log.e(TAG, "Kismis: deleteUserVideo: Remove failure", error)
//        );
//    }
}
