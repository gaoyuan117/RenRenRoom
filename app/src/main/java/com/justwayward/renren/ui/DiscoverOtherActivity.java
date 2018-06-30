package com.justwayward.renren.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.justwayward.renren.R;
import com.justwayward.renren.base.BaseActivity;
import com.justwayward.renren.bean.SubZoneBean;
import com.justwayward.renren.component.AppComponent;
import com.justwayward.renren.retrofit.BaseObjObserver;
import com.justwayward.renren.retrofit.HttpResult;
import com.justwayward.renren.retrofit.RetrofitClient;
import com.justwayward.renren.retrofit.RxUtils;
import com.justwayward.renren.ui.adapter.ViewPagerAdapter;
import com.justwayward.renren.ui.fragment.DiscoverOtherFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class DiscoverOtherActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    @Bind(R.id.tab_fragment_city)
    TabLayout mTabLayout;
    @Bind(R.id.vp_fragment)
    ViewPager mViewPager;

    private ViewPagerAdapter mAdapter;
    private List<String> mTitles = new ArrayList<>();
    private List<Fragment> mList = new ArrayList<>();

    String type;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sub_category;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initToolBar() {
        type = getIntent().getStringExtra("type");
        mCommonToolbar.setTitle(type);
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
    }

    @Override
    public void initDatas() {
        String id = getIntent().getStringExtra("id");
        getAllSubZone(id);
    }


    @Override
    public void configViews() {

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

    private void getAllSubZone(String id) {
        RetrofitClient.getInstance().createApi().getAllSubZone(id)
                .compose(RxUtils.<HttpResult<List<SubZoneBean>>>io_main())
                .subscribe(new BaseObjObserver<List<SubZoneBean>>(this, "加载中") {
                    @Override
                    protected void onHandleSuccess(List<SubZoneBean> list) {
                        setTabViewPager(list);
                    }
                });
    }

    /**
     * 设置Tablayout
     */
    private void setTabViewPager(List<SubZoneBean> list) {

        for (int i = 0; i < list.size(); i++) {
            mTitles.add(list.get(i).getZone_name());
            DiscoverOtherFragment fragment = new DiscoverOtherFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", list.get(i));
            fragment.setArguments(bundle);
            mList.add(fragment);
        }

        if (mTitles.size() > 4) {
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        } else {
            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        }

        mAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), getActivity(), mList, mTitles);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabTextColors(ContextCompat.getColor(getActivity(), R.color.white), ContextCompat.getColor(getActivity(), R.color.tab_color));
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.tab_color));
        mTabLayout.setOnTabSelectedListener(this);

    }
}
