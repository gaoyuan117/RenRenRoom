package com.justwayward.renren.ui.activity;

import android.widget.TextView;

import com.justwayward.renren.R;
import com.justwayward.renren.ReaderApplication;
import com.justwayward.renren.base.BaseActivity;
import com.justwayward.renren.component.AppComponent;

import butterknife.Bind;

public class UserAgreementActivity extends BaseActivity {

    @Bind(R.id.tv_agreement)
    TextView tvAgreement;

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_agreement;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setTitle("用户协议");
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
    }

    @Override
    public void initDatas() {
        if (ReaderApplication.aboutBean!=null){
            tvAgreement.setText(ReaderApplication.aboutBean.getAgreement());
        }
    }

    @Override
    public void configViews() {

    }


}
