package com.example.wanandroid.ui.user.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.wanandroid.databinding.ActivityUserLoginBinding;
import com.example.wanandroid.ui.user.viewmodel.MyLoginViewModel;
import com.xworld.base.BaseVMActivity;


//同步异步问题，数据不统一
//context并不能finish一个activity


/**
 * @author dengfeng
 * @data 2023/4/12
 * @description
 */
public class LoginActivity extends BaseVMActivity {

    //视图绑定
    ActivityUserLoginBinding loginBinding;
    //ViewModel
    private MyLoginViewModel myLoginViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        //获得viewModel实例
        myLoginViewModel = new ViewModelProvider(this).get(MyLoginViewModel.class);
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    protected View initLayout() {
        return loginBinding.getRoot();
    }

    @Override
    protected void initView() {
        loginBinding = ActivityUserLoginBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initData() {
    }

    private void login() {
        loginBinding.editTextTextPersonName.setText("hwl12");
        loginBinding.editTextTextPassword.setText("123456");
        String username = loginBinding.editTextTextPersonName.getText().toString();
        String password = loginBinding.editTextTextPassword.getText().toString();
        Context context = getApplicationContext();
        myLoginViewModel.login(context,username,password);
    }

    @Override
    protected void initListener() {
        //注册
        loginBinding.userRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
        });

        //登录
        loginBinding.loginButton.setOnClickListener(view -> {
            login();
        });
        //观察者
        myLoginViewModel.loginResult.observe(this, user -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }


    /*private void login(String account, String pwd) {
        if (TextUtils.isEmpty(loginBinding.editTextTextPersonName.getText().toString())) {
            Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(loginBinding.editTextTextPassword.getText().toString())) {
            Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        Observable<BaseEntity<User>> observable = RetrofitFactory.getInstence().API().login(new User(account,pwd));
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressDialogSubscriber<BaseEntity<User>>(this) {

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(BaseEntity<User> userBaseEntity) {
                        if (userBaseEntity.isSuccess()){
                            Log.i("TAG", "onNext: "+userBaseEntity.getErrorMsg());
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else {
                            runOnUiThread(() -> Toast.makeText(LoginActivity.this,userBaseEntity.getErrorMsg(), Toast.LENGTH_SHORT).show());
                        }
                    }
                });

    }
*/
}
