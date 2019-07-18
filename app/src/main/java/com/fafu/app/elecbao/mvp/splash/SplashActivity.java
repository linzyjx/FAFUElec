package com.fafu.app.elecbao.mvp.splash;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.fafu.app.elecbao.R;
import com.fafu.app.elecbao.mvp.base.BaseActivity;

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
