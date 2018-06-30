package com.justwayward.renren.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.justwayward.renren.R;
import com.justwayward.renren.bean.HistoryBean;

import java.util.List;

/**
 * Created by gaoyuan on 2017/11/22.
 */

public class ReadyHistoryAdapter extends BaseQuickAdapter<HistoryBean, BaseViewHolder> {

    public ReadyHistoryAdapter(@LayoutRes int layoutResId, @Nullable List<HistoryBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HistoryBean item) {
        ImageView img = helper.getView(R.id.img_cover);
        Glide.with(mContext).load(item.getPic()).error(R.drawable.cover_default).into(img);

        helper.setText(R.id.tv_author, item.getAuthor().split("-")[1])
//                .setText(R.id.tv_des, item.getDes())
                .setText(R.id.tv_des, item.getAuthor().split("-")[0])
//                .setText(R.id.tv_chapter, item.getAuthor().split("-")[0])
                .setText(R.id.tv_title, item.getTitle())
                .addOnClickListener(R.id.tv_read);

    }
}
