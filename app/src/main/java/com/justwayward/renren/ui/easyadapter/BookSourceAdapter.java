package com.justwayward.renren.ui.easyadapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.justwayward.renren.R;
import com.justwayward.renren.bean.SourceBean;

import java.util.List;

/**
 * 查询
 *
 * @author yuyh.
 * @date 16/9/3.
 */
public class BookSourceAdapter extends BaseQuickAdapter<SourceBean.DataBean,BaseViewHolder> {


    public BookSourceAdapter(int layoutResId, @Nullable List<SourceBean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SourceBean.DataBean item) {
        helper.setText(R.id.tv_source_title, item.getSite_name())
                .setText(R.id.tv_source_content, item.getSite_url());
    }
}
