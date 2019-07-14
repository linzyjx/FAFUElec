package com.fafu.app.dfb.mvp.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fafu.app.dfb.R;
import com.fafu.app.dfb.mvp.base.BaseActivity;
import com.fafu.app.dfb.mvp.base.i.IPresenter;
import com.fafu.app.dfb.mvp.login.LoginActivity;
import com.fafu.app.dfb.mvp.main.MainActivity;
import com.fafu.app.dfb.util.CookieUtils;
import com.fafu.app.dfb.util.SPUtils;
import com.fafu.app.dfb.util.StringUtils;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends BaseActivity<SplashContract.Presenter>
        implements SplashContract.View {

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mPresenter = new SplashPresenter(this);
    }
}
