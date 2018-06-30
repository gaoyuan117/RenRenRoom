package com.justwayward.renren.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.justwayward.renren.R;
import com.justwayward.renren.bean.CategoryListBean;
import com.justwayward.renren.bean.FreeListBean;

import java.util.List;

/**
 * Created by gaoyuan on 2018/3/3.
 */

public class FreeListAdapter extends BaseQuickAdapter<FreeListBean.DataBean, BaseViewHolder> {

    public FreeListAdapter(@LayoutRes int layoutResId, @Nullable List<FreeListBean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FreeListBean.DataBean item) {

        ImageView imgCover = helper.getView(R.id.ivSubCateCover);
        Glide.with(mContext).load(item.getPic()).error(R.drawable.cover_default).into(imgCover);
        String percent = "0";
        if (item.getCollect_num() != 0 && item.getView_num() != 0) {
            double d = (item.getCollect_num() * 100 / item.getView_num());
            percent = String.format("%.0f", d);
        }


        helper.setText(R.id.tvSubCateTitle, item.getTitle())
                .setText(R.id.tvSubCateAuthor, (item.getAuthor() == null ? "未知" : item.getAuthor()) + " | " + (TextUtils.isEmpty(item.getLabels()) ? "未知" : item.getCategory_name()))
                .setText(R.id.tvSubCateShort, item.getDesc()
                        .replaceAll("<p>", "")
                        .replaceAll("<br/>", "")
                        .replaceAll("</p>", ""))
                .setText(R.id.tvSubCateMsg, String.format(mContext.getResources().getString(R.string.category_book_msg),
                        item.getCollect_num(),
                        TextUtils.isEmpty(item.getCollect_num() + "") ? "0" : percent));
    }
}
