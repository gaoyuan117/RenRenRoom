package com.justwayward.renren.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.justwayward.renren.R;
import com.justwayward.renren.bean.BuyHistoryBean;
import com.justwayward.renren.utils.FormatUtils;

import java.util.List;

/**
 * Created by gaoyuan on 2017/11/22.
 */

public class BuyHistoryAdapter extends BaseQuickAdapter<BuyHistoryBean.DataBean, BaseViewHolder> {

    public BuyHistoryAdapter(@LayoutRes int layoutResId, @Nullable List<BuyHistoryBean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BuyHistoryBean.DataBean item) {

        TextView tvType = helper.getView(R.id.tv_title);
        TextView tvDes = helper.getView(R.id.tv_des);
        TextView tvTime = helper.getView(R.id.tv_time);

        if (item.getType().equals("pay")) {
            tvType.setText("章节订阅");
        } else {
            tvType.setText("包月会员");
        }

        tvDes.setText(item.getNote());

        tvTime.setText(FormatUtils.getDescriptionTimeFromDateString(item.getAdd_time() + "000"));

    }
}
