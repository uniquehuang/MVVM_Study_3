package com.example.wanandroid.http;

/**
 * @author dengfeng
 * @data 2023/4/11
 * @description
 */

import com.example.wanandroid.entity.BaseEntity;
import com.example.wanandroid.entity.BaseList;
import com.example.wanandroid.entity.Image;
import com.example.wanandroid.entity.Project;
import com.example.wanandroid.entity.User;
import com.example.wanandroid.http.config.URLConfig;

import java.util.HashMap;
import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;


/**
 * @author yemao
 * @date 2017/4/9
 * @description API接口!
 */

public interface APIFunction {
    //登录接口
    /*@QueryMap注解会把参数拼接到url后面，所以它适用于GET请求；@Body会把参数放到请求体中，所以适用于POST请求。*/
    @POST(URLConfig.LOGIN)
    Observable<BaseEntity<User>> login(@QueryMap HashMap<String,String> user);
    //注册接口
    @POST(URLConfig.REGISTER)
    Observable<BaseEntity<String>> register(@Body User register);


    @GET(URLConfig.BANNER)
    Observable<BaseEntity<List<Image>>> getImageList();



    @GET(URLConfig.ITEM)
    Observable<BaseEntity<BaseList<Project>>> getProjectList();

}