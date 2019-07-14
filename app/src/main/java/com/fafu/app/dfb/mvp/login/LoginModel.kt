package com.fafu.app.dfb.mvp.login

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.fafu.app.dfb.data.UserMe
import com.fafu.app.dfb.http.RetrofitFactory
import com.fafu.app.dfb.http.XFBaoService
import com.fafu.app.dfb.util.SPUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

class LoginModel {

    private val service = RetrofitFactory.obtainService(XFBaoService::class.java)
    private val TAG = "LoginModel"

    fun verify(): Observable<Bitmap> {
        return Observable
                .create<Bitmap> {
                    val resp = service.verify().execute()
                    convert(resp.body())?.run {
                        it.onNext(this)
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun login(account: String, password: String, verify: String): Observable<String> {
        return Observable
                .create<String> {
                    val resp = service.login(
                            referer = "http://cardapp.fafu.edu.cn:8088/Phone/Login?sourcetype=0&IMEI=${com.fafu.app.dfb.util.StringUtils.imei()}&language=0",
                            sno = account,
                            pwd = String(Base64.encode(password.toByteArray(), Base64.DEFAULT)),
                            yzm = verify
                    ).execute().body()
                    if (resp != null) {
                        it.onNext(resp.string())
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun save(user: UserMe) {
        SPUtils.putString("sno", user.sno)
        SPUtils.putString("password", user.password)
        SPUtils.putString("name", user.name)
        SPUtils.putString("RescouseType", user.rescouseType)
    }

    private fun convert(value: ResponseBody?): Bitmap? {
        val bytes = value?.bytes() ?: return null
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}