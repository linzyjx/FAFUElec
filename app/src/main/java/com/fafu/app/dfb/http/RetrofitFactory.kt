package com.fafu.app.dfb.http

import okhttp3.OkHttpClient
import retrofit2.Retrofit

object RetrofitFactory {

    private val client = OkHttpClient.Builder()
            .addInterceptor(CookiesInterceptor())
            .addInterceptor(HeaderInterceptor())
            .build()

    private val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("http://cardapp.fafu.edu.cn:8088")
            .build()

    fun <T> obtainService(clazz: Class<T>, headers: Map<String, String>? = null): T {
        if (headers != null) {
            client.newBuilder()
                    .addInterceptor {
                        val builder = it.request().newBuilder()
                        headers.forEach { (t, u) ->
                            builder.addHeader(t, u)
                        }
                        it.proceed(builder.build())
                    }
        }
        return retrofit.create(clazz)
    }

}