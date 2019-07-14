package com.fafu.app.dfb.mvp.main;

import android.widget.EditText;

import com.fafu.app.dfb.data.DFInfo;
import com.fafu.app.dfb.mvp.base.i.IModel;
import com.fafu.app.dfb.mvp.base.i.IPresenter;
import com.fafu.app.dfb.mvp.base.i.IView;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

class MainContract {

    interface View extends IView {

        void setElecText(String text);

        void setSelectorView(int i, List<String> list);

        void showElecCheckView();

        void showPayView();

       void initViewData();

        EditText getPriceTv();
    }

    interface Presenter extends IPresenter {
        void onXQSelect(String name);
        void onLDSelect(String name);
        void onLCSelect(String name);

        /**
         * 1 -> 常工电子电控,
         * 2 -> 山东科大电子电控
         * 3 -> 开普电子电控
         * 4 -> 开普电控东苑
         */
        void onDKSelect(int option);

        void checkElec(String room);

        void pay();
    }

    interface Model extends IModel {

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

        Observable<String> queryElec(Map<String, String> dataMap);

        Observable<String> elecPay(Map<String, String> dataMap, String price);
    }

}
