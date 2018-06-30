package com.justwayward.renren.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.justwayward.renren.R;
import com.justwayward.renren.ReaderApplication;
import com.justwayward.renren.api.CoomonApi;
import com.justwayward.renren.base.BaseFragment;
import com.justwayward.renren.bean.UserBean;
import com.justwayward.renren.component.AppComponent;
import com.justwayward.renren.retrofit.BaseObjObserver;
import com.justwayward.renren.retrofit.HttpResult;
import com.justwayward.renren.retrofit.RetrofitClient;
import com.justwayward.renren.retrofit.RxUtils;
import com.justwayward.renren.ui.activity.BuyHistoryActivity;
import com.justwayward.renren.ui.activity.MyAssetsActivity;
import com.justwayward.renren.ui.activity.MyInfoActivity;
import com.justwayward.renren.ui.activity.MyVipActivity;
import com.justwayward.renren.ui.activity.ReadyHistoryActivity;
import com.justwayward.renren.ui.activity.SettingActivity;
import com.yuyh.easyadapter.glide.GlideCircleTransform;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by gaoyuan on 2017/11/21.
 */

public class MyFragment extends BaseFragment {
    @Bind(R.id.img_my_setting)
    ImageView imgMySetting;
    @Bind(R.id.img_my_avatar)
    ImageView imgMyAvatar;
    @Bind(R.id.tv_my_name)
    TextView tvMyName;
    @Bind(R.id.tv_my_money)
    TextView tvMyMoney;
    @Bind(R.id.tv_my_liquan)
    TextView tvMyLiquan;
    @Bind(R.id.tv_my_vip)
    TextView tvMyVip;
    @Bind(R.id.tv_my_yue)
    TextView tvMyYue;

    @Bind(R.id.tv_my_history)
    TextView tvMyHistory;
    @Bind(R.id.tv_my_buy_history)
    TextView tvMyBuyHistory;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void attachView() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(ReaderApplication.token)){
            tvMyMoney.setText("0");
            tvMyName.setText("请登录您的账户");
        }
        getUser();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            getUser();
        }
    }

    @OnClick({R.id.img_my_setting, R.id.img_my_avatar, R.id.tv_my_vip, R.id.tv_my_yue, R.id.tv_my_history, R.id.tv_my_buy_history, R.id.tv_my_recharge})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_my_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.img_my_avatar:
                if(!CoomonApi.isLogin(getActivity())) return;
                startActivity(new Intent(getActivity(), MyInfoActivity.class));
                break;
            case R.id.tv_my_vip://我的包月会员
                if(!CoomonApi.isLogin(getActivity())) return;
                startActivity(new Intent(getActivity(), MyVipActivity.class));
                break;
            case R.id.tv_my_yue://我的资产
                if(!CoomonApi.isLogin(getActivity())) return;
                startActivity(new Intent(getActivity(), MyAssetsActivity.class).putExtra("coin", tvMyMoney.getText().toString()));
                break;
            case R.id.tv_my_recharge://我的资产
                if(!CoomonApi.isLogin(getActivity())) return;
                startActivity(new Intent(getActivity(), MyAssetsActivity.class).putExtra("coin", tvMyMoney.getText().toString()));
                break;
            case R.id.tv_my_history:
                if(!CoomonApi.isLogin(getActivity())) return;
                startActivity(new Intent(getActivity(), ReadyHistoryActivity.class));
                break;
            case R.id.tv_my_buy_history://购买记录
                if(!CoomonApi.isLogin(getActivity())) return;
                startActivity(new Intent(getActivity(), BuyHistoryActivity.class));
                break;
        }
    }

    /**
     * 获取用户信息
     */
    private void getUser() {
        RetrofitClient.getInstance().createApi().getUser(ReaderApplication.token)
                .compose(RxUtils.<HttpResult<UserBean>>io_main())
                .subscribe(new BaseObjObserver<UserBean>(getActivity(), false) {
                    @Override
                    protected void onHandleSuccess(UserBean userBean) {
                        if (activity!=null){
                            Glide.with(getActivity()).load(userBean.getAvatar())
                                    .error(R.drawable.avatar_default)
                                    .transform(new GlideCircleTransform(mContext)).into(imgMyAvatar);
                        }
                        try {
                            tvMyMoney.setText(userBean.getCoin() + "");
                            tvMyName.setText(userBean.getUser_nickname());
                        }catch (Exception e){

                        }

                    }
                });
    }
}
