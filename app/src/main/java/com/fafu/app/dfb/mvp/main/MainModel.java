package com.fafu.app.dfb.mvp.main;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.fafu.app.dfb.data.DFInfo;
import com.fafu.app.dfb.http.MainService;
import com.fafu.app.dfb.http.RetrofitFactory;
import com.fafu.app.dfb.mvp.base.BaseModel;
import com.fafu.app.dfb.util.RxJavaUtils;
import com.fafu.app.dfb.util.SPUtils;
import com.fafu.app.dfb.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class MainModel extends BaseModel implements MainContract.Model {

    private final MainService service = RetrofitFactory.obtainService(MainService.class, null);
    private final Context context;

    public MainModel(Context context) {
        this.context = context;
    }

    @Override
    public Observable<String> initCookie() {
        return RxJavaUtils.create(emitter -> {
            service.default2(SPUtils.getString("RescouseType")).execute();
            ResponseBody responseBody = service.page(
                    "31", "3", "2", "", "electricity",
                    URLEncoder.encode("交电费", "gbk"),
                    SPUtils.getString("sourcetypeticket"),
                    StringUtils.imei(),
                    "0", "1"
            ).execute().body();
            emitter.onNext(responseBody.string());
        });
    }

    @Override
    public Observable<String> queryAppInfo(String aid) {
        return RxJavaUtils.create(emitter -> {
            ResponseBody responseBody = service.query(
                    "{ \"query_appinfo\": { \"aid\": \"" + aid+ "\", \"account\": \"88988\" } }",
                    "synjones.onecard.query.appinfo",
                    "true"
            ).execute().body();
            String support = JSONObject.parseObject(responseBody.string())
                    .getJSONObject("Msg")
                    .getJSONObject("query_appinfo")
                    .getJSONObject("appinfo")
                    .getString("support");
            emitter.onNext(support);
        });
    }

    @Override
    public Observable<String> queryDetail(String aid, String area, String building, String floor) {
        return RxJavaUtils.create(emitter -> {

            ResponseBody responseBody = service.query(
                    "{ \"query_appinfo\": { \"aid\": \"" + aid+ "\", \"account\": \"88988\" } }",
                    "synjones.onecard.query.appinfo",
                    "true"
            ).execute().body();
            emitter.onNext(responseBody.string());


        });
    }

    @Override
    public Observable<String> queryElec(Map<String, String> dataMap) {
        return RxJavaUtils.create( emitter -> {
            ResponseBody responseBody = service.query(
                    "{ \"query_elec_roominfo\": { \"aid\":\"" + dataMap.get("aid") + "\", " +
                            "\"account\": \"88988\",\"room\": { \"roomid\": \"" + dataMap.get("room") +
                            "\", \"room\": \"" + dataMap.get("room") + "\" },  \"floor\": { \"floorid\": \"" +
                            dataMap.get("floorid") + "\", \"floor\": \"" + dataMap.get("floor") +
                            "\" }, \"area\": { \"area\": \"" + dataMap.get("areaid") + "\", \"areaname\": \"" +
                            dataMap.get("areaname") + "\" }, \"building\": { \"buildingid\": \"" +
                            dataMap.get("buildingid") + "\", \"building\": \"" + dataMap.get("building") + "\" } } }",
                    "synjones.onecard.query.elec.roominfo",
                    "true"
            ).execute().body();
            String msg = JSONObject.parseObject(responseBody.string())
                    .getJSONObject("Msg")
                    .getJSONObject("query_elec_roominfo")
                    .getString("errmsg");
            emitter.onNext(msg);
        });
    }

    @Override
    public Observable<String> elecPay(Map<String, String> dataMap, String price) {
        return RxJavaUtils.create( emitter -> {
            ResponseBody responseBody = service.elecPay(
                    "http://cardapp.fafu.edu.cn:8088/PPage/ComePage",
                    "###", "1", dataMap.get("aid"), "88988",
                    price, dataMap.get("roomid"), dataMap.get("room"), dataMap.get("floorid"),
                    dataMap.get("floor"), dataMap.get("buildingid"), dataMap.get("building"),
                    dataMap.get("areaid"), dataMap.get("areaname"), "true"
            ).execute().body();
            String msg = JSONObject.parseObject(responseBody.string())
                    .getJSONObject("Msg")
                    .getJSONObject("pay_elec_gdc")
                    .getString("errmsg");
            emitter.onNext(msg);
        });
    }

    @Override
    public List<DFInfo> getInfoFromJson() {
        try {
            InputStream is = context.getAssets().open("data.json");
            BufferedReader isr = new BufferedReader(new InputStreamReader(is));
            String str;
            StringBuilder sb = new StringBuilder();
            while ((str = isr.readLine()) != null) {
                sb.append(str);
            }
            return JSONObject.parseArray(sb.toString(), DFInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
