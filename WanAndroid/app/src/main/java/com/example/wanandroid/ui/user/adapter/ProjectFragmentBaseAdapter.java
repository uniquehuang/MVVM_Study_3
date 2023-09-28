package com.example.wanandroid.ui.user.adapter;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.wanandroid.R;
import com.example.wanandroid.entity.Project;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

public class ProjectFragmentBaseAdapter extends BaseQuickAdapter<Project, BaseViewHolder> {

    public ProjectFragmentBaseAdapter(int layoutResId, @Nullable List<Project> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, Project item) {
        helper.setText(R.id.item_title,item.getTitle());
        helper.setText(R.id.item_desc,item.getDesc());
        helper.setText(R.id.item_author,item.getAuthor());
        Glide.with(getContext())
                .load(item.getEnvelopePic())
                .apply(new RequestOptions()
                        .transforms(new CenterCrop(), new RoundedCorners(8)
                        ))
                .into((ImageView) helper.getView(R.id.item_iv_cardViewBak));
    }

    public static class DiffEventCallback extends DiffUtil.ItemCallback<Project>{

        @Override
        public boolean areItemsTheSame(@NonNull Project oldItem, @NonNull Project newItem) {
            return oldItem.getId()== newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Project oldItem, @NonNull Project newItem) {
            return oldItem.getTitle().equals(newItem.getTitle())
                    && oldItem.getAuthor().equals(newItem.getAuthor())
                    && oldItem.getDesc().equals(newItem.getDesc())
                    && oldItem.getEnvelopePic().equals(newItem.getEnvelopePic());
        }
    }
}