package com.justwayward.renren.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.justwayward.renren.R;
import com.justwayward.renren.bean.SubZoneBean;

import java.util.List;

/**
 * Created by gaoyuan on 2017/11/20.
 */

public class DiscoverAdapter extends BaseQuickAdapter<SubZoneBean, BaseViewHolder> {

    public DiscoverAdapter(@LayoutRes int layoutResId, @Nullable List<SubZoneBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SubZoneBean item) {
        ImageView img = helper.getView(R.id.img_icon);
        if (item.getZone_name().equals("排行榜")) {
            img.setImageResource(R.mipmap.paihang_discover);
        } else if (item.getZone_name().equals("会员专区")) {
            img.setImageResource(R.mipmap.huiyuan_discover);
        } else if (item.getZone_name().equals("分类")) {
            img.setImageResource(R.mipmap.fenlei1);
        } else {
            Glide.with(mContext).load(item.getIcon()).error(R.drawable.cover_default).into(img);
        }

        helper.setText(R.id.tv_name, item.getZone_name());


    }
}
