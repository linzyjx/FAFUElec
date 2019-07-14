package com.fafu.app.dfb;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.webkit.WebView;

import com.fafu.app.dfb.util.CookieUtils;
import com.fafu.app.dfb.util.SPUtils;

public class DFBao extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        SPUtils.init(this);
        CookieUtils.clearCookie();
        if (!SPUtils.contain("User-Agent")) {
            String agent = new WebView(this).getSettings().getUserAgentString();
            SPUtils.putString("User-Agent", agent);
        }
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
