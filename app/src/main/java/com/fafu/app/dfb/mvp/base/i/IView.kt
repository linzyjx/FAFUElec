package com.fafu.app.dfb.mvp.base.i

import android.app.Activity
import android.content.Context
import android.content.Intent

interface IView {
    /**
     * 显示加载
     */
    fun showLoading() {}

    /**
     * 隐藏加载
     */
    fun hideLoading() {}

    /**
     * 显示消息
     */
    fun showMessage(msg: String)

    fun openActivity(intent: Intent)

    fun getContext(): Context

    fun getActivity(): Activity

    fun killSelf()

}