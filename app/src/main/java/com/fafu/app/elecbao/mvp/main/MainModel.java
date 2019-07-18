package com.fafu.app.elecbao.mvp.main;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fafu.app.elecbao.data.DFInfo;
import com.fafu.app.elecbao.data.DaoManager;
import com.fafu.app.elecbao.data.QueryData;
import com.fafu.app.elecbao.data.QueryDataDao;
import com.fafu.app.elecbao.http.MainService;
import com.fafu.app.elecbao.http.RetrofitFactory;
import com.fafu.app.elecbao.mvp.base.BaseModel;
import com.fafu.app.elecbao.util.RxJavaUtils;
import com.fafu.app.elecbao.util.SPUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class MainModel extends BaseModel implements MainContract.Model {

    private final MainService service = RetrofitFactory.obtainService(MainService.class, null);
    private final Context context;

    private final QueryDataDao queryDao;

    MainModel(Context context) {
        queryDao = DaoManager.getInstance().getDaoSession().getQueryDataDao();
        this.context = context;
    }

    @Override
    public Observable<Boolean> initCookie() {
        return RxJavaUtils.create(emitter -> {
            service.default2(SPUtils.get("Cookie").getString("RescouseType")).execute();
            ResponseBody responseBody = service.page(
                    "31", "3", "2", "", "electricity",
                    URLEncoder.encode("交电费", "gbk"),
                    SPUtils.get("Cookie").getString("sourcetypeticket"),
                    SPUtils.get("Const").getString("IMEI"),
                    "0", "1"
            ).execute().body();
            emitter.onNext(responseBody.string().contains("<title>登录</title>"));
            emitter.onComplete();
        });
    }

    @Override
    public String getAccount() {
        return SPUtils.get("UserInfo").getString("account");
    }

    public Observable<String> queryElec(QueryData data) {
        return RxJavaUtils.create(emitter -> {
            ResponseBody responseBody = service.query(
                    data.toFiledMap(QueryData.Query.ROOMINFO)
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
    public Observable<Map<String, String>> queryElecCtrl() {
        return RxJavaUtils.create(emitter -> {
            ResponseBody responseBody = service.query(
                    "{\"query_applist\":{ \"apptype\": \"elec\" }}",
                    "synjones.onecard.query.applist",
                    "true"
            ).execute().body();
            JSONArray ctrlList = JSONObject.parseObject(responseBody.string())
                    .getJSONObject("Msg")
                    .getJSONObject("query_applist")
                    .getJSONArray("applist");
            Map<String, String> aidMap = new LinkedHashMap<>();
            for (Object o : ctrlList) {
                JSONObject jo = (JSONObject) o;
                aidMap.put(jo.getString("name"), jo.getString("aid"));
            }
            emitter.onNext(aidMap);
            emitter.onComplete();
        });
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

    @Override
    public void save(QueryData data) {
        queryDao.insertOrReplace(data);
    }

    @Override
    public QueryData getQueryData() {
        String account = SPUtils.get("UserInfo").getString("account");
        return queryDao.load(account);
    }
}
