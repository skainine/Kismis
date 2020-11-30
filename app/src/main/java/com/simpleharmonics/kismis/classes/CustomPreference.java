package com.simpleharmonics.kismis.classes;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class CustomPreference {

    private static final String MY_APP_PREFERENCE = "com.kaymra.kaymra.sfs";
    private static final String SHOW_FLEX_ACTIVITY = "SHOW_FLEX_ACTIVITY";
    private static final String SHOW_LANGUAGE_ACTIVITY = "SHOW_LANGUAGE_ACTIVITY";
    private static final String LANGUAGE_LIST = "LANGUAGE_LIST";
    private static final String USER_SKIPPED_LOGIN = "USER_SKIPPED_LOGIN";
    private static final String USER_ID = "USER_ID";

    public static Set<String> getLanguageSet(Context context) {
        return getSharedPreferences(context).getStringSet(LANGUAGE_LIST, null);
    }

    public static void setLanguageSet(Context context, Set<String> languageSet) {
        getSharedPreferences(context).edit().putStringSet(LANGUAGE_LIST, languageSet).apply();
    }

    public static boolean getShowFlexActivity(Context context) {
        return getSharedPreferences(context).getBoolean(SHOW_FLEX_ACTIVITY, true);
    }

    public static void setShowFlexActivity(Context context, boolean toShow) {
        getSharedPreferences(context).edit().putBoolean(SHOW_FLEX_ACTIVITY, toShow).apply();
    }

    public static boolean getUserSkippedLogin(Context context) {
        return getSharedPreferences(context).getBoolean(USER_SKIPPED_LOGIN, false);
    }

    public static void setUserSkippedLogin(Context context, boolean skipped) {
        getSharedPreferences(context).edit().putBoolean(USER_SKIPPED_LOGIN, skipped).apply();
    }

    public static String getUserID(Context context) {
        return getSharedPreferences(context).getString(USER_ID, null);
    }

    public static void setUserID(Context context, String userID) {
        getSharedPreferences(context).edit().putString(USER_ID, userID).apply();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(MY_APP_PREFERENCE, Context.MODE_PRIVATE);
    }

    public static void cleanUp(Context context) {
        getSharedPreferences(context).edit().clear().apply();
    }
}
