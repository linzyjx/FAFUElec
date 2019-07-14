package com.fafu.app.dfb.mvp.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fafu.app.dfb.mvp.base.i.IPresenter;
import com.fafu.app.dfb.mvp.base.i.IView;

public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity implements IView {

    protected P mPresenter;

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
