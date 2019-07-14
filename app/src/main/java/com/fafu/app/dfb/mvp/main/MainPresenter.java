package com.fafu.app.dfb.mvp.main;

import android.util.Log;

import com.fafu.app.dfb.data.DFInfo;
import com.fafu.app.dfb.mvp.base.BasePresenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.reactivex.disposables.Disposable;

public class MainPresenter extends BasePresenter<MainContract.View, MainContract.Model>
        implements MainContract.Presenter {

    //常工电子电控, 山东科大电子电控, 开普电子电控, 开普电控东苑
    private final String[] aids = {"0030000000002501", "0030000000008001",
            "0030000000008101", "0030000000008102"};

    private List<DFInfo> dkInfos;
    private DFInfo xqInfos;
    private DFInfo ldInfos;
    private DFInfo lcInfos;

    private Map<String, String> postDataMap = new HashMap<>();

    public MainPresenter(MainContract.View view) {
        mView = view;
        mModel = new MainModel(mView.getContext());
        Disposable d = mModel.initCookie()
                .doOnSubscribe(disposable -> mView.showLoading())
                .subscribe(s -> {
                    Log.d(TAG, s);
                    mView.hideLoading();
                }, this::showError);
        dkInfos = mModel.getInfoFromJson();
        initPostDataMap();
        mCDisposable.add(d);
    }

    private void initPostDataMap() {
        postDataMap.put("aid", "");
        postDataMap.put("roomid", "");
        postDataMap.put("room", "");
        postDataMap.put("floorid", "");
        postDataMap.put("floor", "");
        postDataMap.put("area", "");
        postDataMap.put("areaname", "");
        postDataMap.put("buildingid", "");
        postDataMap.put("building", "");
    }

    @Override
    public void onDKSelect(int option) {
        initPostDataMap();
        mView.clearViewData();
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
            postDataMap.put("area", info.getId());
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
        Disposable d = mModel.queryElec(postDataMap)
                .doOnSubscribe(disposable -> mView.showLoading())
                .subscribe(s -> {
                    Log.d(TAG, s);
                    mView.hideLoading();
                }, this::showError);
        mCDisposable.add(d);
    }

    private void showError(Throwable throwable) {
        throwable.printStackTrace();
        mView.showMessage(throwable.getCause().toString());
        mView.hideLoading();
    }

    private List<String> getNames(DFInfo info) {
        return info.getData().stream()
                .flatMap((Function<DFInfo, Stream<String>>) info1 -> Stream.of(info1.getName()))
                .collect(Collectors.toList());
    }
}
