package com.justwayward.renren.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.justwayward.renren.R;
import com.justwayward.renren.ReaderApplication;
import com.justwayward.renren.base.BaseActivity;
import com.justwayward.renren.bean.RechargeRecordBean;
import com.justwayward.renren.component.AppComponent;
import com.justwayward.renren.retrofit.BaseObjObserver;
import com.justwayward.renren.retrofit.HttpResult;
import com.justwayward.renren.retrofit.RetrofitClient;
import com.justwayward.renren.retrofit.RxUtils;
import com.justwayward.renren.ui.adapter.RechargeDetailAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class RechargeDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;

    private List<RechargeRecordBean> mList = new ArrayList<>();
    private RechargeDetailAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_buy_history;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

        mAdapter = new RechargeDetailAdapter(R.layout.activity_recharge_detail, mList);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(mAdapter);
        refresh.setOnRefreshListener(this);
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
        mCommonToolbar.setTitle("充值明细");
    }

    @Override
    public void initDatas() {
        getRechargeDetail();
    }

    @Override
    public void configViews() {

    }


    private void getRechargeDetail() {
        RetrofitClient.getInstance().createApi().getPay(ReaderApplication.token,"recharge")
                .compose(RxUtils.<HttpResult<List<RechargeRecordBean>>>io_main())
                .subscribe(new BaseObjObserver<List<RechargeRecordBean>>(this,refresh) {
                    @Override
                    protected void onHandleSuccess(List<RechargeRecordBean> list) {
                        if (list == null || list.isEmpty()) {
                            return;
                        }
                        mList.clear();
                        mList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }


    @Override
    public void onRefresh() {
        getRechargeDetail();
    }
}
