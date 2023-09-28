package com.example.wanandroid.ui.home.fragment;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.wanandroid.R;
import com.example.wanandroid.entity.Project;
import com.example.wanandroid.http.RetrofitFactory;

import java.util.List;

/**
 * @author dengfeng
 * @data 2023/5/29
 * @description
 */
public class ProjectAdapter extends BaseQuickAdapter<Project, BaseViewHolder> {

    public ProjectAdapter(int layoutResId, @Nullable List<Project> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, Project project) {

        baseViewHolder.setText(R.id.item_desc,project.getDesc());
        baseViewHolder.setText(R.id.item_author,project.getAuthor());
        baseViewHolder.setText(R.id.item_date,project.getNiceDate());
        baseViewHolder.setText(R.id.item_title,project.getTitle());
        Glide.with(getContext())
                .load(project.getEnvelopePic())
                .disallowHardwareConfig()
                .apply(new RequestOptions()
                        .transforms(new CenterCrop(), new RoundedCorners(8)
                        ))
                .into((ImageView) baseViewHolder.getView(R.id.item_iv_cardViewBak));
    }

    public static class DiffEventCallback extends DiffUtil.ItemCallback<Project>{

        @Override
        public boolean areItemsTheSame(@NonNull Project oldItem, @NonNull Project newItem) {
            return oldItem.getTitle()== newItem.getTitle();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Project oldItem, @NonNull Project newItem) {
            return oldItem.getTitle().equals(newItem.getTitle())
                    && oldItem.getDesc().equals(newItem.getDesc());

        }
    }


}
