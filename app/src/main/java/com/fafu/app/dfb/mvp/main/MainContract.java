package com.fafu.app.dfb.mvp.main;

import android.widget.EditText;

import com.fafu.app.dfb.data.DFInfo;
import com.fafu.app.dfb.data.QueryData;
import com.fafu.app.dfb.mvp.base.i.IModel;
import com.fafu.app.dfb.mvp.base.i.IPresenter;
import com.fafu.app.dfb.mvp.base.i.IView;

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

        void showConfirmDialog();

        EditText getPriceET();

        EditText getRoomET();
    }

    interface Presenter extends IPresenter {

        void quit();

        void onAreaSelect(String name);

        void onBuildingSelect(String name);

        void onLCSelect(String name);

        /**
         * 查询余额
         */
        void queryBalance();

        /**
         * 1 -> 常工电子电控,
         * 2 -> 山东科大电子电控
         * 3 -> 开普电子电控
         * 4 -> 开普电控东苑
         */
        void onDKSelect(int option);

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
         */
        Observable<String> initCookie();

        /**
         * 选择电控后，获取需要选择的选项
         */
        Observable<String> queryAppInfo(String aid);

        /**
         * 选择电控后，获取详细信息
         */
        Observable<String> queryDetail(String aid, String area, String building, String floor);

        List<DFInfo> getInfoFromJson();

        Observable<Double> queryBalance();

        Observable<String> queryElec(QueryData data);

        Observable<String> elecPay(QueryData data, String price);
    }

}
