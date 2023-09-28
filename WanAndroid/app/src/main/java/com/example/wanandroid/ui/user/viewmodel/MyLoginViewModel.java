package com.example.wanandroid.ui.user.viewmodel;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import com.example.wanandroid.base.BaseViewModel;
import com.example.wanandroid.entity.BaseEntity;
import com.example.wanandroid.entity.User;
import com.example.wanandroid.http.ProgressDialogSubscriber;
import com.example.wanandroid.http.RetrofitFactory;
import java.util.HashMap;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * @author dengfeng
 * @data 2023/4/12
 * @description
 */
public class MyLoginViewModel extends BaseViewModel {


    private MutableLiveData<Integer> loginSuccess;


    //登录回调
    public MutableLiveData<User> loginResult = new MutableLiveData<>();



    /*setValue 只能在主线程调用，同步更新数据
    postValue 可在后台线程调用，其内部会切换到主线程调用 setValue*/
/*    public MutableLiveData<Integer> getLoginSuccess() {
        if (loginSuccess == null){
            loginSuccess = new MutableLiveData<>();
            loginSuccess.setValue(0);
        }
        return loginSuccess;
    }*/



    //登录
    //传入上下文，用户名，密码
   public void login (Context context, String userName, String password){

        if (TextUtils.isEmpty(userName)){
            Toast.makeText(context, "请输入账号", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
       HashMap<String,String> map=new HashMap<>();
       map.put("username", userName); map.put("password",password);
       Observable<BaseEntity<User>> observable = RetrofitFactory.getInstence().API().login(map);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressDialogSubscriber<BaseEntity<User>>(context) {

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(BaseEntity<User> userBaseEntity) {
                        if (userBaseEntity.isSuccess()){
                            //输入数据
                            loginResult.setValue(userBaseEntity.getData());
                        }else {
                            Toast.makeText(context, userBaseEntity.getErrorMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
