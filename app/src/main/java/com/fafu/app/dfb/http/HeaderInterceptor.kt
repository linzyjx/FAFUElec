package com.fafu.app.dfb.http

import com.fafu.app.dfb.util.SPUtils
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain
                .request()
                .newBuilder()
                .addHeader("Pragma", "no-cache")
                .addHeader("Cache-Control", "no-cache")
        if (SPUtils.contain("User-Agent")) {
            requestBuilder.addHeader("User-Agent", SPUtils.getString("User-Agent"))
        }
        return chain.proceed(requestBuilder.build())
    }
}