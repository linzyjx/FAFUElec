package com.fafu.app.dfb.mvp.main;

import android.content.Intent;
import android.util.Log;

import com.fafu.app.dfb.data.DFInfo;
import com.fafu.app.dfb.mvp.base.BasePresenter;
import com.fafu.app.dfb.mvp.login.LoginActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;

public class MainPresenter extends BasePresenter<MainContract.View, MainContract.Model>
        implements MainContract.Presenter {

    //常工电子电控, 山东科大电子电控, 开普电子电控, 开普电控东苑
    private final String[] aids = {"0030000000002501", "0030000000008001",
            "0030000000008101", "0030000000008102"};

    private List<DFInfo> dkInfos;
    private DFInfo xqInfos;
    private DFInfo ldInfos;
    private DFInfo lcInfos;

    private final Map<String, String> postDataMap = new HashMap<>();

    MainPresenter(MainContract.View view) {
        super(view, new MainModel(view.getContext()));
        Disposable d = mModel.initCookie()
                .doOnSubscribe(disposable -> mView.showLoading())
                .doFinally(() -> mView.hideLoading())
                .subscribe(s -> {
                    if (s.contains("<title>登录</title>")) {
                        mView.showMessage("登录态失效，请重新登录");
                        mView.openActivity(new Intent(mView.getContext(), LoginActivity.class));
                        mView.killSelf();
                    } else {
                        Log.d(TAG, "InitCookie Successful");
                    }
                }, this::showError);
        dkInfos = mModel.getInfoFromJson();
        initPostDataMap();
        mCompDisposable.add(d);
    }

    private void initPostDataMap() {
        postDataMap.put("aid", "");
        postDataMap.put("roomid", "");
        postDataMap.put("room", "");
        postDataMap.put("floorid", "");
        postDataMap.put("floor", "");
        postDataMap.put("areaid", "");
        postDataMap.put("areaname", "");
        postDataMap.put("buildingid", "");
        postDataMap.put("building", "");
    }

    @Override
    public void onDKSelect(int option) {
        initPostDataMap();
        mView.initViewData();
        Optional<DFInfo> o = dkInfos.stream()
                .filter(info -> info.getId().equals(aids[option-1]))
                .findFirst();
        o.ifPresent(info -> {
            postDataMap.put("aid", aids[option-1]);
            xqInfos = info;
            Log.d(TAG, "onDKSelect DFInfo ==> name:" + info.getName());
            if (info.getNext() == 1) {
                xqInfos = info;
                mView.setSelectorView(1, getNames(info));
            } else if (info.getNext() == 2){
                ldInfos = info;
                mView.setSelectorView(2, getNames(info));
            } else if (info.getNext() == 3) {
                lcInfos = info;
                mView.setSelectorView(3, getNames(info));
            }
        });
    }

    @Override
    public void onXQSelect(String name) {
        Optional<DFInfo> o = xqInfos.getData().stream()
                .filter(info -> info.getName().equals(name))
                .findFirst();
        o.ifPresent(info -> {
            postDataMap.put("areaid", info.getId());
            postDataMap.put("areaname", info.getName());
            Log.d(TAG, "onXQSelect DFInfo ==> name:" + info.getName());
            if (info.getNext() == 1) {
                xqInfos = info;
                mView.setSelectorView(1, getNames(info));
            } else if (info.getNext() == 2){
                ldInfos = info;
                mView.setSelectorView(2, getNames(info));
            } else if (info.getNext() == 3) {
                lcInfos = info;
                mView.setSelectorView(3, getNames(info));
            } else {
                mView.showElecCheckView();
            }
        });
    }

    @Override
    public void onLDSelect(String name) {
        Optional<DFInfo> o = ldInfos.getData().stream()
                .filter(info -> info.getName().equals(name))
                .findFirst();
        o.ifPresent(info -> {
            postDataMap.put("buildingid", info.getId());
            postDataMap.put("building", info.getName());
            Log.d(TAG, "onLDSelect DFInfo ==> name:" + info.getName());
            if (info.getNext() == 1) {
                xqInfos = info;
                mView.setSelectorView(1, getNames(info));
            } else if (info.getNext() == 2){
                ldInfos = info;
                mView.setSelectorView(2, getNames(info));
            } else if (info.getNext() == 3) {
                lcInfos = info;
                mView.setSelectorView(3, getNames(info));
            } else {
                mView.showElecCheckView();
            }
        });
    }

    @Override
    public void onLCSelect(String name) {
        Optional<DFInfo> o = lcInfos.getData().stream()
                .filter(info -> info.getName().equals(name))
                .findFirst();
        o.ifPresent(info -> {
            postDataMap.put("floorid", info.getId());
            postDataMap.put("floor", info.getName());
            Log.d(TAG, "onLCSelect DFInfo ==> name:" + info.getName());
            if (info.getNext() == 1) {
                xqInfos = info;
                mView.setSelectorView(1, getNames(info));
            } else if (info.getNext() == 2){
                ldInfos = info;
                mView.setSelectorView(2, getNames(info));
            } else if (info.getNext() == 3) {
                lcInfos = info;
                mView.setSelectorView(3, getNames(info));
            } else {
                mView.showElecCheckView();
            }
        });
    }

    @Override
    public void checkElec(String room) {
        postDataMap.put("room", room);
        postDataMap.put("roomid", room);
        Disposable d = mModel.queryElec(postDataMap)
                .doOnSubscribe(disposable -> mView.showLoading())
                .subscribe(s -> {
                    Log.d(TAG, s);
                    if (s.contains("无法")) {
                        mView.showMessage(s + "，请检查信息是否正确");
                    } else {
                        mView.setElecText(s);
                        mView.showPayView();
                    }
                    mView.hideLoading();
                }, this::showError);
        mCompDisposable.add(d);
    }

    private void showError(Throwable throwable) {
        throwable.printStackTrace();
        mView.showMessage(throwable.getMessage());
    }

    private List<String> getNames(DFInfo info) {
        return info.getData().stream()
                .flatMap((Function<DFInfo, Stream<String>>) info1 -> Stream.of(info1.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public void pay() {
        String price = mView.getPriceTv().getText().toString();
        try {
            price = String.valueOf((Integer.valueOf(price) * 100));
            Disposable d = mModel.elecPay(postDataMap, price)
                    .subscribe(s -> mView.showMessage(s), this::showError);
            mCompDisposable.add(d);
        } catch (Exception e) {
            mView.showMessage("请输入正确的金额");
        }
    }
}
