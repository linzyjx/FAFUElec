package com.fafu.app.dfb.http

import com.fafu.app.dfb.util.StringUtils
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import java.net.URLEncoder

interface XFBaoService {

    /**
     * 初始化Cookie
     */
    @GET("/Phone/Login")
    fun default(
            @Query("sourcetype") sourcetype: String = "0",
            @Query("IMEI") IMEI: String = StringUtils.imei(),
            @Query("language") language: String = "0"
    ): Call<ResponseBody>

    @GET("/Phone/GetValidateCode")
    fun verify(
            @Query("time") time: String = System.currentTimeMillis().toString()
    ): Call<ResponseBody>

    @POST("/Phone/Login")
    @FormUrlEncoded
    fun login(
            @Header("Referer") referer: String,
            @Field("sno") sno: String,
            @Field("pwd") pwd: String,
            @Field("yzm") yzm: String,
            @Field("remember") remember: String = "1",
            @Field("uclass") uclass: String = "1",
            @Field("zqcode") zqcode: String = "",
            @Field("json") json: String = "true"
    ): Call<ResponseBody>

}