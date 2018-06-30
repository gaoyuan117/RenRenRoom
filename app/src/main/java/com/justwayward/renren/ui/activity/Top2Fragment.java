package com.justwayward.renren.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.justwayward.renren.R;
import com.justwayward.renren.base.BaseFragment;
import com.justwayward.renren.bean.CopyrightBean;
import com.justwayward.renren.bean.RankingList;
import com.justwayward.renren.bean.TopDetailBean;
import com.justwayward.renren.common.OnRvItemClickListener;
import com.justwayward.renren.component.AppComponent;
import com.justwayward.renren.retrofit.BaseObjObserver;
import com.justwayward.renren.retrofit.HttpResult;
import com.justwayward.renren.retrofit.RetrofitClient;
import com.justwayward.renren.retrofit.RxUtils;
import com.justwayward.renren.ui.adapter.TopDetailAdapter;
import com.justwayward.renren.ui.adapter.TopRankAdapter;
import com.justwayward.renren.view.CustomExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

public class Top2Fragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemClickListener{


    @Bind(R.id.elvMale)
    CustomExpandableListView elvMale;
    @Bind(R.id.tv_num)
    TextView tvNum;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    private String id;
    private Intent intent;
    private String type, title;

    private List<RankingList.MaleBean> maleGroups = new ArrayList<>();
    private List<List<RankingList.MaleBean>> maleChilds = new ArrayList<>();
    private TopRankAdapter maleAdapter;

    private List<TopDetailBean> mList = new ArrayList<>();
    private TopDetailAdapter mAdapter;
    private RankingList.MaleBean bean;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_top2;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        intent = new Intent(getActivity(), RankDetailActivity.class);
        id = getArguments().getString("id");
        mAdapter = new TopDetailAdapter(R.layout.item_sub_category_list, mList);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setAdapter(mAdapter);

        refresh.setOnRefreshListener(this);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void attachView() {
        maleAdapter = new TopRankAdapter(getActivity(), maleGroups, maleChilds);
        elvMale.setAdapter(maleAdapter);
        getCopyrightList();
        maleAdapter.setItemClickListener(new ClickListener());
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews() {

    }


    private void updateMale(List<String> list) {

        maleGroups.clear();
        maleChilds.clear();
        List<RankingList.MaleBean> list1 = new ArrayList<>();

        RankingList.MaleBean bean1 = new RankingList.MaleBean();
        bean1._id = "click";
        bean1.cover = R.mipmap.zuire + "";
        bean1.title = "最热榜";

        RankingList.MaleBean bean2 = new RankingList.MaleBean();
        bean2._id = "view";
        bean2.cover = R.mipmap.liucun + "";
        bean2.title = "留存榜";

        RankingList.MaleBean bean6 = new RankingList.MaleBean();
        bean6._id = "click";
        bean6.cover = R.mipmap.dianji + "";
        bean6.title = "点击榜";

        RankingList.MaleBean bean3 = new RankingList.MaleBean();
        bean3._id = "collect";
        bean3.cover = R.mipmap.shoucang + "";
        bean3.title = "收藏榜";

        RankingList.MaleBean bean4 = new RankingList.MaleBean();
        bean4._id = "word";
        bean4.cover = R.mipmap.zishu + "";
        bean4.title = "字数榜";

        RankingList.MaleBean bean5 = new RankingList.MaleBean();
        bean5._id = "finish";
        bean5.cover = R.mipmap.wanjiebang + "";
        bean5.title = "完结榜";

        maleGroups.add(bean1);
        maleGroups.add(bean2);
        maleGroups.add(bean6);
        maleGroups.add(bean3);
//        maleGroups.add(bean4);
//        maleGroups.add(bean5);
        maleGroups.add(new RankingList.MaleBean("其他榜"));


        for (int i = 0; i < list.size(); i++) {
            String site_name = list.get(i);
            int id = -1;

            RankingList.MaleBean bean = new RankingList.MaleBean();
            bean.title = site_name;
            bean._id = id + "";

            list1.add(bean);

        }


        maleChilds.add(new ArrayList<RankingList.MaleBean>());
        maleChilds.add(new ArrayList<RankingList.MaleBean>());
        maleChilds.add(new ArrayList<RankingList.MaleBean>());
        maleChilds.add(new ArrayList<RankingList.MaleBean>());
        maleChilds.add(new ArrayList<RankingList.MaleBean>());
        maleChilds.add(new ArrayList<RankingList.MaleBean>());
        maleChilds.add(list1);

        maleAdapter.notifyDataSetChanged();

    }

//    private void getSiteList() {
//        RetrofitClient.getInstance().createApi().getSiteList("")
//                .compose(RxUtils.<HttpResult<List<SiteListBean>>>io_main())
//                .subscribe(new BaseObjObserver<List<SiteListBean>>(getActivity()) {
//                    @Override
//                    protected void onHandleSuccess(List<SiteListBean> list) {
//                        updateMale(list);
//                    }
//                });
//    }

    private void getCopyrightList() {
        RetrofitClient.getInstance().createApi().getCopyrightList("")
                .compose(RxUtils.<CopyrightBean>io_main())
                .subscribe(new Consumer<CopyrightBean>() {
                    @Override
                    public void accept(CopyrightBean bean) throws Exception {
                        if (bean.getCode() == 200) {
                            updateMale(bean.getData());
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    class ClickListener implements OnRvItemClickListener<RankingList.MaleBean> {

        @Override
        public void onItemClick(View view, int position, RankingList.MaleBean data) {
//            if (TextUtils.isEmpty(data.cover)) {
//
//                intent.putExtra("id", id);
//                intent.putExtra("site_id", data._id);
//                intent.putExtra("title", data.title);
//                intent.putExtra("copyright", data.title);
//                startActivity(intent);
//            } else {
//
//                intent.putExtra("id", id);
//                intent.putExtra("type", data._id);
//                intent.putExtra("title", data.title);
//                startActivity(intent);
//            }

            getRankList();

            bean = data;
        }
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

        if (!TextUtils.isEmpty(bean._id)&&!bean._id.equals("-1")) {
            map.put("site_id", bean._id);
        }

        if (!TextUtils.isEmpty(bean.title)) {
            map.put("copyright", bean.title);
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
