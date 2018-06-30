package com.justwayward.renren.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.justwayward.renren.R;
import com.justwayward.renren.base.BaseActivity;
import com.justwayward.renren.bean.TopDetailBean;
import com.justwayward.renren.component.AppComponent;
import com.justwayward.renren.retrofit.BaseObjObserver;
import com.justwayward.renren.retrofit.HttpResult;
import com.justwayward.renren.retrofit.RetrofitClient;
import com.justwayward.renren.retrofit.RxUtils;
import com.justwayward.renren.ui.adapter.TopDetailAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

public class RankDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemClickListener {


    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;

    private List<TopDetailBean> mList = new ArrayList<>();
    private TopDetailAdapter mAdapter;
    private String title;
    private String type;
    private String id, site_id,copyright;

    @Override
    public int getLayoutId() {
        return R.layout.activity_buy_history;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        title = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        id = getIntent().getStringExtra("id");
        site_id = getIntent().getStringExtra("site_id");
        copyright = getIntent().getStringExtra("copyright");

        mAdapter = new TopDetailAdapter(R.layout.item_sub_category_list, mList);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setAdapter(mAdapter);

        refresh.setOnRefreshListener(this);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
        mCommonToolbar.setTitle(title);
    }

    @Override
    public void initDatas() {
        getRankList(type, id);
    }

    @Override
    public void configViews() {

    }


    @Override
    public void onRefresh() {
        getRankList(type, id);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        BookDetailActivity.startActivity(getActivity(), mList.get(position).getId() + "");
    }

    private void getRankList(String type, String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("category_id", id);

        if (!TextUtils.isEmpty(type)) {
            map.put("order_type", type);
        }

        if (!TextUtils.isEmpty(site_id)&&!site_id.equals("-1")) {
            map.put("site_id", site_id);
        }

        if (!TextUtils.isEmpty(copyright)) {
            map.put("copyright", copyright);
        }

        RetrofitClient.getInstance().createApi().getRankList(map)
                .compose(RxUtils.<HttpResult<List<TopDetailBean>>>io_main())
                .subscribe(new BaseObjObserver<List<TopDetailBean>>(getActivity(), refresh) {
                    @Override
                    protected void onHandleSuccess(List<TopDetailBean> list) {
                        if (list == null || list.isEmpty()) {
                            return;
                        }
                        mList.clear();
                        mList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

}
