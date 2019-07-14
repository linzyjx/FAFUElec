package com.fafu.app.dfb.mvp.login;

import android.graphics.Bitmap;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;

import com.fafu.app.dfb.data.UserMe;
import com.fafu.app.dfb.mvp.base.i.IModel;
import com.fafu.app.dfb.mvp.base.i.IPresenter;
import com.fafu.app.dfb.mvp.base.i.IView;

import io.reactivex.Observable;

class LoginContract {

    interface View extends IView {

        Editable getSNoEditable();
        Editable getPasswordEditable();
        Editable getVerifyEditable();

        /**
         * 设置验证码
         */
        void setVerifyBitmap(Bitmap bitmap);
    }

    interface Model extends IModel {

        /**
         * 获取验证码
         */
        Observable<Bitmap> verifyBitmap();

        /**
         * 登录
         */
        io.reactivex.Observable<String> login(String sno, String password, String verify);

        /**
         * 保存用户信息
         */
        void save(UserMe user);
    }

    interface Presenter extends IPresenter {
        /**
         * 刷新验证码
         */
        void verify();

        void login();
    }
}
