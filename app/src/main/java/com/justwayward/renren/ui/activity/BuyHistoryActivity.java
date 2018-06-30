package com.justwayward.renren.ui.activity;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.justwayward.renren.R;
import com.justwayward.renren.ReaderApplication;
import com.justwayward.renren.base.BaseActivity;
import com.justwayward.renren.bean.BuyHistoryBean;
import com.justwayward.renren.bean.RechargeRecordBean;
import com.justwayward.renren.component.AppComponent;
import com.justwayward.renren.retrofit.BaseObjObserver;
import com.justwayward.renren.retrofit.HttpResult;
import com.justwayward.renren.retrofit.RetrofitClient;
import com.justwayward.renren.retrofit.RxUtils;
import com.justwayward.renren.ui.adapter.BuyHistoryAdapter;
import com.justwayward.renren.view.recyclerview.decoration.DividerDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;

public class BuyHistoryActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.tv_num)
    TextView tvNum;

    private List<BuyHistoryBean.DataBean> mList = new ArrayList<>();
    private BuyHistoryAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_buy_history;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        mAdapter = new BuyHistoryAdapter(R.layout.item_buy_history, mList);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerDecoration itemDecoration = new DividerDecoration(ContextCompat.getColor(getActivity(), R.color.common_divider_narrow), 1, 0, 0);
        itemDecoration.setDrawLastItem(false);
        recyclerview.addItemDecoration(itemDecoration);
        recyclerview.setAdapter(mAdapter);
        refresh.setOnRefreshListener(this);
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setTitle("购买记录");
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
    }

    @Override
    public void initDatas() {
        getHistory();
    }

    @Override
    public void configViews() {

    }

    private void getHistory() {
        RetrofitClient.getInstance().createApi().getCoinLog(ReaderApplication.token)
                .compose(RxUtils.<HttpResult<BuyHistoryBean>>io_main())
                .subscribe(new BaseObjObserver<BuyHistoryBean>(this, "获取中") {
                    @Override
                    protected void onHandleSuccess(BuyHistoryBean bean) {
                        mList.clear();

                        if (bean.getData() == null || bean.getData().size() == 0) {
                            tvNum.setVisibility(View.GONE);
                            mAdapter.notifyDataSetChanged();
                            getRechargeDetail();
                            return;
                        }

                        mList.addAll(bean.getData());
                        getRechargeDetail();

                    }
                });
    }

    private void getRechargeDetail() {
        RetrofitClient.getInstance().createApi().getPay(ReaderApplication.token, "member")
                .compose(RxUtils.<HttpResult<List<RechargeRecordBean>>>io_main())
                .subscribe(new BaseObjObserver<List<RechargeRecordBean>>(this, refresh) {
                    @Override
                    protected void onHandleSuccess(List<RechargeRecordBean> list) {
                        if (list == null || list.isEmpty()) {
                            return;
                        }

                        for (int i = 0; i < list.size(); i++) {
                            for (int i1 = 0; i1 < list.get(i).getList().size(); i1++) {
                                BuyHistoryBean.DataBean bean = new BuyHistoryBean.DataBean();
                                bean.setAdd_time(list.get(i).getList().get(i1).getAdd_time());
                                int status = list.get(i).getList().get(i1).getStatus();
                                if (status == 0) {
                                    bean.setNote("包月:网络小说免费畅读(支付失败)");
                                } else {
                                    bean.setNote("包月:网络小说免费畅读(支付成功)");
                                }
                                bean.setType("member");
                                bean.setStatus(list.get(i).getList().get(i1).getStatus());

                                mList.add(bean);
                            }
                        }

                        Collections.sort(mList);
                        Collections.reverse(mList);

                        tvNum.setVisibility(View.VISIBLE);
                        mAdapter.notifyDataSetChanged();

                        tvNum.setText("共" + mList.size() + "条记录");

                    }
                });
    }

    @Override
    public void onRefresh() {
        getHistory();
    }
}
