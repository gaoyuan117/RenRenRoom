package com.justwayward.renren.ui.activity;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.justwayward.renren.R;
import com.justwayward.renren.base.BaseActivity;
import com.justwayward.renren.bean.CategoryListBean;
import com.justwayward.renren.component.AppComponent;
import com.justwayward.renren.retrofit.BaseObjObserver;
import com.justwayward.renren.retrofit.HttpResult;
import com.justwayward.renren.retrofit.RetrofitClient;
import com.justwayward.renren.retrofit.RxUtils;
import com.justwayward.renren.ui.adapter.CategoryAdapter;
import com.justwayward.renren.view.recyclerview.decoration.DividerDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

public class CommonActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;

    private List<CategoryListBean.DataBean> mList = new ArrayList<>();
    private CategoryAdapter mAdapter;

    private String type, id, title;
    private int page = 1;
    private String category_id;

    @Override
    public int getLayoutId() {
        return R.layout.activity_buy_history;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        type = getIntent().getStringExtra("type");
        id = getIntent().getStringExtra("id");
        category_id = getIntent().getStringExtra("category_id");
        title = getIntent().getStringExtra("title");

        mAdapter = new CategoryAdapter(R.layout.item_sub_category_list, mList);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnLoadMoreListener(this, recyclerview);

        DividerDecoration itemDecoration = new DividerDecoration(ContextCompat.getColor(getActivity(), R.color.common_divider_narrow), 1, 0, 0);
        itemDecoration.setDrawLastItem(false);
        recyclerview.addItemDecoration(itemDecoration);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setAdapter(mAdapter);


        refresh.setOnRefreshListener(this);

    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setTitle(title);
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
    }

    @Override
    public void initDatas() {
        getNovelByCategory();
    }

    @Override
    public void configViews() {

    }

    private void getNovelByCategory() {
        Map<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(type)) {
            map.put(type, 1);
        }
         if (!TextUtils.isEmpty(id)) {
            map.put("category_id", id);
        }


        RetrofitClient.getInstance().createApi().getNovelByCategory(map, page)
                .compose(RxUtils.<HttpResult<CategoryListBean>>io_main())
                .subscribe(new BaseObjObserver<CategoryListBean>(this, refresh, mAdapter) {
                    @Override
                    protected void onHandleSuccess(CategoryListBean categoryListBean) {
                        if (page == 1) {
                            mList.clear();
                        }
                        if (categoryListBean.getData()==null||categoryListBean.getData().size()==0) {
                            mAdapter.loadMoreEnd();
                            mAdapter.notifyDataSetChanged();
                            return;
                        }
                        mList.addAll(categoryListBean.getData());
                        mAdapter.notifyDataSetChanged();
                        mAdapter.loadMoreComplete();
                    }
                });
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        BookDetailActivity.startActivity(getActivity(), mList.get(position).getId() + "");
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        getNovelByCategory();
    }

    @Override
    public void onRefresh() {
        page = 1;
        getNovelByCategory();
    }
}
