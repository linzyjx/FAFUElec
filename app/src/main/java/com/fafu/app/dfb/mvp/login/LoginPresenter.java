package com.fafu.app.dfb.mvp.login;

import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.fafu.app.dfb.data.UserMe;
import com.fafu.app.dfb.mvp.base.BasePresenter;
import com.fafu.app.dfb.mvp.main.MainActivity;

import io.reactivex.disposables.Disposable;

public class LoginPresenter extends BasePresenter<LoginContract.View, LoginContract.Model> implements LoginContract.Presenter {

    LoginPresenter(LoginContract.View view) {
        mView = view;
        mModel = new LoginModel();
    }

    @Override
    public void verify() {
        Disposable d = mModel.verifyBitmap()
                .subscribe(bitmap -> mView.setVerifyBitmap(bitmap), this::onError);
        mCompDisposable.add(d);
    }

    @Override
    public void login() {
        String sno = mView.getSNoEditable().toString();
        String password = mView.getPasswordEditable().toString();
        String verify = mView.getVerifyEditable().toString();
        Disposable d = mModel.login(sno, password, verify)
                .doOnSubscribe( disposable -> mView.showLoading())
                .subscribe(s -> {
                    JSONObject jo = JSONObject.parseObject(s);
                    if (jo.getBoolean("IsSucceed")) {
                        mView.showMessage("登录成功");
                        JSONObject obj2 = jo.getJSONObject("Obj2");
                        UserMe user = new UserMe();
                        user.setSno(obj2.getString("SNO"));
                        user.setName(obj2.getString("NAME"));
                        user.setPassword(password);
                        user.setRescouseType(obj2.getString("RescouseType"));
                        mModel.save(user);
                        mView.openActivity(new Intent(mView.getContext(), MainActivity.class));
                        mView.killSelf();
                    } else {
                        if (jo.containsKey("Msg")) {
                            mView.showMessage(jo.getString("Msg"));
                        }
                        verify();
                    }
                    mView.hideLoading();
                }, this::onError);
        mCompDisposable.add(d);
    }
}
