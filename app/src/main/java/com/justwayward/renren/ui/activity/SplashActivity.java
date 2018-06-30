package com.justwayward.renren.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.justwayward.renren.R;
import com.justwayward.renren.ReaderApplication;
import com.justwayward.renren.bean.AboutBean;
import com.justwayward.renren.bean.ShareBean;
import com.justwayward.renren.retrofit.BaseObjObserver;
import com.justwayward.renren.retrofit.HttpResult;
import com.justwayward.renren.retrofit.RetrofitClient;
import com.justwayward.renren.retrofit.RxUtils;
import com.justwayward.renren.ui.activity.login.LoginActivity;
import com.justwayward.renren.utils.LogUtils;
import com.justwayward.renren.utils.SharedPreferencesUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    @Bind(R.id.tvSkip)
    TextView tvSkip;
    @Bind(R.id.frameLayout)
    FrameLayout frameLayout;
    @Bind(R.id.img)
    ImageView imageView;

    private boolean flag = false;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        getAbout();
        getUrl();

        ReaderApplication.wordsPrice = SharedPreferencesUtil.getInstance().getFloat("words_price", 0);

        LogUtils.e("字数价格：" + ReaderApplication.wordsPrice);

        runnable = new Runnable() {
            @Override
            public void run() {
                goHome();
            }
        };

        tvSkip.postDelayed(runnable, 2000);

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHome();
            }
        });
    }

    private synchronized void goHome() {
        if (!flag) {
            flag = true;
            String token = SharedPreferencesUtil.getInstance().getString("token");
            ReaderApplication.uid = SharedPreferencesUtil.getInstance().getString("uid");

//            if (TextUtils.isEmpty(token)) {
//                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//            } else {
//                startActivity(new Intent(SplashActivity.this, Main2Activity.class));
//            }

            startActivity(new Intent(SplashActivity.this, Main2Activity.class));

            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flag = true;
        tvSkip.removeCallbacks(runnable);
        ButterKnife.unbind(this);
    }

    private void getAbout() {
        RetrofitClient.getInstance().createApi().getAbout(ReaderApplication.token)
                .compose(RxUtils.<HttpResult<AboutBean>>io_main())
                .subscribe(new BaseObjObserver<AboutBean>(this) {
                    @Override
                    protected void onHandleSuccess(AboutBean aboutBean) {
                        ReaderApplication.aboutBean = aboutBean;
                    }
                });
    }

    /**
     * 获取基本配置信息
     */
    private void getUrl() {
        RetrofitClient.getInstance().createApi().getSettingInfo("")
                .compose(RxUtils.<HttpResult<ShareBean>>io_main())
                .subscribe(new BaseObjObserver<ShareBean>(this, false) {
                    @Override
                    protected void onHandleSuccess(ShareBean shareBean) {
                        SharedPreferencesUtil.getInstance().putFloat("words_price", shareBean.getWords_price());
                        ReaderApplication.wordsPrice = shareBean.getWords_price();
                        ReaderApplication.shareUrl = shareBean.getShare_url();
                        ReaderApplication.dushi = shareBean.getDushi_cid();
                        ReaderApplication.xianyan = shareBean.getXianyan_cid();
                        ReaderApplication.qita = shareBean.getQita_cid();
                        ReaderApplication.vipPosition = shareBean.getNav_member_position();

                        Glide.with(ReaderApplication.getsInstance()).load(shareBean.getBootpic()).into(imageView);
                    }
                });
    }
}
