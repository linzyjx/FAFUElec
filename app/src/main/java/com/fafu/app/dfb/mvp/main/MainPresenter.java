package com.fafu.app.dfb.mvp.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;

import com.fafu.app.dfb.data.DFInfo;
import com.fafu.app.dfb.data.QueryData;
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

public class MainPresenter extends BasePresenter<MainContract.View, MainContract.Model>
        implements MainContract.Presenter {

    //常工电子电控, 山东科大电子电控, 开普电子电控, 开普电控东苑
    private final String[] aids = {"0030000000002501", "0030000000008001",
            "0030000000008101", "0030000000008102"};

    private DFInfo xqInfos;
    private DFInfo ldInfos;
    private DFInfo lcInfos;

    private final List<DFInfo> dkInfos = mModel.getInfoFromJson();
    private final QueryData queryData = new QueryData();

    MainPresenter(MainContract.View view) {
        super(view, new MainModel(view.getContext()));
        onStart();
    }

    @Override
    public void onStart() {
        super.onStart();
        mView.setSnoText(mModel.getSno());
        mCompDisposable.add(mModel.initCookie()
                .doOnSubscribe(disposable -> mView.showLoading())
                .doFinally(() -> mView.hideLoading())
                .subscribe(s -> {
                    if (s.contains("<title>登录</title>")) {
                        mView.showMessage("登录态失效，请重新登录");
                        mView.openActivity(new Intent(mView.getContext(), LoginActivity.class));
                        mView.killSelf();
                    } else {
                        queryData.setAccount(mModel.getAccount());
                        mView.setSnoText(mModel.getSno());
                        balance(false);
                        Log.d(TAG, "InitCookie Successful");
                    }
                }, this::showError)
        );
        queryData.clear();
    }

    @Override
    public void onDKSelect(int option) {
        queryData.clear();
        mView.initViewVisibility();
        Optional<DFInfo> o = dkInfos.stream()
                .filter(info -> info.getId().equals(aids[option - 1]))
                .findFirst();
        o.ifPresent(info -> {
            queryData.setAid(aids[option - 1]);
            xqInfos = info;
            Log.d(TAG, "onDKSelect DFInfo ==> name:" + info.getName());
            if (info.getNext() == 1) {
                xqInfos = info;
                mView.setSelectorView(1, getNames(info));
            } else if (info.getNext() == 2) {
                ldInfos = info;
                mView.setSelectorView(2, getNames(info));
            } else if (info.getNext() == 3) {
                lcInfos = info;
                mView.setSelectorView(3, getNames(info));
            }
        });
    }

    @Override
    public void onAreaSelect(String name) {
        Optional<DFInfo> o = xqInfos.getData().stream()
                .filter(info -> info.getName().equals(name))
                .findFirst();
        o.ifPresent(info -> {
            queryData.setAreaId(info.getId());
            queryData.setArea(info.getName());
            Log.d(TAG, "onAreaSelect DFInfo ==> name:" + info.getName());
            if (info.getNext() == 1) {
                xqInfos = info;
                mView.setSelectorView(1, getNames(info));
            } else if (info.getNext() == 2) {
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
    public void quit() {
        mModel.clearAll();
        mView.openActivity(new Intent(mView.getContext(), LoginActivity.class));
        mView.killSelf();
    }

    @Override
    public void onBuildingSelect(String name) {
        Optional<DFInfo> o = ldInfos.getData().stream()
                .filter(info -> info.getName().equals(name))
                .findFirst();
        o.ifPresent(info -> {
            queryData.setBuildingId(info.getId());
            queryData.setBuilding(info.getName());
            Log.d(TAG, "onBuildingSelect DFInfo ==> name:" + info.getName());
            if (info.getNext() == 1) {
                xqInfos = info;
                mView.setSelectorView(1, getNames(info));
            } else if (info.getNext() == 2) {
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
            queryData.setFloorId(info.getId());
            queryData.setFloor(info.getName());
            Log.d(TAG, "onLCSelect DFInfo ==> name:" + info.getName());
            if (info.getNext() == 1) {
                xqInfos = info;
                mView.setSelectorView(1, getNames(info));
            } else if (info.getNext() == 2) {
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
    public void queryBalance() {
        balance(true);
    }

    @SuppressLint("DefaultLocale")
    private void balance(boolean toast) {
        mCompDisposable.add(mModel.queryBalance()
                .doOnSubscribe(disposable -> mView.showLoading())
                .doFinally(() -> mView.hideLoading())
                .subscribe(balance -> {
                    String t = String.format("%.2f", balance);
                    mView.setBalanceText(t);
                    if (toast) {
                        mView.showMessage(String.format("当前账户余额：%s元", t));
                    }
                }, this::showError)
        );
    }

    @Override
    public void queryElecFees() {
        String room = mView.getRoomET().getText().toString();
        if (room.isEmpty()) {
            mView.showMessage("请输入正确的宿舍号");
            return;
        }
        queryData.setRoom(room);
        Disposable d = mModel.queryElec(queryData)
                .doOnSubscribe(disposable -> mView.showLoading())
                .doFinally(() -> mView.hideLoading())
                .subscribe(s -> {
                    Log.d(TAG, s);
                    if (s.contains("无法")) {
                        mView.showMessage(s + "，请检查信息是否正确");
                    } else {
                        mView.setElecText(s);
                        mView.showPayView();
                    }
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
    public void whetherPay() {
        String price = mView.getPriceET().getText().toString();
        if (price.isEmpty()) {
            mView.showMessage("请输入正确的金额");
            return;
        }
        mView.showConfirmDialog();
    }

    @Override
    public void pay() {
        try {
            int price = Integer.valueOf(mView.getPriceET().getText().toString()) * 100;
            if (price <= 0) {
                mView.showMessage("请输入正确的金额");
                return;
            }
            mCompDisposable.add(mModel.elecPay(queryData, String.valueOf(price))
                    .subscribe(s -> {
                        mView.showMessage(s);
                        balance(false);
                    }, this::showError)
            );
        } catch (Exception e) {
            mView.showMessage("请输入正确的金额");
        }
    }
}
