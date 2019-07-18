package com.fafu.app.elecbao.mvp.login;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.fafu.app.elecbao.R;
import com.fafu.app.elecbao.mvp.base.BaseActivity;
import com.fafu.app.elecbao.view.ProgressDialog;

public class LoginActivity extends BaseActivity<LoginContract.Presenter> implements LoginContract.View {

    private EditText snoET;
    private EditText passwordET;
    private EditText verifyET;
    private ImageView verifyIV;
    private ProgressDialog progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        snoET = findViewById(R.id.accountET);
        passwordET = findViewById(R.id.passwordET);
        verifyET = findViewById(R.id.verifyET);
        verifyIV = findViewById(R.id.verifyIV);
        Button loginBtn = findViewById(R.id.loginBtn);
        verifyIV.setOnClickListener( v -> mPresenter.verify());
        loginBtn.setOnClickListener(v -> mPresenter.login());
        progress = new ProgressDialog(this, "加载中");
        mPresenter = new LoginPresenter(this);
    }

    @Override
    public void setSnoEtText(String sno) {
        snoET.setText(sno);
    }

    @Override
    public void setPasswordText(String password) {
        passwordET.setText(password);
    }

    @Override
    public Editable getSNoEditable() {
        return snoET.getText();
    }

    @Override
    public Editable getPasswordEditable() {
        return passwordET.getText();
    }

    @Override
    public Editable getVerifyEditable() {
        return verifyET.getText();
    }

    @Override
    public void setVerifyBitmap(Bitmap bitmap) {
        verifyIV.setImageBitmap(bitmap);
    }

    @Override
    public void showLoading() {
        progress.show();
    }

    @Override
    public void hideLoading() {
        progress.cancel();
    }
}
