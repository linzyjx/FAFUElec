package com.fafu.app.elecbao.mvp.main;

import android.content.Intent;

import com.fafu.app.elecbao.data.QueryData;
import com.fafu.app.elecbao.data.Selection;
import com.fafu.app.elecbao.mvp.base.BasePresenter;
import com.fafu.app.elecbao.mvp.login.LoginActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter extends BasePresenter<MainContract.View, MainContract.Model>
        implements MainContract.Presenter {

    private Selection areaInfos;
    private Selection buildingInfos;
    private Selection floorInfos;

    private final List<Selection> dkInfos;
    private QueryData userQueryData;

    private Map<String, String> dkNameToAidMap = new HashMap<>();

    MainPresenter(MainContract.View view) {
        super(view, new MainModel(view.getContext()));
        dkInfos = mModel.getSelectionFromJson();
    }

    @Override
    public void onStart() {
        // 检查登录态
        checkLoginStatus();
        // 查询余额
        queryCardBalance(false);
        // 设置学号
        mView.setSnoText(mModel.getSno());
        // 初始化电控
        intiElecCtrl();
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
                        mView.setSnoText(mModel.getSno());
                    }
                }, this::onError)
        );
    }

    /**
     * 初始化电控
     */
    private void intiElecCtrl() {
        mCompDisposable.add(mModel.queryDKInfos()
                .doOnNext(stringStringMap -> dkNameToAidMap = stringStringMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    mView.showDKSelection(dkNameToAidMap.keySet());
                    //设置快捷查询电费
                    quickQueryElec();
                }, this::onError)
        );
    }

    //设置快捷查询电费
    private void quickQueryElec() {
        userQueryData = mModel.getQueryData();
        if (userQueryData == null || userQueryData.getRoom() == null || userQueryData.getRoom().isEmpty())
            return;
        for (Map.Entry<String, String> e : dkNameToAidMap.entrySet()) {
            if (e.getValue().equals(userQueryData.getAid())) {
                mView.setSelections(e.getKey(), userQueryData.getArea(), userQueryData.getBuilding(), userQueryData.getFloor());
                if (userQueryData.getArea() != null && !userQueryData.getArea().isEmpty()) {
                    onAreaSelect(userQueryData.getArea());
                }
                if (userQueryData.getBuilding() != null && !userQueryData.getBuilding().isEmpty()) {
                    onBuildingSelect(userQueryData.getBuilding());
                }
                if (userQueryData.getFloor() != null && !userQueryData.getFloor().isEmpty()) {
                    onFloorSelect(userQueryData.getFloor());
                }
                mView.setRoomText(userQueryData.getRoom());
                break;
            }
        }
    }

    /**
     * @param name   电控名字
     * @param isInit 是否初始化View和数据
     */
    private void onDKChecked(String name, boolean isInit) {
        // 每次重选电控时，刷新界面与数据
        if (isInit) {
            mView.initViewVisibility();
        }
        Selection info = new Selection();
        for (Selection i : dkInfos) {
            if (name.equals(i.getName())) {
                info = i;
            }
        }
        areaInfos = info;
        if (info.getNext() == 1) {
            areaInfos = info;
            mView.setSelectorView(1, getNames(info));
        } else if (info.getNext() == 2) {
            buildingInfos = info;
            mView.setSelectorView(2, getNames(info));
        } else if (info.getNext() == 3) {
            floorInfos = info;
            mView.setSelectorView(3, getNames(info));
        }
    }

    @Override
    public void onDKSelect(String name) {
        //如果存在Room，则设置不初始化mView
        onDKChecked(name, true);
//        Log.d(TAG, "onDKChecked ==> " + name);
    }

    @Override
    public void onAreaSelect(String name) {
        Optional<Selection> o = areaInfos.getData().stream()
                .filter(info -> info.getName().equals(name))
                .findFirst();
        o.ifPresent(info -> {
//            Log.d(TAG, "onAreaSelect Selection ==> name:" + info.getName());
            if (info.getNext() == 1) {
                areaInfos = info;
                mView.setSelectorView(1, getNames(info));
            } else if (info.getNext() == 2) {
                buildingInfos = info;
                mView.setSelectorView(2, getNames(info));
            } else if (info.getNext() == 3) {
                floorInfos = info;
                mView.setSelectorView(3, getNames(info));
            } else {
                mView.showElecCheckView();
            }
        });
    }

    @Override
    public void onBuildingSelect(String name) {
        Optional<Selection> o = buildingInfos.getData().stream()
                .filter(info -> info.getName().equals(name))
                .findFirst();
        o.ifPresent(info -> {
//            Log.d(TAG, "onBuildingSelect Selection ==> name:" + info.getName());
            if (info.getNext() == 1) {
                areaInfos = info;
                mView.setSelectorView(1, getNames(info));
            } else if (info.getNext() == 2) {
                buildingInfos = info;
                mView.setSelectorView(2, getNames(info));
            } else if (info.getNext() == 3) {
                floorInfos = info;
                mView.setSelectorView(3, getNames(info));
            } else {
                mView.showElecCheckView();
            }
        });
    }

    @Override
    public void onFloorSelect(String name) {
        Optional<Selection> o = floorInfos.getData().stream()
                .filter(info -> info.getName().equals(name))
                .findFirst();
        o.ifPresent(info -> {
//            Log.d(TAG, "onFloorSelect Selection ==> name:" + info.getName());
            if (info.getNext() == 1) {
                areaInfos = info;
                mView.setSelectorView(1, getNames(info));
            } else if (info.getNext() == 2) {
                buildingInfos = info;
                mView.setSelectorView(2, getNames(info));
            } else if (info.getNext() == 3) {
                floorInfos = info;
                mView.setSelectorView(3, getNames(info));
            } else {
                mView.showElecCheckView();
            }
        });
    }

    @Override
    public void queryCardBalance() {
        queryCardBalance(true);
    }

    /**
     * 查询余额
     *
     * @param showToast 是否显示Toast
     */
    private void queryCardBalance(boolean showToast) {
        mCompDisposable.add(mModel.queryBalance()
                .map(aDouble -> String.format(Locale.CHINA, "%.2f", aDouble))  // 取2位小数
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mView.showLoading())
                .doFinally(() -> mView.hideLoading())
                .subscribe(balance -> {
                    mView.setBalanceText(balance);
                    if (showToast) {
                        mView.showMessage(String.format("当前账户余额：%s元", balance));
                    }
                }, this::onError)
        );
    }

    @Override
    public void queryElecBalance() {
        QueryData userQuery = new QueryData();
        // 设置账号
        userQuery.setAccount(mModel.getAccount());
        //设置电控信息
        String dkName = mView.getCheckedDKName();
        userQuery.setAid(dkNameToAidMap.get(dkName));
        //设置地区
        String area = mView.getAreaText();
        if (!area.isEmpty() && areaInfos != null) {
            userQuery.setArea(area);
            areaInfos.getData().forEach(areaInfo -> {
                if (areaInfo.getName().equals(area)) {
                    userQuery.setAreaId(areaInfo.getId());
                }
            });
        }
        //设置楼栋
        String building = mView.getBuildingText();
        if (!building.isEmpty() && buildingInfos != null) {
            userQuery.setBuilding(building);
            buildingInfos.getData().forEach(buildingInfo -> {
                if (buildingInfo.getName().equals(building)) {
                    userQuery.setBuildingId(buildingInfo.getId());
                }
            });
        }
        //设置楼层
        String floor = mView.getFloorText();
        if (!floor.isEmpty() && floorInfos != null) {
            userQuery.setFloor(building);
            floorInfos.getData().forEach(floorInfo -> {
                if (floorInfo.getName().equals(floor)) {
                    userQuery.setFloorId(floorInfo.getId());
                }
            });
        }
        //设置房间信息
        String room = mView.getRoomText();
        userQuery.setRoom(room);
        if (room.isEmpty()) {
            mView.showMessage("请输入正确的宿舍号");
            return;
        }
        mCompDisposable.add(mModel.queryElec(userQuery)
                .doOnSubscribe(disposable -> mView.showLoading())
                .doFinally(() -> mView.hideLoading())
                .subscribe(s -> {
                    if (s.contains("无法")) {
                        this.userQueryData = null;
                        mView.showMessage(s + "，请检查信息是否正确");
                    } else {
                        this.userQueryData = userQuery;
                        mModel.save(userQueryData);
                        mView.setElecText(s);
                        mView.showMessage("查询成功");
                        mView.showPayView();
                    }
                }, this::onError)
        );
    }

    private List<String> getNames(Selection info) {
        return info.getData().stream()
                .flatMap((Function<Selection, Stream<String>>) info1 -> Stream.of(info1.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public void whetherPay() {
        String price = mView.getPriceText();
        if (price.isEmpty()) {
            mView.showMessage("请输入正确的金额");
            return;
        }
        StringBuilder msg = new StringBuilder("请确认缴费信息：\n");
        if (!userQueryData.getArea().isEmpty()) {
            msg.append("\t\t校区：").append(userQueryData.getArea()).append("\n");
        }
        if (!userQueryData.getBuilding().isEmpty()) {
            msg.append("\t\t楼栋：").append(userQueryData.getBuilding()).append("\n");
        }
        if (!userQueryData.getFloor().isEmpty()) {
            msg.append("\t\t楼层").append(userQueryData.getFloor()).append("\n");
        }
        msg.append("\t\t宿舍号：").append(userQueryData.getRoom()).append("\n");
        msg.append("\t\t充值金额：").append(mView.getPriceText()).append("元");
        mView.showConfirmDialog(msg.toString());
    }

    @Override
    public void pay() {
        mCompDisposable.add(Observable
                .<Integer>create(emitter -> {
                    int price = Integer.valueOf(mView.getPriceText()) * 100;
                    if (price <= 0) {
                        throw new NumberFormatException();
                    }
                    emitter.onNext(price);
                    emitter.onComplete();
                })
                .flatMap(integer -> mModel.elecPay(userQueryData, String.valueOf(integer)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mView.showLoading())
                .doFinally(() -> mView.hideLoading())
                .subscribe(s -> {
                            mView.showMessage(s);
                            queryCardBalance(false);
                        }, throwable -> {
                            if (throwable instanceof NumberFormatException) {
                                mView.showMessage("请输入正确金额");
                            }
                        }
                )
        );
    }

    @Override
    public void quit() {
        mModel.clearAll();
        mView.openActivity(new Intent(mView.getContext(), LoginActivity.class));
        mView.killSelf();
    }

}
