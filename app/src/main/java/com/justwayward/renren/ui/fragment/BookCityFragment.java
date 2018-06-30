package com.justwayward.renren.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.justwayward.renren.R;
import com.justwayward.renren.ReaderApplication;
import com.justwayward.renren.base.BaseFragment;
import com.justwayward.renren.bean.BookCityCategoryBean;
import com.justwayward.renren.bean.CategoryBean;
import com.justwayward.renren.component.AppComponent;
import com.justwayward.renren.retrofit.BaseObjObserver;
import com.justwayward.renren.retrofit.HttpResult;
import com.justwayward.renren.retrofit.RetrofitClient;
import com.justwayward.renren.retrofit.RxUtils;
import com.justwayward.renren.ui.activity.SearchActivity;
import com.justwayward.renren.ui.adapter.ViewPagerAdapter;
import com.justwayward.renren.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by gaoyuan on 2017/11/14.
 */

public class BookCityFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {

    @Bind(R.id.img_recommend_search)
    ImageView imgRecommendSearch;
    @Bind(R.id.navigation_bar)
    RelativeLayout navigationBar;
    @Bind(R.id.tab_fragment_city)
    TabLayout mTabLayout;
    @Bind(R.id.vp_fragment)
    ViewPager mViewPager;

    private ViewPagerAdapter mAdapter;
    private List<String> mTitles = new ArrayList<>();
    private List<Fragment> mList = new ArrayList<>();
    private int current;

    public static List<CategoryBean> categoryList;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_book_city;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void attachView() {

    }

    @Override
    public void initDatas() {
        EventBus.getDefault().register(this);
        getCategoryList();
        getAllCategoryList();
    }

    @Override
    public void configViews() {
        setTabViewPager();
    }

    @OnClick({R.id.img_recommend_search, R.id.navigation_bar})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.img_recommend_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;

            case R.id.navigation_bar:

                break;
        }
    }

    /**
     * 设置Tablayout
     */
    private void setTabViewPager() {
        mAdapter = new ViewPagerAdapter(getChildFragmentManager(), getActivity(), mList, mTitles);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(11);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabTextColors(ContextCompat.getColor(getActivity(), R.color.white), ContextCompat.getColor(getActivity(), R.color.tab_color));
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.tab_color));
        mTabLayout.setOnTabSelectedListener(this);

    }

    /**
     * 获取导航栏
     */
    private void getCategoryList() {
        RetrofitClient.getInstance().createApi().getCategoryList("0")
                .compose(RxUtils.<HttpResult<List<BookCityCategoryBean>>>io_main())
                .subscribe(new BaseObjObserver<List<BookCityCategoryBean>>(getActivity()) {
                    @Override
                    protected void onHandleSuccess(List<BookCityCategoryBean> list) {
                        setCategory(list);
                    }
                });
    }

    /**
     * 设置导航栏
     *
     * @param list
     */
    private void setCategory(List<BookCityCategoryBean> list) {
        mTitles.add("首页");
        mList.add(new HomeFragment());

        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (i == ReaderApplication.vipPosition - 2) {
                    mTitles.add("会员");
                    mList.add(new VipFragment());

                }
                mTitles.add(list.get(i).getCategory());
                BookOtherFragment fragment = new BookOtherFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("id", list.get(i).getId() + "");
                fragment.setArguments(bundle);
                mList.add(fragment);

            }
        }


        mTitles.add("发现");
        mList.add(new VipFragment());

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (mTabLayout == null) return;
        String text = tab.getText().toString();
        if (text.equals("发现")) {
            EventBus.getDefault().postSticky("2");
            mViewPager.setCurrentItem(current);
            mTabLayout.getTabAt(current).select();
            return;
        }
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            if (mTabLayout.getTabAt(i) == tab) {
                mViewPager.setCurrentItem(i);
                current = i;
                break;
            }
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Subscribe
    public void change(String tab) {//切换底部栏目
        if (tab.equals("discover")) {
            for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                LogUtils.e("标题栏：" + mTabLayout.getTabAt(i).getText());
                if (mTabLayout.getTabAt(i).getText().equals("会员")) {
                    mViewPager.setCurrentItem(i);
                    mTabLayout.getTabAt(i).select();
                    break;
                }
            }
        }
    }

    /**
     * 获取全部导航栏，点击更多需要
     */
    private void getAllCategoryList() {
//        RetrofitClient.getInstance().createApi().getCategoryList("-1")
//                .compose(RxUtils.<HttpResult<List<BookCityCategoryBean>>>io_main())
//                .subscribe(new BaseObjObserver<List<BookCityCategoryBean>>(getActivity()) {
//                    @Override
//                    protected void onHandleSuccess(List<BookCityCategoryBean> list) {
//                        if (list != navigationBar) {
//                            categoryList = list;
//                        }
//                    }
//                });

        RetrofitClient.getInstance().createApi().getCategoryStatistical(ReaderApplication.token)
                .compose(RxUtils.<HttpResult<List<CategoryBean>>>io_main())
                .subscribe(new BaseObjObserver<List<CategoryBean>>(getActivity()) {
                    @Override
                    protected void onHandleSuccess(List<CategoryBean> categoryBeen) {
//                        if (categoryBeen != null && categoryBeen.size() > 0) {
                        categoryList = categoryBeen;
//                        }
                    }
                });
    }

}
