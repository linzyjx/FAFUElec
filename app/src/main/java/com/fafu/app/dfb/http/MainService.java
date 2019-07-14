package com.fafu.app.dfb.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MainService {


    /**
     * 初始化Cookie
     */
    @POST("/NoBase/TPGetAppList")
    @FormUrlEncoded
    Call<ResponseBody> default2(
            @Field("sourcetype") String sourcetype
    );

    /**
     * 获取交电费页面，更新Cookie
     */
    @POST("/Page/Page")
    @FormUrlEncoded
    Call<ResponseBody> page(
            @Field("flowid") String flowid,
            @Field("type") String type,
            @Field("apptype") String apptype,
            @Field("url") String url,
            @Field("EMenuName") String EMenuName,
            @Field("MenuName") String MenuName,
            @Field("sourcetype") String sourcetype,
            @Field("IMEI") String IMEI,
            @Field("language") String language,
            @Field("comeapp") String comeapp
    );


    /**
     * 查询信息
     */
    @POST("/Tsm/TsmCommon")
    @FormUrlEncoded
    Call<ResponseBody> query(
            @Field("jsondata") String jsondata,
            @Field("funname") String funname,
            @Field("json") String json
    );

}
