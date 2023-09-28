package com.example.studymvvmproject01.base


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


abstract class BaseActivity : AppCompatActivity() {
    var mContext: Context? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        initView()
        setContentView(initLayout())
        initData()
        initListener()
    }

    protected abstract fun initLayout(): View?
    protected abstract fun initView()
    protected abstract fun initData()
    protected abstract fun initListener()
    fun showToast(msg: String?) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
    }

    fun showToastSync(msg: String?) {
        Looper.prepare()
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
        Looper.loop()
    }

    fun navigateTo(cls: Class<*>?) {
        val `in` = Intent(mContext, cls)
        startActivity(`in`)
    }

    fun navigateToWithFlag(cls: Class<*>?, flags: Int) {
        val `in` = Intent(mContext, cls)
        `in`.flags = flags
        startActivity(`in`)
    }

    protected fun insertVal(key: String?, `val`: String?) {
        val sp = getSharedPreferences("User_Tab", MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString(key, `val`)
        editor.apply()
    }

    protected fun findByKey(key: String?): String? {
        val sp = getSharedPreferences("User_Tab", MODE_PRIVATE)
        return sp.getString(key, "")
    }

    //管理线程
    fun <R> toSubscribe(o: Observable<R>, s: Subscriber<R>?) {
        o.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(s)
    }

}