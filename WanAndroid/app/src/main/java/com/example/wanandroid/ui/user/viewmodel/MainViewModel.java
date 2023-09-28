package com.example.wanandroid.ui.user.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.wanandroid.base.BaseViewModel;
import com.example.wanandroid.entity.BaseEntity;
import com.example.wanandroid.entity.Image;
import com.example.wanandroid.http.RetrofitFactory;
import com.example.wanandroid.http.ProgressDialogSubscriber;
import java.util.List;

import rx.Observable;

/**
 * @author dengfeng
 * @data 2023/4/18
 * @description
 */
public class MainViewModel  extends BaseViewModel {

    //Banner图片列表回调
    public MutableLiveData<List<Image>> bannerImageListResult = new MutableLiveData<>();;



    public void getImageList(Context context){

        Observable<BaseEntity<List<Image>>> observable = RetrofitFactory.getInstence().API().getImageList();
        toSubscribe(observable, new ProgressDialogSubscriber<BaseEntity<List<Image>>>(context) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(BaseEntity<List<Image>> listBaseEntity) {
                if(listBaseEntity.isSuccess()){
                    bannerImageListResult.setValue(listBaseEntity.getData());
                }else {
                    Toast.makeText(context, listBaseEntity.getErrorMsg(), Toast.LENGTH_SHORT).show();
                }

            }


        });

    }



}
