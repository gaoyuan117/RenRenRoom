package com.justwayward.renren.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.justwayward.renren.R;
import com.justwayward.renren.bean.MonthPackageBean;
import com.justwayward.renren.ui.activity.MyVipActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gaoyuan on 2017/11/21.
 */

public class MyVipPayAdapter extends BaseAdapter {
    private Context context;
    private List<MonthPackageBean> mList;
    private boolean isSelect = false;
    MyVipActivity activity;
    private List<CheckBox> list = new ArrayList<>();

    public MyVipPayAdapter(Context context, List<MonthPackageBean> mList, MyVipActivity activity) {
        this.context = context;
        this.mList = mList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            view = View.inflate(context, R.layout.item_my_pay_vip, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
            list.add(holder.cb);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        MonthPackageBean bean = mList.get(i);

        holder.tvTitle.setText(bean.getMonth() + "个月会员 " + bean.getMoney());
        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.check(holder.cb, i);
            }
        });


        return view;
    }

    public void setAllFalse() {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setChecked(false);
        }
    }

    static class ViewHolder {
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.cb_wx_pay)
        CheckBox cb;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
