package com.fafu.app.dfb.mvp.base.i;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public interface IView {

    /**
     * 显示加载
     */
    default void showLoading() {}

    /**
     * 隐藏加载
     */
    default void hideLoading() {}

    /**
     * 显示消息
     */
    void showMessage(String msg);

    void openActivity(Intent intent);

    Context getContext();

    Activity getActivity();

    void killSelf();
}
