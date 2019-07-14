package com.fafu.app.dfb.mvp.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fafu.app.dfb.mvp.base.i.IPresenter;
import com.fafu.app.dfb.mvp.base.i.IView;
import com.google.android.material.snackbar.Snackbar;
import com.jaeger.library.StatusBarUtil;

public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity implements IView {

    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setLightMode(this);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void showMessage(String msg) {
        View view = this.getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void openActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void killSelf() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
    }

}
