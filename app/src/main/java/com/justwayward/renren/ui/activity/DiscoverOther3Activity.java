package com.justwayward.renren.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.justwayward.renren.R;
import com.justwayward.renren.api.CoomonApi;
import com.justwayward.renren.base.BaseActivity;
import com.justwayward.renren.bean.SubZoneBean;
import com.justwayward.renren.component.AppComponent;
import com.justwayward.renren.ui.adapter.DiscoverOther2Adapter;
import com.justwayward.renren.ui.adapter.DiscoverOther3Adapter;

import java.util.List;

import butterknife.Bind;

public class DiscoverOther3Activity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {

    @Bind(R.id.common_toolbar)
    Toolbar commonToolbar;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    private String type;

    private List<SubZoneBean.SubBeanX> mList;
    private SubZoneBean.SubBeanX.SubBean bean;
    private DiscoverOther3Adapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_discover_other2;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initToolBar() {
        type = getIntent().getStringExtra("type");
        mCommonToolbar.setTitle(type);
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
    }

    @Override
    public void initDatas() {

        mList = (List<SubZoneBean.SubBeanX>) getIntent().getSerializableExtra("data");

        mAdapter = new DiscoverOther3Adapter(R.layout.item_discover_other2, mList);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void configViews() {

    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        CoomonApi.toBrowser(this,mList.get(position).getZone_link());
    }
}
