package com.example.wanandroid.ui.home.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wanandroid.R;
import com.example.wanandroid.base.BaseFragment;
import com.example.wanandroid.databinding.FragmentHomepageBinding;
import com.example.wanandroid.entity.Project;
import com.example.wanandroid.ui.home.ImageAdapter;
import com.example.wanandroid.ui.user.view.LoginActivity;
import com.example.wanandroid.ui.user.view.MainActivity;
import com.example.wanandroid.ui.user.viewmodel.HomePageViewModel;
import com.example.wanandroid.ui.user.viewmodel.MyLoginViewModel;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author dengfeng
 * @data 2023/4/20
 * @description
 */
public class HomePageFragment extends BaseFragment {

    FragmentHomepageBinding binding;

    private HomePageViewModel homePageViewModel;
    List<Project> projectList=new ArrayList<>();
    private ProjectAdapter projectAdapter;

    private int index = 0;
    private final int AUTOPLAY = 2;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomepageBinding.inflate(inflater);
        //初始化画廊
        initGallery();
       binding.myGallery.setOnItemClickListener((parent, view, position, id) -> Toast.makeText
                (getContext(), "当前位置position:" + id + "的图片被选中了", Toast.LENGTH_SHORT).show());
        //获得viewModel实例
        homePageViewModel = new ViewModelProvider(this).get(HomePageViewModel.class);
        return binding.getRoot();

    }


    protected void initData() {
        //自动播放
        autoPlay();

        //设置item布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        //baseAdapter
        projectAdapter = new ProjectAdapter(R.layout.item_project, projectList);
        //设置不同数据发生改变
        projectAdapter.setDiffCallback(new ProjectAdapter.DiffEventCallback());
        binding.recyclerView.setAdapter(projectAdapter);

        //获取数据
        homePageViewModel.getProjects(getContext());


        //观察者
        homePageViewModel.getProjectsResult.observe(this, projectList -> {
            projectList = homePageViewModel.getProjectsResult.getValue();

            Log.i("hwl1234",projectList.get(1).getDesc());

            projectAdapter.setDiffNewData(projectList);
            projectAdapter.notifyDataSetChanged();
        });

    }



    //初始化画廊
    private void initGallery() {
        //用网络加载的方式未实现
        /*pictureAdapter = new PictureAdapter(this, mUrls);*/
       /* GlideAdapter glideAdapter = new GlideAdapter(this, mUrls);
        binding.myGallery.setAdapter(glideAdapter);*/
        //图片资源数组，本地图片资源
        int[] imageResIDs = new int[]{R.mipmap.w1, R.mipmap.w2, R.mipmap.w3};
        ImageAdapter adapter = new ImageAdapter(imageResIDs, getContext());
        binding.myGallery.setAdapter(adapter);
        /*binding.myGallery.setSpacing(20); //图片之间的间距*/
        binding.myGallery.setSelection((Integer.MAX_VALUE / 2) - (Integer.MAX_VALUE / 2) % imageResIDs.length);
        binding.myGallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    //在消息队列中实现对控件的更改，实现画廊的自动播放
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == AUTOPLAY) {
                binding.myGallery.setSelection(index);
            }
        }
    };

    private void autoPlay(){
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = AUTOPLAY;
                index = binding.myGallery.getSelectedItemPosition();
                index++;
                handler.sendMessage(message);
            }
        };
        Timer timer = new Timer();
        timer.schedule(task,3000,3000);
    }


}
