package com.justwayward.renren.ui.fragment;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.justwayward.renren.R;
import com.justwayward.renren.base.BaseFragment;
import com.justwayward.renren.bean.CategoryListBean;
import com.justwayward.renren.bean.FreeListBean;
import com.justwayward.renren.component.AppComponent;
import com.justwayward.renren.retrofit.BaseObjObserver;
import com.justwayward.renren.retrofit.HttpResult;
import com.justwayward.renren.retrofit.RetrofitClient;
import com.justwayward.renren.retrofit.RxUtils;
import com.justwayward.renren.ui.activity.BookDetailActivity;
import com.justwayward.renren.ui.adapter.CategoryAdapter;
import com.justwayward.renren.ui.adapter.FreeListAdapter;
import com.justwayward.renren.view.recyclerview.decoration.DividerDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by gaoyuan on 2018/3/3.
 */

public class FreeListFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;

    private List<FreeListBean.DataBean> mList = new ArrayList<>();
    private FreeListAdapter mAdapter;
    private String id;
    private String type;
    private int page = 1;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_book_city_home;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

        mAdapter = new FreeListAdapter(R.layout.item_sub_category_list, mList);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnLoadMoreListener(this, recyclerview);
        mAdapter.disableLoadMoreIfNotFullPage();

        DividerDecoration itemDecoration = new DividerDecoration(ContextCompat.getColor(getActivity(), R.color.common_divider_narrow), 1, 0, 0);
        itemDecoration.setDrawLastItem(false);
        recyclerview.addItemDecoration(itemDecoration);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setAdapter(mAdapter);


        refresh.setOnRefreshListener(this);

    }

    @Override
    public void attachView() {
        type = getArguments().getString("type");
    }

    @Override
    public void initDatas() {
        getLimitTimeFree();
    }

    @Override
    public void configViews() {

    }


    private void getLimitTimeFree() {
        RetrofitClient.getInstance().createApi().getLimitTimeFree("")
                .compose(RxUtils.<HttpResult<List<FreeListBean>>>io_main())
                .subscribe(new BaseObjObserver<List<FreeListBean>>(activity, "获取中") {
                    @Override
                    protected void onHandleSuccess(List<FreeListBean> freeListBeans) {
                        if (freeListBeans == null || freeListBeans.size() == 0) return;
                        mList.clear();
                        for (int i = 0; i < freeListBeans.size(); i++) {
                            if (freeListBeans.get(i).getCategory_id().equals(type)) {
                                mList.addAll(freeListBeans.get(i).getData());
                                mAdapter.notifyDataSetChanged();
                            }
                        }
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
        getLimitTimeFree();
    }

    @Override
    public void onRefresh() {
        page = 1;
        getLimitTimeFree();
    }
}
