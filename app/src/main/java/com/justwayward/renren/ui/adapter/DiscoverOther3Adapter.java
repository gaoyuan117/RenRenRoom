package com.justwayward.renren.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.justwayward.renren.R;
import com.justwayward.renren.bean.SubZoneBean;

import java.util.List;

/**
 * Created by gaoyuan on 2018/2/9.
 */

public class DiscoverOther3Adapter extends BaseQuickAdapter<SubZoneBean.SubBeanX,BaseViewHolder> {

    public DiscoverOther3Adapter(@LayoutRes int layoutResId, @Nullable List<SubZoneBean.SubBeanX> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SubZoneBean.SubBeanX item) {
        ImageView imageView = helper.getView(R.id.img_cover);

        Glide.with(mContext).load(item.getIcon()).error(R.drawable.cover_default)
                .into(imageView);

        helper.setText(R.id.tv_title, item.getZone_name())
                .setText(R.id.tv_des, item.getDesc());



    }
}
