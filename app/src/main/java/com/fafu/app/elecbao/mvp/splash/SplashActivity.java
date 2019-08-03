package com.fafu.app.elecbao.mvp.splash;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.fafu.app.elecbao.R;
import com.fafu.app.elecbao.mvp.base.BaseActivity;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class SplashActivity extends BaseActivity<SplashContract.Presenter>
        implements SplashContract.View {

    @SuppressLint("CheckResult")
    @Override
    @NeedsPermission(Manifest.permission.READ_PHONE_STATE)
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mPresenter = new SplashPresenter(this);
    }
}
