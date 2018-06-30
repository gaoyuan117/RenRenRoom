package com.justwayward.renren.ui.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.justwayward.renren.ConnectionChangeReceiver;
import com.justwayward.renren.R;
import com.justwayward.renren.ReaderApplication;
import com.justwayward.renren.base.BaseActivity;
import com.justwayward.renren.bean.UserBean;
import com.justwayward.renren.component.AppComponent;
import com.justwayward.renren.manager.SettingManager;
import com.justwayward.renren.retrofit.BaseObjObserver;
import com.justwayward.renren.retrofit.HttpResult;
import com.justwayward.renren.retrofit.RetrofitClient;
import com.justwayward.renren.retrofit.RxUtils;
import com.justwayward.renren.service.DownloadBookService;
import com.justwayward.renren.ui.adapter.ViewPagerAdapter;
import com.justwayward.renren.ui.fragment.BookCityFragment;
import com.justwayward.renren.ui.fragment.BookShelfFragment;
import com.justwayward.renren.ui.fragment.DiscoverFragment;
import com.justwayward.renren.ui.fragment.MyFragment;
import com.justwayward.renren.utils.LogUtils;
import com.justwayward.renren.view.AlphaTabsIndicator;
import com.justwayward.renren.view.GenderPopupWindow;
import com.justwayward.renren.view.OnTabChangedListner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

public class Main2Activity extends BaseActivity implements OnTabChangedListner {

    @Bind(R.id.activity_main)
    LinearLayout activityMain;
    ViewPager viewPager;
    AlphaTabsIndicator mAlphaTabsIndicator;

    private List<Fragment> mList;
    private int target;
    private long firstTime = 0;
    private GenderPopupWindow genderPopupWindow;
    ViewPagerAdapter adapter;
    private ConnectionChangeReceiver myReceiver;
    private SharedPreferences sp;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main2;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {


    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setVisibility(View.GONE);
    }

    @Override
    public void initDatas() {
        sp = getSharedPreferences("data", MODE_PRIVATE);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        mAlphaTabsIndicator = (AlphaTabsIndicator) findViewById(R.id.alphaIndicator);
        viewPager.setOffscreenPageLimit(4);
        startService(new Intent(this, DownloadBookService.class));
        getUser();


        EventBus.getDefault().register(this);
        mList = new ArrayList<>();

        mList.add(new BookShelfFragment());
        mList.add(new BookCityFragment());
        mList.add(new DiscoverFragment());
        mList.add(new MyFragment());
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), this, mList);
        viewPager.setAdapter(adapter);

        mAlphaTabsIndicator.setViewPager(viewPager);
        mAlphaTabsIndicator.setOnTabChangedListner(this);
        mAlphaTabsIndicator.removeAllBadge();
        mAlphaTabsIndicator.setTabCurrenItem(0);
        registerReceiver();

    }


    @Override
    public void configViews() {
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {//如果两次按键时间间隔大于2秒，则不退出
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;//更新firstTime
                    return true;
                } else {//两次按键小于2秒时，退出应用
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    public void showChooseSexPopupWindow() {
        if (genderPopupWindow == null) {
            genderPopupWindow = new GenderPopupWindow(Main2Activity.this);
        }
        if (!SettingManager.getInstance().isUserChooseSex()
                && !genderPopupWindow.isShowing()) {
            genderPopupWindow.showAtLocation(mCommonToolbar, Gravity.CENTER, 0, 0);
        }
    }

    @Subscribe
    public void change(String tab) {//切换底部栏目
        if (tab.equals("1")) {
            target = 1;

        } else if (tab.equals("2")) {
            target = 2;
        } else if (tab.equals("3")) {
            target = 1;
            EventBus.getDefault().postSticky("discover");
        }

        mAlphaTabsIndicator.setTabCurrenItem(target);
    }

    /**
     * 获取用户信息
     */
    private void getUser() {
        RetrofitClient.getInstance().createApi().getUser(ReaderApplication.token)
                .compose(RxUtils.<HttpResult<UserBean>>io_main())
                .subscribe(new BaseObjObserver<UserBean>(this, false) {
                    @Override
                    protected void onHandleSuccess(UserBean userBean) {

                        long create_time = userBean.getCreate_time();//到期时间
                        Date currentTime = new Date();
                        long diff = currentTime.getTime() - create_time * 1000;//注册时间
                        long days = diff / (1000 * 60 * 60 * 24);
                        LogUtils.e("注册时间：" + create_time);
                        userBean.getId();

                        ReaderApplication.user = userBean;
                        ReaderApplication.days = days;
                        int like_type = userBean.getLike_type();
                        if (like_type == 0) {//未设置喜好
                            showChooseSexPopupWindow();
                        }
                    }
                });
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        myReceiver = new ConnectionChangeReceiver();
        this.registerReceiver(myReceiver, filter);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        this.unregisterReceiver(myReceiver);
    }

    @Override
    public void onTabSelected(int tabNum) {
        viewPager.setCurrentItem(tabNum);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            String type = intent.getStringExtra("type");
            if (type.equals("relogin")) {
                mAlphaTabsIndicator.setTabCurrenItem(0);
                viewPager.setCurrentItem(0);
            }
        }
    }


}
