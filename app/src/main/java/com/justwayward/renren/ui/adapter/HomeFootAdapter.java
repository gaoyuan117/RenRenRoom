package com.justwayward.renren.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.justwayward.renren.R;
import com.justwayward.renren.api.CoomonApi;
import com.justwayward.renren.bean.BannerBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gaoyuan on 2017/12/27.
 */

public class HomeFootAdapter extends BaseAdapter {

    private List<BannerBean> list;
    private Context context;

    public HomeFootAdapter(List<BannerBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();

    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.foot_item, null);
        ViewHolder viewHolder = new ViewHolder(convertView);

        Glide.with(context).load(list.get(position).getImage_url()).error(R.drawable.cover_default).into(viewHolder.footImg);

        viewHolder.footImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoomonApi.toBrowser(context,list.get(position).getUrl());
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.foot_img)
        ImageView footImg;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
