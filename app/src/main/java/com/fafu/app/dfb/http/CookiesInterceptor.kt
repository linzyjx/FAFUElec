package com.fafu.app.dfb.http

import com.fafu.app.dfb.util.SPUtils
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import android.util.Log
import com.fafu.app.dfb.util.CookieUtils

class CookiesInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        //添加Cookie
        val builder = chain.request().newBuilder()
        val cookie = CookieUtils.getCookie()
        builder.addHeader("Cookie", cookie)
        //保存Cookie
        val response = chain.proceed(builder.build())
        if (response.headers("Set-Cookie").isNotEmpty()) {
            for (header in response.headers("Set-Cookie")) {
                val kv = header.substring(0, header.indexOf(";")).split("=")
                SPUtils.putString(kv[0], kv[1])
                Log.v("CookiesInterceptor", "Set-Cookie: ${header.substring(0, header.indexOf(";"))}")
            }
        }
        return response
    }

}