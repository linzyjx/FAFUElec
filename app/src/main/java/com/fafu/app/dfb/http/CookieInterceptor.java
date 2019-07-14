package com.fafu.app.dfb.http;

import android.util.Log;

import androidx.annotation.NonNull;

import com.fafu.app.dfb.util.CookieUtils;
import com.fafu.app.dfb.util.SPUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CookieInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        String cookie = CookieUtils.getCookie();
        builder.addHeader("Cookie", cookie);
        Response response = chain.proceed(builder.build());
        if (!response.headers("Set-Cookie").isEmpty()) {
            for (String header: response.headers("Set-Cookie")) {
                String[] kv = header.substring(0, header.indexOf(";")).split("=");
                SPUtils.get("Cookie").putString(kv[0], kv[1]);
                Log.d("CookiesInterceptor", "Set-Cookie: " + kv[0] + "=" + kv[1]);
            }
        }
        return response;
    }
}
