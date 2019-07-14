package com.fafu.app.dfb.mvp.login

import android.content.Intent
import android.os.Bundle
import com.alibaba.fastjson.JSONObject

import com.fafu.app.dfb.R
import com.fafu.app.dfb.data.UserMe
import com.fafu.app.dfb.http.RetrofitFactory
import com.fafu.app.dfb.http.XFBaoService
import com.fafu.app.dfb.mvp.base.BaseActivity
import com.fafu.app.dfb.mvp.base.i.IPresenter
import com.fafu.app.dfb.mvp.main.MainActivity
import com.fafu.app.dfb.view.ProgressDialog
import kotlinx.android.synthetic.main.activity_login.*

class LoginUiActivity : BaseActivity<IPresenter>() {

    private val model = LoginModel()
    private lateinit var progress: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        progress = ProgressDialog(this)
        Thread {
            RetrofitFactory.obtainService(XFBaoService::class.java).default().execute()
            verify()
        }.start()
        verifyIV.setOnClickListener { verify() }
        btn_login.setOnClickListener { login() }
    }

    private fun login() {
        val account = accountET.editableText.toString()
        val password = passwordET.editableText.toString()
        val verify = verifyET.editableText.toString()
        val d = model.login(account, password, verify)
                .doOnSubscribe { showLoading() }
                .subscribe({
                    val json = JSONObject.parseObject(it)
                    if (json.getBoolean("IsSucceed") == true) {
                        showMessage("登录成功")
                        val obj2 = json.getJSONObject("Obj2")
                        val user = UserMe()
                        user.sno = obj2.getString("SNO")
                        user.name = obj2.getString("NAME")
                        user.password = password
                        user.rescouseType = obj2.getString("RescouseType")
                        model.save(user)
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        if (json.containsKey("Msg")) {
                            showMessage(json.getString("Msg"))
                        }
                        verify()
                    }
                    hideLoading()
                }, {
                    it.printStackTrace()
                    showMessage(it.cause.toString())
                    hideLoading()
                })
    }

    private fun verify() {
        val d = model.verify()
                .subscribe({
                    verifyIV.setImageBitmap(it)
                }, {
                    showMessage(it.cause.toString())
                })
    }

    override fun showLoading() {
        progress.show("登录中")
    }

    override fun hideLoading() {
        progress.cancel()
    }

}
