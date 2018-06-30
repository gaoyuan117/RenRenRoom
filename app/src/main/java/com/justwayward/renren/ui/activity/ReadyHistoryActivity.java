package com.justwayward.renren.ui.activity;

import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.justwayward.renren.R;
import com.justwayward.renren.ReaderApplication;
import com.justwayward.renren.base.BaseActivity;
import com.justwayward.renren.bean.HistoryBean;
import com.justwayward.renren.bean.HistoryBeanDao;
import com.justwayward.renren.component.AppComponent;
import com.justwayward.renren.manager.SettingManager;
import com.justwayward.renren.ui.adapter.ReadyHistoryAdapter;
import com.justwayward.renren.utils.LogUtils;
import com.justwayward.renren.view.recyclerview.decoration.DividerDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class ReadyHistoryActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.tv_clear)
    TextView tvClear;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refreshLayout;

    private ReadyHistoryAdapter mAdapter;
    private HistoryBeanDao historyBeanDao;
    private List<HistoryBean> mList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_ready_history;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        View view = View.inflate(this, R.layout.empty_history, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_empty_content);
        textView.setText("暂无最近阅读记录");
        historyBeanDao = ReaderApplication.getDaoInstant().getHistoryBeanDao();

        mAdapter = new ReadyHistoryAdapter(R.layout.item_ready_history, mList);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setAdapter(mAdapter);
        DividerDecoration itemDecoration = new DividerDecoration(ContextCompat.getColor(getActivity(), R.color.common_divider_narrow), 1, 0, 0);
        itemDecoration.setDrawLastItem(false);
        recyclerview.addItemDecoration(itemDecoration);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setEmptyView(view);

        refreshLayout.setOnRefreshListener(this);
        loadHistory();

    }

    private void loadHistory() {
        mList.clear();
        List<HistoryBean> list = this.historyBeanDao.queryBuilder().list();
        if (list != null && list.size() > 0) {
            Collections.reverse(list);
            mList.addAll(list);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void initToolBar() {
    }

    @Override
    public void initDatas() {
    }

    @Override
    public void configViews() {

    }

    @OnClick({R.id.img_back, R.id.tv_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_clear:
                if (mList == null || mList.isEmpty()) {
                    return;
                }
                showClearDialog();
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        HistoryBean bean = mList.get(position);
        ReadActivity.startActivity(this, bean.getTitle(), bean.getBookId(), bean.getIsShelf(), bean.getPic(), bean.getPic(), bean.getDes());

    }


    /**
     * 清空对话框
     */
    private void showClearDialog() {
        new AlertDialog.Builder(this)
                .setTitle("清空阅览记录")
                .setMessage("确定要清除吗，清除后对应书籍将从第一节开始阅读")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        for (int i = 0; i < mList.size(); i++) {
                            LogUtils.e("位置："+mList.get(i).getBookId());
                            SettingManager.getInstance().removeReadProgress(mList.get(i).getBookId());
                        }
                        historyBeanDao.deleteAll();
                        mList.clear();
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .create().show();
    }

    @Override
    public void onRefresh() {
        loadHistory();
    }
}
