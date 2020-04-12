package com.cbd.android.common;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private SharedPreferencesManager() {

    }

    private static SharedPreferences getSharedPreferences() {
        return MyApp.getContext().getSharedPreferences(Constants.APP_SETTINGS_FILE, Context.MODE_PRIVATE);
    }

    public static void setSomeStringValue(String label, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(label, value);
        editor.apply();
    }

    public static String getSomeStringValue(String label) {
        return getSharedPreferences().getString(label, null);
    }
}
