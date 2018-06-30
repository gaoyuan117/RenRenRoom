package com.justwayward.renren.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.justwayward.renren.R;
import com.justwayward.renren.bean.support.ReadTheme;
import com.justwayward.renren.manager.ThemeManager;
import com.justwayward.renren.utils.LogUtils;

import java.util.List;

/**
 * Created by gaoyuan on 2018/2/24.
 */

public class ThemeAdapter extends BaseQuickAdapter<ReadTheme, BaseViewHolder> {

    private int selected = 0;

    public ThemeAdapter(int layoutResId, @Nullable List<ReadTheme> data, int selected) {
        super(layoutResId, data);
        this.selected = selected;
    }

    @Override
    protected void convert(BaseViewHolder helper, ReadTheme readTheme) {
        ThemeManager.setReaderTheme(readTheme.theme, helper.getView(R.id.ivThemeBg));
        if (selected == helper.getPosition()) {
            helper.setVisible(R.id.ivSelected, true);
        } else {
            helper.setVisible(R.id.ivSelected, false);
        }
    }

    public void select(int position) {
        selected = position;
        LogUtils.i("curtheme=" + selected);
        notifyDataSetChanged();
    }
}
