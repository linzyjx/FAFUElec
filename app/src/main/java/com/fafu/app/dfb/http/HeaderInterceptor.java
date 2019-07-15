package com.fafu.app.dfb.http;

import androidx.annotation.NonNull;

import com.fafu.app.dfb.util.SPUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder builder = chain.request()
                .newBuilder()
                .addHeader("Prama", "no-cache")
                .addHeader("Cache-Control", "no-cache");
        if (SPUtils.get("Const").contain("User-Agent")) {
            builder.addHeader("User-Agent", SPUtils.get("Const").getString("User-Agent"));
        }
        return chain.proceed(builder.build());
    }
}
