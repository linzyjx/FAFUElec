package com.fafu.app.dfb.mvp.main;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.fafu.app.dfb.data.DFInfo;
import com.fafu.app.dfb.data.QueryData;
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

    MainModel(Context context) {
        this.context = context;
    }

    @Override
    public Observable<String> initCookie() {
        return RxJavaUtils.create(emitter -> {
            service.default2(SPUtils.get("Cookie").getString("RescouseType")).execute();
            ResponseBody responseBody = service.page(
                    "31", "3", "2", "", "electricity",
                    URLEncoder.encode("交电费", "gbk"),
                    SPUtils.get("Cookie").getString("sourcetypeticket"),
                    SPUtils.get("Const").getString("IMEI"),
                    "0", "1"
            ).execute().body();
            emitter.onNext(responseBody.string());
            emitter.onComplete();
        });
    }

    @Override
    public String getAccount() {
        return SPUtils.get("UserInfo").getString("account");
    }

    @Override
    public Observable<String> queryAppInfo(String aid) {
        return RxJavaUtils.create(emitter -> {
            ResponseBody responseBody = service.query(
                    "{ \"query_appinfo\": { \"aid\": \"" + aid + "\", \"account\": \"" +
                            SPUtils.get("UserInfo").getString("account") + "\" } }",
                    "synjones.onecard.query.appinfo",
                    "true"
            ).execute().body();
            String support = JSONObject.parseObject(responseBody.string())
                    .getJSONObject("Msg")
                    .getJSONObject("query_appinfo")
                    .getJSONObject("appinfo")
                    .getString("support");
            emitter.onNext(support);
            emitter.onComplete();
        });
    }

    @Override
    public Observable<String> queryDetail(String aid, String area, String building, String floor) {
        return RxJavaUtils.create(emitter -> {
            ResponseBody responseBody = service.query(
                    String.format(
                            "{\"query_appinfo\":{\"aid\":\"%s\",\"account\":\"%s\" }}",
                            aid, SPUtils.get("UserInfo").getString("account")
                    ),
                    "synjones.onecard.query.appinfo",
                    "true"
            ).execute().body();
            emitter.onNext(responseBody.string());
            emitter.onComplete();
        });
    }

    @Override
    public Observable<String> queryElec(QueryData data) {
        return RxJavaUtils.create(emitter -> {
            ResponseBody responseBody = service.query(
                    "{\"query_elec_roominfo\":" + data.toJsonString() + "}",
                    "synjones.onecard.query.elec.roominfo",
                    "true"
            ).execute().body();
            String msg = JSONObject.parseObject(responseBody.string())
                    .getJSONObject("Msg")
                    .getJSONObject("query_elec_roominfo")
                    .getString("errmsg");
            emitter.onNext(msg);
            emitter.onComplete();
        });
    }

    @Override
    public Observable<String> elecPay(QueryData data, String price) {
        return RxJavaUtils.create(emitter -> {
            ResponseBody responseBody = service.elecPay(
                    "http://cardapp.fafu.edu.cn:8088/PPage/ComePage",
                    "###", "1", data.getAid(),
                    data.getAccount(), price, data.getRoom(), data.getRoom(),
                    data.getFloorId(), data.getFloor(),
                    data.getBuildingId(), data.getBuilding(),
                    data.getAreaId(), data.getArea(), "true"
            ).execute().body();
            String msg = JSONObject.parseObject(responseBody.string())
                    .getJSONObject("Msg")
                    .getJSONObject("pay_elec_gdc")
                    .getString("errmsg");
            emitter.onNext(msg);
            emitter.onComplete();
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

    @Override
    public Observable<Double> queryBalance() {
        return RxJavaUtils.create(emitter -> {
            ResponseBody responseBody = service.queryBalance(
                    "true"
            ).execute().body();
            JSONObject msg = JSONObject.parseObject(responseBody.string())
                    .getJSONObject("Msg")
                    .getJSONObject("query_card")
                    .getJSONArray("card")
                    .getJSONObject(0);
            Double total = (msg.getIntValue("db_balance") + msg.getIntValue("unsettle_amount")) / 100.0;
            emitter.onNext(total);
            emitter.onComplete();
        });
    }

    @Override
    public String getSno() {
        return SPUtils.get("UserInfo").getString("sno");
    }

    @Override
    public void clearAll() {
        SPUtils.get("UserInfo").clear();
        SPUtils.get("Cookie").clear();
    }
}
