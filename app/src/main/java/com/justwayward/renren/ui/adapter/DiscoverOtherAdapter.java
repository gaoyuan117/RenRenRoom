package com.justwayward.renren.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.justwayward.renren.R;
import com.justwayward.renren.bean.SubZoneBean;
import com.justwayward.renren.utils.LogUtils;

import java.util.List;

/**
 * Created by gaoyuan on 2017/11/21.
 */

public class DiscoverOtherAdapter extends BaseQuickAdapter<SubZoneBean.SubBeanX, BaseViewHolder> {

    public DiscoverOtherAdapter(@LayoutRes int layoutResId, @Nullable List<SubZoneBean.SubBeanX> data) {
        super(layoutResId, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, SubZoneBean.SubBeanX item) {
        ImageView imageView = helper.getView(R.id.img_cover);
        LogUtils.e("图片链接：" + item.getIcon());
        Glide.with(mContext).load(item.getIcon().replace("amp;","")).asBitmap().into(imageView);

        helper.setText(R.id.tv_title, item.getZone_name())
                .setText(R.id.tv_des, item.getDesc());

    }


}
