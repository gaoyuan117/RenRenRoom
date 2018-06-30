package com.justwayward.renren.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.justwayward.renren.R;
import com.justwayward.renren.base.BaseActivity;
import com.justwayward.renren.bean.FreeListBean;
import com.justwayward.renren.component.AppComponent;
import com.justwayward.renren.retrofit.BaseObjObserver;
import com.justwayward.renren.retrofit.HttpResult;
import com.justwayward.renren.retrofit.RetrofitClient;
import com.justwayward.renren.retrofit.RxUtils;
import com.justwayward.renren.ui.adapter.ViewPagerAdapter;
import com.justwayward.renren.ui.fragment.CategoryFragment;
import com.justwayward.renren.ui.fragment.FreeListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class FreeListActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    @Bind(R.id.tab_fragment_city)
    TabLayout mTabLayout;
    @Bind(R.id.vp_fragment)
    ViewPager mViewPager;

    private ViewPagerAdapter mAdapter;
    private List<String> mTitles = new ArrayList<>();
    private List<Fragment> mList = new ArrayList<>();
    public static List<FreeListBean> freeList = new ArrayList<>();

    String type;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sub_category;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        type = getIntent().getStringExtra("type");
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
        mCommonToolbar.setTitle(type);
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews() {
        setTabViewPager();
    }

    /**
     * 设置Tablayout
     */
    private void setTabViewPager() {
        mTitles.add("男频");
        mTitles.add("女频");
        mTitles.add("其它");

        //男频
        FreeListFragment newFragment = new FreeListFragment();
        Bundle newBundle = new Bundle();
        newBundle.putString("type", "男频");
        newFragment.setArguments(newBundle);

        //女频
        FreeListFragment hotFragment = new FreeListFragment();
        Bundle hotBundle = new Bundle();
        hotBundle.putString("type", "女频");
        hotFragment.setArguments(hotBundle);

        //其它
        FreeListFragment finishFragment = new FreeListFragment();
        Bundle finishBundle = new Bundle();
        finishBundle.putString("type", "其它");
        finishFragment.setArguments(finishBundle);

        mList.add(newFragment);
        mList.add(hotFragment);
        mList.add(finishFragment);
        mAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), getActivity(), mList, mTitles);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabTextColors(ContextCompat.getColor(getActivity(), R.color.white), ContextCompat.getColor(getActivity(), R.color.tab_color));
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.tab_color));
        mTabLayout.setOnTabSelectedListener(this);


    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        String text = tab.getText().toString();
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            if (mTabLayout.getTabAt(i) == tab) {
                mViewPager.setCurrentItem(i);
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
}
