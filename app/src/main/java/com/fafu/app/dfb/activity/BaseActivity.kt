package com.fafu.app.dfb.activity

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable

@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity() {
    protected val mCDisposable = CompositeDisposable()

    val TAG = javaClass.simpleName

    override fun onDestroy() {
        super.onDestroy()
        mCDisposable.dispose()
    }

    protected fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
