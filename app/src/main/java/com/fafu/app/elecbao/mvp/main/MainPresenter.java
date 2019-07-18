package com.fafu.app.elecbao.mvp.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;

import com.fafu.app.elecbao.data.DFInfo;
import com.fafu.app.elecbao.data.QueryData;
import com.fafu.app.elecbao.mvp.base.BasePresenter;
import com.fafu.app.elecbao.mvp.login.LoginActivity;
import com.fafu.app.elecbao.util.RxJavaUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainPresenter extends BasePresenter<MainContract.View, MainContract.Model>
        implements MainContract.Presenter {

    private DFInfo xqInfos;
    private DFInfo ldInfos;
    private DFInfo lcInfos;

    private final List<DFInfo> dkInfos;
    private QueryData queryData;

    private Map<String, String> nameToAidMap;

    MainPresenter(MainContract.View view) {
        super(view, new MainModel(view.getContext()));
        dkInfos = mModel.getInfoFromJson();
        queryData = new QueryData();
        nameToAidMap = new HashMap<>();
        onStart();
    }

    @Override
    public void onStart() {
        super.onStart();
        mCompDisposable.add(RxJavaUtils.<Boolean>create(emitter -> {
            queryData = mModel.getQueryData();
            if (queryData == null) {
                queryData = new QueryData();
                emitter.onNext(false);
            } else {
                emitter.onNext(true);
            }
            emitter.onComplete();
        }).doOnSubscribe(disposable -> mView.showLoading()
        ).subscribe(bool -> {
            checkLoginStatus();
            mView.setSnoText(mModel.getSno());
        }, this::onError));
    }

    /**
     * 检查登录态，失效则跳转到登陆界面
     */
    private void checkLoginStatus() {
        mCompDisposable.add(mModel.initCookie()
                .doOnSubscribe(disposable -> mView.showLoading())
                .subscribe(isReLogin -> {
                    if (isReLogin) {
                        mView.showMessage("登录态失效，请重新登录");
                        mView.openActivity(new Intent(mView.getContext(), LoginActivity.class));
                        mView.killSelf();
                    } else {
                        queryData.setAccount(mModel.getAccount());
                        mView.setSnoText(mModel.getSno());
                        // 初始化电控
                        intiElecCtrl();
                    }
                }, this::onError)
        );
    }

    /**
     * 初始化电控
     */
    private void intiElecCtrl() {
        mCompDisposable.add(mModel.queryElecCtrl()
                .doOnSubscribe(disposable -> mView.showLoading())
                .doFinally(() -> mView.hideLoading())
                .subscribe(s -> {
                    // 查询余额，放这，防止提前hideLoading
                    balance(false);
                    nameToAidMap = s;
                    mView.showElecCtrlRBtn(new ArrayList<>(nameToAidMap.keySet()));
                    if (!queryData.getRoom().isEmpty()) {
                        //设置快捷查询电费
                        for (Map.Entry<String, String> e : nameToAidMap.entrySet()) {
                            if (e.getValue().equals(queryData.getAid())) {
                                mView.setElecSelInfo(e.getKey(), queryData.getArea(), queryData.getBuilding(), queryData.getFloor());
                                onDKSelect(e.getKey(),false);
                                if (!queryData.getArea().isEmpty()) {
                                    onAreaSelect(queryData.getArea());
                                }
                                if (!queryData.getBuilding().isEmpty()) {
                                    onBuildingSelect(queryData.getBuilding());
                                }
                                if (!queryData.getFloor().isEmpty()) {
                                    onFloorSelect(queryData.getFloor());
                                }
                                mView.setRoomText(queryData.getRoom());
                                break;
                            }
                        }
                    }
                }, this::onError)
        );
    }

    private void onDKSelect(String name, boolean isInit) {
        // 每次重选电控时，刷新界面与数据
        if (isInit) {
            queryData.init();
            mView.initViewVisibility();
        }
        Optional<DFInfo> o = dkInfos.stream()
                .filter(info -> name.equals(info.getName()))
                .findFirst();
        o.ifPresent(info -> {
            queryData.setAid(nameToAidMap.get(name));
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
    public void onDKSelect(String name) {
        //如果存在Room，则为首次查询设置check
        onDKSelect(name, queryData.getRoom().isEmpty());
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
    public void onFloorSelect(String name) {
        Optional<DFInfo> o = lcInfos.getData().stream()
                .filter(info -> info.getName().equals(name))
                .findFirst();
        o.ifPresent(info -> {
            queryData.setFloorId(info.getId());
            queryData.setFloor(info.getName());
            Log.d(TAG, "onFloorSelect DFInfo ==> name:" + info.getName());
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
                }, this::onError)
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
        mCompDisposable.add(mModel.queryElec(queryData)
                .doOnSubscribe(disposable -> mView.showLoading())
                .doFinally(() -> mView.hideLoading())
                .subscribe(s -> {
                    Log.d(TAG, s);
                    if (s.contains("无法")) {
                        mView.showMessage(s + "，请检查信息是否正确");
                    } else {
                        mModel.save(queryData);
                        mView.setElecText(s);
                        mView.showMessage("查询成功");
                        mView.showPayView();
                    }
                }, this::onError)
        );
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
        StringBuilder msg = new StringBuilder("请确认缴费信息：\n");
        if (!queryData.getArea().isEmpty()) {
            msg.append("\t\t校区：").append(queryData.getArea()).append("\n");
        }
        if (!queryData.getBuilding().isEmpty()) {
            msg.append("\t\t楼栋：").append(queryData.getBuilding()).append("\n");
        }
        if (!queryData.getFloor().isEmpty()) {
            msg.append("\t\t楼层").append(queryData.getFloor()).append("\n");
        }
        msg.append("\t\t宿舍号：").append(queryData.getRoom()).append("\n");
        msg.append("\t\t充值金额：").append(mView.getPriceET().getText().toString()).append("元");
        mView.showConfirmDialog(msg.toString());
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
                    }, this::onError)
            );
        } catch (Exception e) {
            mView.showMessage("请输入正确的金额");
        }
    }
}
