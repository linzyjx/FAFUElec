package com.fafu.app.dfb;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class FAFUElec extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
