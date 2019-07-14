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
                    Log.d("SplashActivity", "User-Agent ==> " + StringUtils.getUserAgent());
                    boolean b = SPUtils.contain("sourcetypeticket") && SPUtils.contain("account");
                    if (!b) {
                        CookieUtils.clearCookie();
                    }
                    if (!SPUtils.contain("User-Agent")) {
                        String agent = StringUtils.getUserAgent();
                        SPUtils.putString("User-Agent", agent);
                    }
                    emitter.onNext(b);
                    emitter.onComplete();
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(b -> {
                    if (b) {
                        mView.openActivity(new Intent(mView.getContext(), MainActivity.class));
                    } else {
                        mView.openActivity(new Intent(mView.getContext(), LoginActivity.class));
                    }
                    mView.killSelf();
                }, throwable -> {
                    onError(throwable);
                    mView.openActivity(new Intent(mView.getContext(), LoginActivity.class));
                    mView.killSelf();
                });
    }
}
