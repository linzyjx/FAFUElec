package com.fafu.app.dfb.mvp.login;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.fafu.app.dfb.data.UserMe;
import com.fafu.app.dfb.http.RetrofitFactory;
import com.fafu.app.dfb.http.LoginService;
import com.fafu.app.dfb.mvp.base.BaseModel;
import com.fafu.app.dfb.util.RxJavaUtils;
import com.fafu.app.dfb.util.SPUtils;
import com.fafu.app.dfb.util.StringUtils;

import java.io.IOException;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class LoginModel extends BaseModel implements LoginContract.Model {

    private final LoginService service = RetrofitFactory.obtainService(LoginService.class, null);
    private boolean isInit = false;

    @Override
    public Observable<Bitmap> verifyBitmap() {
        return RxJavaUtils.create( emitter -> {
            if (!isInit) {
                isInit = true;
                service.init("0", StringUtils.imei(), "0").execute();
            }
            ResponseBody responseBody = service.verify(
                    String.valueOf(System.currentTimeMillis())
            ).execute().body();
            try {
                assert responseBody != null;
                byte[] bytes = responseBody.bytes();
                emitter.onNext(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            } catch (IOException e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Observable<String> login(String sno, String password, String verify) {
        return RxJavaUtils.create( emitter -> {
            ResponseBody responseBody = service.login(
                    "http://cardapp.fafu.edu.cn:8088/Phone/Login?sourcetype=0&IMEI=" + StringUtils.imei() + "&language=0",
                    sno, new String(Base64.encode(password.getBytes(), Base64.DEFAULT)),
                    verify, "1", "1", "", "true"
            ).execute().body();
            assert responseBody != null;
            emitter.onNext(responseBody.string());
        });
    }

    @Override
    public void save(UserMe user) {
        SPUtils.putString("sno", user.getSno());
        SPUtils.putString("password", user.getPassword());
        SPUtils.putString("name", user.getName());
        SPUtils.putString("RescouseType", user.getRescouseType());
    }

}
