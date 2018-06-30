package com.justwayward.renren.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.justwayward.renren.R;

import java.util.List;

/**
 * Created by gaoyuan on 2018/3/27.
 */

public class ShaiXuanAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ShaiXuanAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

        helper.setText(R.id.tv_fenlei, item);

    }


}
