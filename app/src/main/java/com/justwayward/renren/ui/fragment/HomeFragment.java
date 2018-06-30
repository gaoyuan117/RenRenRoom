package com.justwayward.renren.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.justwayward.renren.R;
import com.justwayward.renren.ReaderApplication;
import com.justwayward.renren.api.CoomonApi;
import com.justwayward.renren.base.BaseFragment;
import com.justwayward.renren.bean.BannerBean;
import com.justwayward.renren.bean.BookCityBean;
import com.justwayward.renren.bean.BookCityCategoryBean;
import com.justwayward.renren.bean.FreeBean;
import com.justwayward.renren.component.AppComponent;
import com.justwayward.renren.retrofit.BaseObjObserver;
import com.justwayward.renren.retrofit.HttpResult;
import com.justwayward.renren.retrofit.RetrofitClient;
import com.justwayward.renren.retrofit.RxUtils;
import com.justwayward.renren.ui.activity.BookDetailActivity;
import com.justwayward.renren.ui.activity.FreeListActivity;
import com.justwayward.renren.ui.activity.SubCategoryActivity;
import com.justwayward.renren.ui.activity.TopActivity;
import com.justwayward.renren.ui.activity.TopCategoryListActivity;
import com.justwayward.renren.ui.adapter.HomeAdapter;
import com.justwayward.renren.ui.adapter.HomeFootAdapter;
import com.justwayward.renren.utils.LogUtils;
import com.justwayward.renren.utils.MyImageLoader;
import com.justwayward.renren.utils.ToastUtils;
import com.justwayward.renren.view.MyGridView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 书城首页
 * Created by gaoyuan on 2017/11/17.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, OnBannerClickListener {

    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;

    private View headView;
    private View footView;
    private List<BookCityBean> mList = new ArrayList<>();
    private List<String> bannerList = new ArrayList<>();
    private HomeAdapter mAdapter;
    private ViewHolder viewHolder;

    private String freeId = "-1";
    private Banner mBanner;
    private List<BookCityCategoryBean> categoryList;
    private List<LinearLayout> layoutList = new ArrayList<>();
    private List<TextView> tvList = new ArrayList<>();
    private List<ImageView> imgList = new ArrayList<>();
    private List<BannerBean> bannerBeens;
    private MyGridView footGridView;
    private List<BannerBean> footList = new ArrayList<>();
    private HomeFootAdapter footAdapter;


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_book_city_home;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

        headView = View.inflate(getActivity(), R.layout.head_book_city_home, null);
        footView = View.inflate(getActivity(), R.layout.foot_home, null);
        TextView tvCopyRight = (TextView) footView.findViewById(R.id.tv_copy_right);

        if (ReaderApplication.aboutBean != null) {
            tvCopyRight.setText(ReaderApplication.aboutBean.getCopyright());
        }

        footGridView = (MyGridView) footView.findViewById(R.id.gv_foot);
        footAdapter = new HomeFootAdapter(footList, getActivity());
        footGridView.setAdapter(footAdapter);

        mBanner = (Banner) headView.findViewById(R.id.banner);
        mBanner.setOnBannerClickListener(this);
        viewHolder = new ViewHolder(headView);
        layoutList.add(viewHolder.layout1);
        layoutList.add(viewHolder.layout2);
//        layoutList.add(viewHolder.layout3);

        tvList.add(viewHolder.tvHomeDushi);
        tvList.add(viewHolder.tvHomeXianyan);
        tvList.add(viewHolder.tvHomeQita);

        imgList.add(viewHolder.imgHomeDushi);
        imgList.add(viewHolder.imgHomeXianyan);
        imgList.add(viewHolder.imgHomeQita);

        mAdapter = new HomeAdapter(mList);
        mAdapter.addHeaderView(headView);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setAdapter(mAdapter);

        refresh.setOnRefreshListener(this);

    }

    @Override
    public void attachView() {

    }

    @Override
    public void initDatas() {
        getTodyFree();
        getHomeList();
        getAdList();
        getCategory();
        getFootAdList();
    }

    @Override
    public void configViews() {
        viewHolder.layoutPaiHang.setOnClickListener(this);
        viewHolder.layoutFenLei.setOnClickListener(this);
        viewHolder.layout1.setOnClickListener(this);
        viewHolder.layout2.setOnClickListener(this);
        viewHolder.layout3.setOnClickListener(this);
        viewHolder.tvTj.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_paihang://排行
                Intent intent = new Intent(getActivity(), TopActivity.class);
                getActivity().startActivity(intent);
                break;

            case R.id.layout_fenlei://分类
                getActivity().startActivity(new Intent(getActivity(), TopCategoryListActivity.class));
                break;

            case R.id.layout1://都市

                try {
                    String category = categoryList.get(0).getCategory();
                    String id = categoryList.get(0).getId();
                    getActivity().startActivity(new Intent(getActivity(),
                            SubCategoryActivity.class)
                            .putExtra("type", category)
                            .putExtra("id", id));
                } catch (Exception e) {

                }

                break;

            case R.id.layout2://现言

                try {
                    String category = categoryList.get(1).getCategory();
                    String id = categoryList.get(1).getId();
                    getActivity().startActivity(new Intent(getActivity(),
                            SubCategoryActivity.class)
                            .putExtra("type", category)
                            .putExtra("id", id));
                } catch (Exception e) {

                }

                break;

            case R.id.layout3://其他
                try {
                    getActivity().startActivity(new Intent(getActivity(),
                            FreeListActivity.class)
                            .putExtra("type", "限时免费"));
                } catch (Exception e) {

                }

                break;

            case R.id.tv_tj://今日免费
                if (freeId.equals("-1")) return;
                BookDetailActivity.startActivity(getActivity(), freeId);
                break;
        }
    }

    /**
     * 获取今日推荐
     */
    private void getTodyFree() {
        RetrofitClient.getInstance().createApi().getTodyFree(ReaderApplication.token)
                .compose(RxUtils.<HttpResult<FreeBean>>io_main())
                .subscribe(new BaseObjObserver<FreeBean>(getActivity(), false) {

                    @Override
                    protected void onHandleSuccess(FreeBean freeBean) {
                        freeId = freeBean.getId() + "";
                        viewHolder.tvTj.setText("今日免费：" + freeBean.getTitle());
                    }
                });
    }

    /**
     * 获取小说列表
     */
    private void getHomeList() {
        RetrofitClient.getInstance().createApi().getIndexRecommend(ReaderApplication.token)
                .compose(RxUtils.<HttpResult<List<BookCityBean>>>io_main())
                .subscribe(new BaseObjObserver<List<BookCityBean>>(getActivity(), refresh) {
                    @Override
                    protected void onHandleSuccess(List<BookCityBean> bookCityBeen) {
                        if (bookCityBeen == null || bookCityBeen.size() == 0) {
                            return;
                        }
                        mList.clear();

                        for (int i = 0; i < bookCityBeen.size(); i++) {
                            if (bookCityBeen.get(i).getList().size() > 0) {
                                mList.add(bookCityBeen.get(i));
                                LogUtils.e("id："+bookCityBeen.get(i).getCategory_id());

                            }
                        }

                        mAdapter.notifyDataSetChanged();

                    }
                });
    }

    /**
     * 获取首页分类.都市 现言 其他
     */
    private void getCategory() {
        RetrofitClient.getInstance().createApi().getCategory2(1)
                .compose(RxUtils.<HttpResult<List<BookCityCategoryBean>>>io_main())
                .subscribe(new BaseObjObserver<List<BookCityCategoryBean>>(getActivity()) {
                    @Override
                    protected void onHandleSuccess(List<BookCityCategoryBean> list) {
                        if (activity == null)
                            return;
                        if (list.isEmpty()) {
                            return;
                        }
                        for (int i = 0; i < list.size(); i++) {
                            try {

                                layoutList.get(i).setVisibility(View.VISIBLE);
                                tvList.get(i).setText(list.get(i).getCategory());
                                Glide.with(getActivity()).load(list.get(i).getIcon()).error(R.drawable.cover_default).into(imgList.get(i));

                            }catch (Exception e){

                            }

                        }
                        categoryList = list;
                    }
                });
    }

    @Override
    public void onRefresh() {
        getTodyFree();
        getHomeList();
        getAdList();
        getCategory();
        getFootAdList();
    }

    /**
     * 获取轮播图
     */
    private void getAdList() {
        RetrofitClient.getInstance().createApi().getAdList("2")
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
     * 获取底部广告
     */
    private void getFootAdList() {
        RetrofitClient.getInstance().createApi().getAdList("3")
                .compose(RxUtils.<HttpResult<List<BannerBean>>>io_main())
                .subscribe(new BaseObjObserver<List<BannerBean>>(getActivity()) {
                    @Override
                    protected void onHandleSuccess(List<BannerBean> bannerBeen) {
                        if (bannerBeen == null || bannerBeen.size() == 0) {
                            return;
                        }
                        footList.clear();
                        footList.addAll(bannerBeen);
                        footAdapter.notifyDataSetChanged();
                        mAdapter.removeFooterView(footView);
                        mAdapter.addFooterView(footView);

                    }
                });
    }

    /**
     * 设置轮播图
     *
     * @param list
     */
    private void setBanner(List<String> list) {
        if (activity == null) return;
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

    @Override
    public void OnBannerClick(int position) {
        BannerBean bannerBean = bannerBeens.get(position - 1);

        if (bannerBean.getType().equals("0")) {
            CoomonApi.toBrowser(getActivity(), bannerBean.getUrl());
        } else {
            BookDetailActivity.startActivity(activity, bannerBean.getType());
        }
    }

    static class ViewHolder {
        @Bind(R.id.banner)
        Banner banner;
        @Bind(R.id.tv_home_paihang)
        TextView tvHomePaihang;
        @Bind(R.id.img_home_fenlei)
        TextView imgHomeFenlei;
        @Bind(R.id.img_home_dushi)
        ImageView imgHomeDushi;
        @Bind(R.id.tv_home_dushi)
        TextView tvHomeDushi;
        @Bind(R.id.layout1)
        LinearLayout layout1;
        @Bind(R.id.img_home_xianyan)
        ImageView imgHomeXianyan;
        @Bind(R.id.tv_home_xianyan)
        TextView tvHomeXianyan;
        @Bind(R.id.layout2)
        LinearLayout layout2;
        @Bind(R.id.layout_paihang)
        LinearLayout layoutPaiHang;
        @Bind(R.id.layout_fenlei)
        LinearLayout layoutFenLei;
        @Bind(R.id.img_home_qita)
        ImageView imgHomeQita;
        @Bind(R.id.tv_home_qita)
        TextView tvHomeQita;
        @Bind(R.id.layout3)
        LinearLayout layout3;
        @Bind(R.id.tv_tj)
        TextView tvTj;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
