package com.justwayward.renren.ui.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.justwayward.renren.R;
import com.justwayward.renren.api.CoomonApi;
import com.justwayward.renren.bean.BookCityBean;
import com.justwayward.renren.ui.activity.BookDetailActivity;
import com.justwayward.renren.utils.LogUtils;
import com.justwayward.renren.view.MyGridView;

import java.util.List;

/**
 * Created by gaoyuan on 2017/11/17.
 */

public class HomeAdapter extends BaseMultiItemQuickAdapter<BookCityBean, BaseViewHolder> {

    public HomeAdapter(List<BookCityBean> data) {
        super(data);
        addItemType(1, R.layout.item_book_city_home);
        addItemType(2, R.layout.item_book_city_home2);
    }

    @Override
    protected void convert(BaseViewHolder helper, BookCityBean item) {
        switch (helper.getItemViewType()) {
            case 1:
                initType1Data(helper, item);
                break;

            case 2:
                initType2Data(helper, item);
                break;
        }

    }

    private void initType1Data(BaseViewHolder helper, final BookCityBean item) {
        String s = item.getType();
        helper.setText(R.id.tv_type, s);
        LinearLayout layout = helper.getView(R.id.ll_more);
        int ids = item.getCategory_id();
        if (ids==0) {
            CoomonApi.check(mContext, layout, item.getType(), "");
        } else {
            CoomonApi.check(mContext, layout, item.getType(), "-"+ids);
        }

        MyGridView gridView = helper.getView(R.id.grid_view);

        BookCityHomeItemAdapter adapter = new BookCityHomeItemAdapter(mContext, item.getList());
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BookDetailActivity.startActivity(mContext, item.getList().get(i).getId() + "");
            }
        });
    }

    private void initType2Data(BaseViewHolder helper, final BookCityBean item) {
        String s = item.getType();
        LinearLayout layout = helper.getView(R.id.ll_more2);
        helper.setText(R.id.tv_type2, s);
        int ids = item.getCategory_id();
        if (ids==0) {
            CoomonApi.check(mContext, layout, item.getType(), "");
        } else {
            CoomonApi.check(mContext, layout, item.getType(), "-"+ids);
        }
        MyGridView gridView = helper.getView(R.id.grid_view2);

        BookCityHomeItem2Adapter adapter = new BookCityHomeItem2Adapter(mContext, item.getList());
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BookDetailActivity.startActivity(mContext, item.getList().get(i).getId() + "");
            }
        });
    }

}
