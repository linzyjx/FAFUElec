package com.fafu.app.dfb.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class SPUtils {

    private static SharedPreferences sp;

    public static void init(Context context) {
        if (context != null && sp == null) {
            sp = context.getSharedPreferences("FAFU_Elec", Context.MODE_PRIVATE);
        }
    }

    public static void putString(String key, String value) {
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public static void putStringSet(String key, Set<String> value) {
        SharedPreferences.Editor edit = sp.edit();
        edit.putStringSet(key, value);
        edit.apply();
    }

    public static void putInt(String key, Integer value) {
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.apply();
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    public static Set<String> getStringSet(String key, Set<String> defaultValue) {
        return sp.getStringSet(key, defaultValue);
    }

    public static String getString(String key) {
        return sp.getString(key, "");
    }

    public static boolean getBoolean(String key) {
        return sp.getBoolean(key, false);
    }

    public static void remove(String key) {
        sp.edit().remove(key).apply();
    }

    public static boolean contain(String key) {
        return sp.contains(key);
    }
}
