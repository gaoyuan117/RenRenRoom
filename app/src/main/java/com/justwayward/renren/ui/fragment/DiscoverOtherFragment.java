package com.justwayward.renren.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.justwayward.renren.R;
import com.justwayward.renren.api.CoomonApi;
import com.justwayward.renren.base.BaseFragment;
import com.justwayward.renren.bean.SubZoneBean;
import com.justwayward.renren.component.AppComponent;
import com.justwayward.renren.ui.activity.DiscoverOther2Activity;
import com.justwayward.renren.ui.adapter.DiscoverOtherAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by gaoyuan on 2017/11/21.
 */

public class DiscoverOtherFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;

    private List<String> mList = new ArrayList<>();
    private DiscoverOtherAdapter mAdapter;
    private SubZoneBean bean;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_book_city_home;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

        bean = (SubZoneBean) getArguments().getSerializable("data");

        mAdapter = new DiscoverOtherAdapter(R.layout.item_discover_other, bean.getSub());
        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerview.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);
        refresh.setEnabled(false);
    }

    @Override
    public void attachView() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews() {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        if (bean.getSub().get(position).getSub() != null && bean.getSub().get(position).getSub().size() > 0) {
            Intent intent = new Intent(activity, DiscoverOther2Activity.class);
            intent.putExtra("type", bean.getSub().get(position).getZone_name());
            intent.putExtra("data", (ArrayList) bean.getSub().get(position).getSub());
            startActivity(intent);
        } else {
            CoomonApi.toBrowser(activity, bean.getSub().get(position).getZone_link());

        }
    }

    @Override
    public void onRefresh() {

    }
}
