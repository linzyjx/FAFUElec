package com.fafu.app.dfb.mvp.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;

import com.fafu.app.dfb.mvp.base.BasePresenter;
import com.fafu.app.dfb.mvp.base.i.IModel;
import com.fafu.app.dfb.mvp.login.LoginActivity;
import com.fafu.app.dfb.mvp.main.MainActivity;
import com.fafu.app.dfb.util.CookieUtils;
import com.fafu.app.dfb.util.SPUtils;
import com.fafu.app.dfb.util.StringUtils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SplashPresenter extends BasePresenter<SplashContract.View, IModel>
        implements SplashContract.Presenter {

    SplashPresenter(SplashContract.View view) {
        super(view);
        onStart();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onStart() {
        Observable
                .<Boolean>create(emitter -> {
                    SPUtils.init(mView.getContext());
                    emitter.onNext(jumpJudge());
                    emitter.onComplete();
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally( () -> mView.killSelf())
                .subscribe(b -> {
                    if (b) {
                        mView.openActivity(new Intent(mView.getContext(), MainActivity.class));
                    } else {
                        mView.openActivity(new Intent(mView.getContext(), LoginActivity.class));
                    }
                }, throwable -> {
                    onError(throwable);
                    mView.openActivity(new Intent(mView.getContext(), LoginActivity.class));
                });
    }

    private boolean jumpJudge() {
        boolean b = SPUtils.get("Cookie").contain("sourcetypeticket") && SPUtils.get("UserInfo").contain("account");
        if (!b) {
            SPUtils.get("Cookie").clear();
            SPUtils.get("UserInfo").clear();
        }
        Log.d("SplashActivity", "IMEI ==> " + StringUtils.imei());
        Log.d("SplashActivity", "User-Agent ==> " + StringUtils.getUserAgent());
        if (!SPUtils.get("Const").contain("IMEI")) {
            SPUtils.get("Const").putString("IMEI", StringUtils.imei());
        }
        if (!SPUtils.get("Const").contain("User-Agent")) {
            SPUtils.get("Const").putString("User-Agent", StringUtils.getUserAgent());
        }
        return b;
    }
}
