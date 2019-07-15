package com.fafu.app.dfb.mvp.login;

import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.fafu.app.dfb.data.UserMe;
import com.fafu.app.dfb.mvp.base.BasePresenter;
import com.fafu.app.dfb.mvp.main.MainActivity;

import io.reactivex.disposables.Disposable;

public class LoginPresenter extends BasePresenter<LoginContract.View, LoginContract.Model> implements LoginContract.Presenter {

    LoginPresenter(LoginContract.View view) {
        super(view, new LoginModel());
        onStart();
    }

    @Override
    public void onStart() {
        UserMe user = mModel.getUserMe();
        if (user != null) {
            mView.setSnoEtText(user.getSno());
            mView.setPasswordText(user.getPassword());
        }
        verify();
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
                .doFinally(() -> mView.hideLoading())
                .subscribe(s -> {
                    JSONObject jo = JSONObject.parseObject(s);
                    if (jo.getBoolean("IsSucceed")) {
                        mView.showMessage("登录成功");
                        UserMe user = new UserMe();
                        user.setAccount(jo.getString("Obj"));
                        JSONObject obj2 = jo.getJSONObject("Obj2");
                        user.setSno(obj2.getString("SNO"));
                        user.setName(obj2.getString("NAME"));
                        user.setPassword(password);
                        mModel.save(user, obj2.getString("RescouseType"));
                        mView.openActivity(new Intent(mView.getContext(), MainActivity.class));
                        mView.killSelf();
                    } else {
                        if (jo.containsKey("Msg")) {
                            mView.showMessage(jo.getString("Msg"));
                        }
                        verify();
                    }
                }, throwable -> {
                    onError(throwable);
                    verify();
                });
        mCompDisposable.add(d);
    }

}
