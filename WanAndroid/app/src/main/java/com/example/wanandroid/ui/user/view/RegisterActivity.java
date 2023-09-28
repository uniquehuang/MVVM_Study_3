package com.example.wanandroid.ui.user.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.studymvvmproject01.base.BaseActivity;
import com.example.wanandroid.databinding.ActivityUserRegisterBinding;
import com.example.wanandroid.entity.BaseEntity;
import com.example.wanandroid.entity.User;
import com.example.wanandroid.http.ProgressDialogSubscriber;
import com.example.wanandroid.http.RetrofitFactory;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author dengfeng
 * @data 2023/4/12
 * @description
 */
public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRegisterBinding.button.setOnClickListener(view -> {
            register(userRegisterBinding.editRegisterPersonName.getText().toString(),
                    userRegisterBinding.editRegisterPassword.getText().toString(),
                    userRegisterBinding.reRegisterPassword.getText().toString());
        });
    }

    ActivityUserRegisterBinding userRegisterBinding;

    @Nullable
    @Override
    protected View initLayout() {
        return userRegisterBinding.getRoot();
    }

    @Override
    protected void initView() {
        userRegisterBinding = ActivityUserRegisterBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initData() {

    }

    private void register(String username, String password, String repassword) {
        if (TextUtils.isEmpty(userRegisterBinding.editRegisterPersonName.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(userRegisterBinding.editRegisterPassword.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        } else if (!userRegisterBinding.reRegisterPassword.getText().toString().equals(userRegisterBinding.editRegisterPassword.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        Observable<BaseEntity<String>> observable = RetrofitFactory.getInstence().API().register(new User(username, password, repassword));
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressDialogSubscriber<BaseEntity<String>>(this) {

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(BaseEntity<String> stringBaseEntity) {
                        if (stringBaseEntity.isSuccess()) {
                            Log.i("TAG", "onNext: " + stringBaseEntity.getErrorMsg());
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            runOnUiThread(() -> Toast.makeText(RegisterActivity.this, stringBaseEntity.getErrorMsg(), Toast.LENGTH_SHORT).show());
                        }
                    }

                });

    }

    @Override
    protected void initListener() {

    }
}
