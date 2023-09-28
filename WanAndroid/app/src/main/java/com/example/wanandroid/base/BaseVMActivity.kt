package com.xworld.base

import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.studymvvmproject01.base.BaseActivity
import com.example.wanandroid.base.BaseViewModel

/**
 * 基础MVVM Activity
 */
abstract class BaseVMActivity :BaseActivity() {
   /* val binding: VB by lazy { inflaterBlock(layoutInflater) }
    var mViewModel: VM? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this)[modelClass]
        setContentView(binding.root)*/
//        if (binding.root is ViewGroup) {
//            initItemLaguage(binding.root as ViewGroup?)
//        }



}