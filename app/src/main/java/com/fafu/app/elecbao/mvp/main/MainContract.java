package com.fafu.app.elecbao.mvp.main;

import android.widget.EditText;

import com.fafu.app.elecbao.data.DFInfo;
import com.fafu.app.elecbao.data.QueryData;
import com.fafu.app.elecbao.mvp.base.i.IModel;
import com.fafu.app.elecbao.mvp.base.i.IPresenter;
import com.fafu.app.elecbao.mvp.base.i.IView;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

class MainContract {

    interface View extends IView {

        void setSnoText(String text);

        void setElecText(String text);

        void setSelectorView(int i, List<String> list);

        void setBalanceText(String text);

        void showElecCheckView();

        void showPayView();

        void initViewVisibility();

        void showConfirmDialog(String message);

        void showElecCtrlRBtn(Collection<String> strings);

        void setRoomText(String text);

        /**
         * 设置电费选项信息
         */
        void setElecSelInfo(String dkName, String area, String building, String floor);

        /**
         * 获取充值金额信息EditText
         */
        EditText getPriceET();

        /**
         * 获取宿舍号信息EditText
         */
        EditText getRoomET();
    }

    interface Presenter extends IPresenter {

        void quit();

        void onAreaSelect(String name);

        void onBuildingSelect(String name);

        void onFloorSelect(String name);

        void onDKSelect(String name);

        /**
         * 查询余额
         */
        void queryBalance();

        /**
         * 查询电费
         */
        void queryElecFees();

        /**
         * 充值前弹窗
         */
        void whetherPay();

        /**
         * 确认充值
         */
        void pay();

    }

    interface Model extends IModel {

        /**
         * 清除所有数据
         */
        void clearAll();

        /**
         * 获取当前账号的学号
         */
        String getSno();

        /**
         * 获取当前账号
         */
        String getAccount();

        /**
         * 初始化Cookie
         * @return 是否重新登录
         */
        Observable<Boolean> initCookie();

        List<DFInfo> getInfoFromJson();

        Observable<Double> queryBalance();

        /**
         * 获取所有电控信息
         * key: Name(电控名字) value: Aid(电控Id)
         */
        Observable<Map<String, String>> queryElecCtrl();

        /**
         * 充值电费
         */
        Observable<String> queryElec(QueryData data);

        Observable<String> elecPay(QueryData data, String price);

        /**
         * 保存查询选项，用于一键查询
         */
        void save(QueryData data);

        /**
         * @return 一键查询数据
         */
        QueryData getQueryData();
    }

}
