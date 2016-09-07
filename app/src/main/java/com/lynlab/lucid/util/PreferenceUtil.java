package com.lynlab.lucid.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author lyn
 * @since 2016/09/07
 */
public class PreferenceUtil {

    public static final String KEY_ACCESS_TOKEN = "AccessToken";

    private static final String KEY_PREFERENCE = "LucidPreference";

    public static String getStringPreference(Context context, String key, String defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_PREFERENCE, Context.MODE_PRIVATE);
        return preferences.getString(key, defaultValue);
    }

    public static void setPreference(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
