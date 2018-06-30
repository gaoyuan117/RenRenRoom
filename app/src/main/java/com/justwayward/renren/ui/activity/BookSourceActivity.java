/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.justwayward.renren.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.justwayward.renren.R;
import com.justwayward.renren.base.BaseActivity;
import com.justwayward.renren.bean.BookSource;
import com.justwayward.renren.bean.SourceBean;
import com.justwayward.renren.component.AppComponent;
import com.justwayward.renren.component.DaggerBookComponent;
import com.justwayward.renren.retrofit.BaseObjObserver;
import com.justwayward.renren.retrofit.HttpResult;
import com.justwayward.renren.retrofit.RetrofitClient;
import com.justwayward.renren.retrofit.RxUtils;
import com.justwayward.renren.ui.contract.BookSourceContract;
import com.justwayward.renren.ui.easyadapter.BookSourceAdapter;
import com.justwayward.renren.view.recyclerview.decoration.DividerDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author yuyh.
 * @date 2016/9/8.
 */
public class BookSourceActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemClickListener {

    public static final String INTENT_BOOK_ID = "bookId";
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;

    private BookSourceAdapter adapter;
    private View headView;

    private List<SourceBean.DataBean> mList = new ArrayList<>();

    public static void start(Activity activity, String bookId, String chapter, int reqId) {
        activity.startActivityForResult(new Intent(activity, BookSourceActivity.class)
                .putExtra(INTENT_BOOK_ID, bookId).putExtra("chapter", chapter), reqId);
    }


    private String bookId, chapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_common_recyclerview;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerBookComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void initToolBar() {
        headView = View.inflate(this, R.layout.head_book_source, null);
        bookId = getIntent().getStringExtra(INTENT_BOOK_ID);
        chapter = getIntent().getStringExtra("chapter");
        mCommonToolbar.setTitle("选择来源");
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);

        adapter = new BookSourceAdapter(R.layout.item_book_source, mList);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        DividerDecoration itemDecoration = new DividerDecoration(ContextCompat.getColor(getActivity(), R.color.common_divider_narrow), 1, 0, 0);
        recyclerview.addItemDecoration(itemDecoration);
        recyclerview.setAdapter(adapter);
        refresh.setOnRefreshListener(this);
        adapter.setOnItemClickListener(this);

    }

    @Override
    public void initDatas() {
    }

    @Override
    public void configViews() {
        getSource();
    }

    private void getSource() {

        RetrofitClient.getInstance().createApi().getSource(bookId, chapter)
                .compose(RxUtils.<HttpResult<SourceBean>>io_main())
                .subscribe(new BaseObjObserver<SourceBean>(this) {
                    @Override
                    protected void onHandleSuccess(SourceBean sourceBean) {
                        if (sourceBean.getData() == null || sourceBean.getData().size() == 0)
                            return;

                        mList.clear();
                        mList.addAll(sourceBean.getData());
                        adapter.notifyDataSetChanged();

                        for (int i = 0; i < sourceBean.getData().size(); i++) {
                            SourceBean.DataBean data = sourceBean.getData().get(i);
                            if (data.getIs_copyright() == 1) {
                                adapter.removeAllHeaderView();
                                adapter.addHeaderView(headView);
                                TextView tvSource = (TextView) headView.findViewById(R.id.tv_source_content);
                                TextView tvSourceNum = (TextView) headView.findViewById(R.id.tv_source_num);
                                tvSource.setText(data.getSite_url());
                                tvSourceNum.setText("共搜索到" + sourceBean.getData().size() + "个网站");
                            }
                        }

                    }
                });
    }


    @Override
    public void onRefresh() {
        getSource();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        SourceBean.DataBean bean = mList.get(position);
        Intent intent = new Intent();
        intent.putExtra("source", bean.getId() + "");
        setResult(RESULT_OK, intent);
        finish();
    }
}
