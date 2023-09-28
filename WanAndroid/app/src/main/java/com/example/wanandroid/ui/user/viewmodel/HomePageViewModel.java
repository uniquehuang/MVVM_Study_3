package com.example.wanandroid.ui.user.viewmodel;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.wanandroid.base.BaseViewModel;
import com.example.wanandroid.entity.BaseList;
import com.example.wanandroid.entity.BaseEntity;
import com.example.wanandroid.entity.Project;

import com.example.wanandroid.http.ProgressDialogSubscriber;
import com.example.wanandroid.http.RetrofitFactory;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author dengfeng
 * @data 2023/5/29
 * @description
 */
public class HomePageViewModel extends BaseViewModel {

    //回调
    public MutableLiveData<List<Project>> getProjectsResult = new MutableLiveData<>();


    public void getProjects(Context context){

        Observable<BaseEntity<BaseList<Project>>> observable = RetrofitFactory.getInstence().API().getProjectList();
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressDialogSubscriber<BaseEntity<BaseList<Project>>>(context){
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(BaseEntity<BaseList<Project>> listBaseEntity) {
                if(listBaseEntity.isSuccess()){
                    //获取数据
                    getProjectsResult.setValue(listBaseEntity.getData().getDatas());
                }else {
                    Toast.makeText(context, listBaseEntity.getErrorMsg(), Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

}
