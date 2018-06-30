package com.justwayward.renren.ui.fragment;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.justwayward.renren.R;
import com.justwayward.renren.api.CoomonApi;
import com.justwayward.renren.base.BaseFragment;
import com.justwayward.renren.bean.BannerBean;
import com.justwayward.renren.bean.SubZoneBean;
import com.justwayward.renren.component.AppComponent;
import com.justwayward.renren.retrofit.BaseObjObserver;
import com.justwayward.renren.retrofit.HttpResult;
import com.justwayward.renren.retrofit.RetrofitClient;
import com.justwayward.renren.retrofit.RxUtils;
import com.justwayward.renren.ui.DiscoverOtherActivity;
import com.justwayward.renren.ui.activity.BookDetailActivity;
import com.justwayward.renren.ui.activity.DiscoverOther2Activity;
import com.justwayward.renren.ui.activity.DiscoverOther3Activity;
import com.justwayward.renren.ui.activity.SearchActivity;
import com.justwayward.renren.ui.activity.TopActivity;
import com.justwayward.renren.ui.activity.TopCategoryListActivity;
import com.justwayward.renren.ui.adapter.DiscoverAdapter;
import com.justwayward.renren.utils.MyImageLoader;
import com.justwayward.renren.view.recyclerview.decoration.DividerDecoration;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by gaoyuan on 2017/11/20.
 */

public class DiscoverFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, OnBannerClickListener {

    @Bind(R.id.img_recommend_search)
    ImageView imgRecommendSearch;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refreshLayout;

    private View headView;
    private Banner mBanner;
    private List<String> bannerList = new ArrayList<>();
    private List<SubZoneBean> mList = new ArrayList<>();
    private DiscoverAdapter mAdapter;
    private List<BannerBean> bannerBeens;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_discover;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        headView = LayoutInflater.from(activity).inflate(R.layout.layout_banner, null);
        mBanner = (Banner) headView.findViewById(R.id.banner);
        mBanner.setOnBannerClickListener(this);
    }

    @Override
    public void attachView() {

        mAdapter = new DiscoverAdapter(R.layout.item_discover, mList);
        mAdapter.addHeaderView(headView);

        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerDecoration itemDecoration = new DividerDecoration(ContextCompat.getColor(getActivity(), R.color.common_divider_narrow), 1, 0, 0);
        itemDecoration.setDrawLastItem(false);
        recyclerview.addItemDecoration(itemDecoration);
        recyclerview.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);
        refreshLayout.setOnRefreshListener(this);
        imgRecommendSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });
    }

    @Override
    public void initDatas() {
        addItem();
        getAdList();
        getZoneList();
    }

    @Override
    public void configViews() {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (position == 0) {
            startActivity(new Intent(getActivity(), TopActivity.class));
        } else if (position == 1) {

            getActivity().startActivity(new Intent(getActivity(), TopCategoryListActivity.class));
        }  else if (position == 2) {
            EventBus.getDefault().postSticky("3");

        } else {
            SubZoneBean bean = mList.get(position);

            if (bean.getSub() == null || bean.getSub().size() == 0) {
                CoomonApi.toBrowser(getActivity(), bean.getZone_link());
            } else {
                int num = 0;
                for (int i = 0; i < bean.getSub().size(); i++) {
                    if (bean.getSub().get(i).getSub() == null || bean.getSub().get(i).getSub().size() == 0) {
                        num += 1;
                    }
                }

                if (num==bean.getSub().size()){

                    Intent intent = new Intent(activity, DiscoverOther3Activity.class);
                    intent.putExtra("type", bean.getZone_name() );
                    intent.putExtra("data", (ArrayList) bean.getSub());
                    startActivity(intent);

                }else {
                    Intent intent = new Intent(getActivity(), DiscoverOtherActivity.class);
                    intent.putExtra("id", bean.getId() + "");
                    intent.putExtra("type", bean.getZone_name() + "");
                    startActivity(intent);
                }


            }
        }
    }


    private void addItem() {
        SubZoneBean bean = new SubZoneBean();
        bean.setZone_name("排行榜");
        SubZoneBean bean2 = new SubZoneBean();
        bean2.setZone_name("分类");
        SubZoneBean bean1 = new SubZoneBean();
        bean1.setZone_name("会员专区");

        mList.add(bean);
        mList.add(bean2);
        mList.add(bean1);
    }

    /**
     * 获取轮播图
     */
    private void getAdList() {
        RetrofitClient.getInstance().createApi().getAdList("4")
                .compose(RxUtils.<HttpResult<List<BannerBean>>>io_main())
                .subscribe(new BaseObjObserver<List<BannerBean>>(getActivity()) {
                    @Override
                    protected void onHandleSuccess(List<BannerBean> bannerBeen) {
                        if (bannerBeen == null || bannerBeen.size() == 0) {
                            return;
                        }
                        bannerBeens = bannerBeen;
                        bannerList.clear();
                        for (int i = 0; i < bannerBeen.size(); i++) {
                            bannerList.add(bannerBeen.get(i).getImage_url());
                        }
                        setBanner(bannerList);
                    }
                });
    }

    /**
     * 设置轮播图
     *
     * @param list
     */
    private void setBanner(List<String> list) {
        if (activity==null) return;
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mBanner.setImageLoader(new MyImageLoader());
        mBanner.setImages(list);
        mBanner.setBannerAnimation(Transformer.Default);
        mBanner.isAutoPlay(true);
        mBanner.setViewPagerIsScroll(true);
        mBanner.setDelayTime(3000);
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        mBanner.start();
    }

    private void getZoneList() {
        RetrofitClient.getInstance().createApi().getAllSubZone("0")
                .compose(RxUtils.<HttpResult<List<SubZoneBean>>>io_main())
                .subscribe(new BaseObjObserver<List<SubZoneBean>>(activity, refreshLayout) {
                    @Override
                    protected void onHandleSuccess(List<SubZoneBean> list) {

                        if (list.isEmpty()) {
                            return;
                        }
                        mList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onRefresh() {
        mList.clear();
        addItem();
        getAdList();
        getZoneList();
    }

    @Override
    public void OnBannerClick(int position) {
        BannerBean bannerBean = bannerBeens.get(position - 1);

        if (bannerBean.getType().equals("0")) {
            CoomonApi.toBrowser(getActivity(), bannerBean.getUrl());
        } else {
            BookDetailActivity.startActivity(activity, bannerBean.getType());
        }
    }
}
