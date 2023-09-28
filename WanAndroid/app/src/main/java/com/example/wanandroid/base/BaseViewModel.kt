package com.example.wanandroid.base

import android.content.Context
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.wanandroid.entity.BaseEntity
import com.example.wanandroid.entity.Image
import com.example.wanandroid.http.ProgressDialogSubscriber
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

open class BaseViewModel: ViewModel() {


    var mContext: Context? = null



    fun showToast(msg: String?) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
    }

    fun showToastSync(msg: String?) {
        Looper.prepare()
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
        Looper.loop()
    }



    //管理线程
    fun <R> toSubscribe(o: Observable<BaseEntity<MutableList<Image>>>, s: ProgressDialogSubscriber<BaseEntity<MutableList<Image>>>?) {
        o.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(s)
    }
}