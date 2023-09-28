package com.example.wanandroid.entity;

import java.util.List;

/**
 * @author dengfeng
 * @data 2023/5/29
 * @description
 */
public class BaseList<T> {

    private List<T> datas;

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }
}
