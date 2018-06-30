package com.justwayward.renren.ui.activity;

import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.justwayward.renren.R;
import com.justwayward.renren.base.BaseActivity;
import com.justwayward.renren.bean.CategoryBean;
import com.justwayward.renren.bean.CategoryListBean;
import com.justwayward.renren.component.AppComponent;
import com.justwayward.renren.retrofit.BaseObjObserver;
import com.justwayward.renren.retrofit.HttpResult;
import com.justwayward.renren.retrofit.RetrofitClient;
import com.justwayward.renren.retrofit.RxUtils;
import com.justwayward.renren.ui.adapter.CategoryAdapter;
import com.justwayward.renren.ui.adapter.ShaiXuanAdapter;
import com.justwayward.renren.ui.adapter.ZdRvAdapter;
import com.justwayward.renren.ui.fragment.BookCityFragment;
import com.justwayward.renren.utils.LogUtils;
import com.justwayward.renren.view.recyclerview.decoration.DividerDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

public class WordNumRangeActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;

    private List<CategoryListBean.DataBean> mList = new ArrayList<>();
    private CategoryAdapter mAdapter;

    private String range, title;
    private int page = 1;
    private MenuItem searchMenuItem;
    private ShaiXuanAdapter adapter;
    private PopupWindow window;
    private List<CategoryBean> categoryList;
    private int zdPosition = 0;
    private int flPosition = 0;
    private RecyclerView rvZd;
    private RecyclerView rvFl;
    private LinearLayout layout2;
    private List<String> list1;
    private List<String> list2;
    private String id = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_buy_history;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        categoryList = BookCityFragment.categoryList;
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();

        range = getIntent().getStringExtra("range");
        title = getIntent().getStringExtra("title");

        mAdapter = new CategoryAdapter(R.layout.item_sub_category_list, mList);

        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnLoadMoreListener(this, recyclerview);
//        mAdapter.disableLoadMoreIfNotFullPage();

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
        getNovelByCategory(id);
    }

    @Override
    public void configViews() {

    }

    private void getNovelByCategory(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("word_num_range", range);
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
                        if (categoryListBean.getData() == null || categoryListBean.getData().size() == 0) {
                            mAdapter.loadMoreEnd();

                            if (page == 1) {
                                mList.clear();
                            }
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
        LogUtils.e("页数：" + page);
        getNovelByCategory(id);
    }

    @Override
    public void onRefresh() {
        page = 1;
        getNovelByCategory(id);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.word_num_menu, menu);

        searchMenuItem = menu.findItem(R.id.shaixuan);//在菜单中找到对应控件的item

        searchMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                setBackgroundAlpha(0.8f);
                showPop();
                return true;
            }
        });

        return true;
    }


    private void showPop() {
        View view = View.inflate(this, R.layout.pop_word_num, null);
        window = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setFocusable(true);
        window.showAsDropDown(mCommonToolbar);

        rvZd = (RecyclerView) view.findViewById(R.id.zd_rv);
        rvFl = (RecyclerView) view.findViewById(R.id.fl_rv);
        layout2 = (LinearLayout) view.findViewById(R.id.layout2);

        if (zdPosition == 0) {
            layout2.setVisibility(View.GONE);
        } else {
            layout2.setVisibility(View.VISIBLE);
        }

        list1.clear();
        list2.clear();
        list1.add("全部");
        list2.add("全部");


        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).getCategory().equals("字数")) continue;
            list1.add(categoryList.get(i).getCategory());

        }

        final ZdRvAdapter adapter1 = new ZdRvAdapter(R.layout.item_word_num_select, list1);
        final ZdRvAdapter adapter2 = new ZdRvAdapter(R.layout.item_word_num_select, list2);

        rvZd.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvZd.setAdapter(adapter1);
        adapter1.setSelect(zdPosition);
        adapter1.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

//                if (zdPosition == position) return;

                page = 1;
                zdPosition = position;
                flPosition = 0;
                adapter1.setSelect(position);

                if (position == 0) {
                    layout2.setVisibility(View.GONE);
                } else {
                    layout2.setVisibility(View.VISIBLE);
                    list2.clear();
                    list2.add("全部");
                    try {
                        for (int i = 0; i < categoryList.get(zdPosition).getSub_category().size(); i++) {
                            list2.add(categoryList.get(zdPosition).getSub_category().get(i).getCategory());
                        }
                    } catch (Exception e) {

                    }

                    adapter2.notifyDataSetChanged();
                }

                getNovelByCategory(categoryList.get(zdPosition).getId() + "");

            }
        });

        if (zdPosition > 0) {
            for (int i = 0; i < categoryList.get(zdPosition).getSub_category().size(); i++) {
                list2.add(categoryList.get(zdPosition).getSub_category().get(i).getCategory());
            }
        }


//        rvFl.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvFl.setLayoutManager(new GridLayoutManager(this,5));
        rvFl.setAdapter(adapter2);
        adapter2.setSelect(flPosition);
        adapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                flPosition = position;

                page = 1;
                if (position == 0) {
                    getNovelByCategory(categoryList.get(zdPosition).getId() + "");
                } else {
                    getNovelByCategory(categoryList.get(zdPosition).getSub_category().get(position - 1).getId() + "");
                }

                adapter2.setSelect(position);

            }
        });


        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (this != null) {
                    setBackgroundAlpha(1f);
                }
            }
        });
    }

    /**
     * 设置页面的透明度
     *
     * @param bgAlpha 1表示不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bu
        }
        getWindow().setAttributes(lp);
    }

}
