package com.justwayward.renren.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.justwayward.renren.R;
import com.justwayward.renren.bean.TtsThemeBean;

import java.util.List;

/**
 * Created by gaoyuan on 2017/12/3.
 */

public class TtsAdapter extends BaseQuickAdapter<TtsThemeBean, BaseViewHolder> {

    private int p;

    public TtsAdapter(@LayoutRes int layoutResId, @Nullable List<TtsThemeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TtsThemeBean item) {
        TextView type = helper.getView(R.id.tv_type);
        type.setText(item.type);
        if (p == helper.getPosition()) {
            type.setSelected(true);
        } else {
            type.setSelected(false);
        }

        helper.setText(R.id.tv_type, item.type);
    }

    public  void setSelect(int position) {
        p = position;
        notifyDataSetChanged();
    }
}
