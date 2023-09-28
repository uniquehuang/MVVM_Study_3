package com.example.wanandroid.ui.home.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wanandroid.R;
import com.example.wanandroid.base.BaseFragment;
import com.example.wanandroid.databinding.FragmentHomepageBinding;
import com.example.wanandroid.databinding.FragmentProjectBinding;
import com.example.wanandroid.entity.Project;
import com.example.wanandroid.ui.user.adapter.ProjectFragmentBaseAdapter;
import com.example.wanandroid.ui.user.viewmodel.HomePageViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dengfeng
 * @data 2023/4/20
 * @description
 */
public class ProjectFragment extends BaseFragment {

    private ProjectFragmentBaseAdapter adapter;

    private FragmentProjectBinding binding;
    List<Project> projectList=new ArrayList<>();
    private ProjectAdapter projectAdapter;

    private HomePageViewModel homePageViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProjectBinding.inflate(inflater);
        //获取viewModel
        homePageViewModel = new ViewModelProvider(this).get(HomePageViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    protected void initData() {
        //设置item布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recycleView.setLayoutManager(linearLayoutManager);
        //baseAdapter
        projectAdapter = new ProjectAdapter(R.layout.item_project, projectList);
        //设置不同数据发生改变
        projectAdapter.setDiffCallback(new ProjectAdapter.DiffEventCallback());

        binding.recycleView.setAdapter(projectAdapter);

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
}
