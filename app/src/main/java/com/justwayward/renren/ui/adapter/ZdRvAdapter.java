package com.justwayward.renren.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.justwayward.renren.R;

import java.util.List;

/**
 * Created by gaoyuan on 2018/3/30.
 */

public class ZdRvAdapter extends BaseQuickAdapter<String,BaseViewHolder>{

    private int p = 0;

    public ZdRvAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView textView = helper.getView(R.id.tv_fenlei);

        textView.setText(item);

        if (helper.getPosition()==p){
            textView.setTextColor(mContext.getResources().getColor(R.color.global));
        }else {
            textView.setTextColor(mContext.getResources().getColor(R.color.light_black));
        }

    }

    public void setSelect(int position){
        p = position;
        notifyDataSetChanged();
    }
}
