package com.justwayward.renren.ui.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.justwayward.renren.R;
import com.justwayward.renren.api.CoomonApi;
import com.justwayward.renren.base.BaseActivity;
import com.justwayward.renren.bean.ReviewBean;
import com.justwayward.renren.component.AppComponent;
import com.justwayward.renren.retrofit.BaseObjObserver;
import com.justwayward.renren.retrofit.HttpResult;
import com.justwayward.renren.retrofit.RetrofitClient;
import com.justwayward.renren.retrofit.RxUtils;
import com.justwayward.renren.ui.adapter.AllReviewAdapter;
import com.justwayward.renren.view.recyclerview.decoration.DividerDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class AllReviewActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemClickListener {

    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;

    private List<ReviewBean.DataBean> mList = new ArrayList<>();
    private AllReviewAdapter mAdapter;
    private int page = 1;
    private String novelId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_all_review;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        novelId = getIntent().getStringExtra("id");
        if (TextUtils.isEmpty(novelId)) {
            finish();
            return;
        }
        mAdapter = new AllReviewAdapter(R.layout.item_book_detai_hot_review_list, mList);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(this, recyclerview);
        mAdapter.setOnItemClickListener(this);
        mAdapter.disableLoadMoreIfNotFullPage();
        refresh.setOnRefreshListener(this);

        DividerDecoration itemDecoration = new DividerDecoration(ContextCompat.getColor(this, R.color.common_divider_narrow), 1, 0, 0);
        itemDecoration.setDrawLastItem(false);
        recyclerview.addItemDecoration(itemDecoration);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(mAdapter);
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
        mCommonToolbar.setTitle("全部书评");
    }

    @Override
    public void initDatas() {
    }

    @Override
    public void configViews() {

    }


    @Override
    public void onLoadMoreRequested() {
        page++;
        getReviewList();
    }

    @Override
    public void onRefresh() {
        page = 1;
        getReviewList();
    }

    @OnClick(R.id.tv_all_review_edit)
    public void onViewClicked() {
        if (!CoomonApi.isLogin(this)) {

            return;
        }
        Intent intent = new Intent(this, EditReviewActivity.class);
        intent.putExtra("id", novelId);
        startActivity(intent);
    }

    /**
     * 获取全部书评
     */
    private void getReviewList() {
        RetrofitClient.getInstance().createApi().getReviewList(novelId, page)
                .compose(RxUtils.<HttpResult<ReviewBean>>io_main())
                .subscribe(new BaseObjObserver<ReviewBean>(this, refresh, mAdapter) {
                    @Override
                    protected void onHandleSuccess(ReviewBean reviewBean) {
                        if (page == 1) {
                            mList.clear();
                        }

                        if (reviewBean.getData().isEmpty()) {
                            mAdapter.loadMoreEnd();
                            mAdapter.notifyDataSetChanged();
                            return;
                        }

                        mList.addAll(reviewBean.getData());
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getReviewList();

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(this, ReviewDetailActivity.class);
        intent.putExtra("id", mList.get(position).getId() + "");
        intent.putExtra("novelId", novelId);
        startActivity(intent);
    }


}
