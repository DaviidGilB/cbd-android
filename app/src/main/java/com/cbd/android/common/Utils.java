package com.cbd.android.common;

public class Utils {

    private Utils() {

    }

    public static boolean hasToken() {
        return SharedPreferencesManager.getSomeStringValue(Constants.TOKEN) != null;
    }

    public static String getToken() {
        return SharedPreferencesManager.getSomeStringValue(Constants.TOKEN);
    }

    public static void saveToken(String token) {
        SharedPreferencesManager.setSomeStringValue(Constants.TOKEN, token);
    }

    public static void deleteToken() {
        saveToken(null);
    }

}
