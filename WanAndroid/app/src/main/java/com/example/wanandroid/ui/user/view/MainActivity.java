package com.example.wanandroid.ui.user.view;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.wanandroid.R;
import com.example.wanandroid.databinding.ActivityMainBinding;
import com.example.wanandroid.entity.Image;
import com.example.wanandroid.ui.home.GlideAdapter;
import com.example.wanandroid.ui.home.ImageAdapter;
import com.example.wanandroid.ui.home.PictureAdapter;
import com.example.wanandroid.ui.home.UrlImage;
import com.example.wanandroid.ui.home.fragment.HomePageFragment;
import com.example.wanandroid.ui.home.fragment.HotSpotFragment;
import com.example.wanandroid.ui.home.fragment.ProjectFragment;
import com.example.wanandroid.ui.user.adapter.PagAdapter2;
import com.example.wanandroid.ui.user.viewmodel.MainViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.xworld.base.BaseVMActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseVMActivity {
    ActivityMainBinding binding;

    private MainViewModel mainViewModel;



    private PictureAdapter pictureAdapter;



    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>(
            Arrays.asList("主页","项目","热门"));

    private PagAdapter2 pagAdapter2;

    private ArrayList<String> mUrls = new ArrayList<>();


    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    protected View initLayout() {
       return binding.getRoot();
    }

    @Override
    protected void initView() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        HomePageFragment homePageFragment = new HomePageFragment();
        HotSpotFragment hotSpotFragment = new HotSpotFragment();
        ProjectFragment projectFragment = new ProjectFragment();
        mFragments.add(homePageFragment);
        mFragments.add(hotSpotFragment);
        mFragments.add(projectFragment);

        //若在fragment中嵌套则
        /*pagAdapter2 = new PagAdapter2(getChildFragmentManager(),getLifecycle(),mFragments);*/

        //把tab和viewpager绑定到一起
        //binding.tab.setupWithViewPager(binding.viewpager2);
        //在activity中嵌套fragment
        pagAdapter2 = new PagAdapter2(getSupportFragmentManager(),getLifecycle(),mFragments);
        binding.viewpager2.setAdapter(pagAdapter2);
        //实现动态接口，绑定viewpager2和tab
        new TabLayoutMediator(binding.tab,binding.viewpager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles.get(position));
            }
        }).attach();

    }

    @Override
    protected void initData() {

        mainViewModel.getImageList(getApplicationContext());

        mainViewModel.bannerImageListResult.observe(this, images -> {
            for (Image image : images) {
                mUrls.add(image.getImagePath());
            }
        });

    }

    @Override
    protected void initListener() {

    }





}