package com.justwayward.renren.ui.activity.login;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.justwayward.renren.R;
import com.justwayward.renren.ReaderApplication;
import com.justwayward.renren.api.CoomonApi;
import com.justwayward.renren.base.BaseActivity;
import com.justwayward.renren.bean.CommonBean;
import com.justwayward.renren.bean.LoginBean;
import com.justwayward.renren.component.AppComponent;
import com.justwayward.renren.manager.SettingManager;
import com.justwayward.renren.retrofit.BaseObjObserver;
import com.justwayward.renren.retrofit.HttpResult;
import com.justwayward.renren.retrofit.RetrofitClient;
import com.justwayward.renren.retrofit.RxUtils;
import com.justwayward.renren.ui.activity.Main2Activity;
import com.justwayward.renren.utils.CountDownUtils;
import com.justwayward.renren.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity implements CountDownUtils.CountdownListener {

    @Bind(R.id.edt_register_user)
    EditText edtRegisterUser;
    @Bind(R.id.edt_register_code)
    EditText edtRegisterCode;
    @Bind(R.id.tv_register_code)
    TextView tvRegisterCode;
    @Bind(R.id.edt_register_pwd)
    EditText edtRegisterPwd;
    @Bind(R.id.edt_reset_pwd)
    EditText edtResetPwd;
    @Bind(R.id.btn_register)
    Button btnRegister;

    private String mobile;
    private CountDownUtils countDown;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setTitle("注册");
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews() {
        countDown = new CountDownUtils(tvRegisterCode, "%s秒", 60);
        countDown.setCountdownListener(this);
    }


    @OnClick({R.id.tv_register_code, R.id.btn_register})
    public void onViewClicked(View view) {
        mobile = edtRegisterUser.getText().toString();
        switch (view.getId()) {
            case R.id.tv_register_code:
                if (TextUtils.isEmpty(mobile)) {
                    ToastUtils.showToast(ReaderApplication.getsInstance().getResources().getString(R.string.please_input_phone));
                    return;
                }
                tvRegisterCode.setEnabled(false);
                countDown.start();
                CoomonApi.sendsms(this, "register", mobile);
                break;
            case R.id.btn_register:
                register();
                break;
        }
    }

    /**
     * 注册
     */
    private void register() {
        final String pwd = edtRegisterPwd.getText().toString();
        String rePwd = edtResetPwd.getText().toString();
        String code = edtRegisterCode.getText().toString();

        if (TextUtils.isEmpty(mobile)) {
            showToastMsg(getResources().getString(R.string.please_input_phone));
            return;
        }

        if (TextUtils.isEmpty(pwd)) {
            showToastMsg(getResources().getString(R.string.please_input_pwd));
            return;
        }

        if (TextUtils.isEmpty(rePwd)) {
            showToastMsg(getResources().getString(R.string.please_input_re_pwd));
            return;
        }

        if (TextUtils.isEmpty(code)) {
            showToastMsg(getResources().getString(R.string.please_input_code));
            return;
        }

        if (!pwd.equals(rePwd)) {
            showToastMsg(getResources().getString(R.string.pwn_no_repwd));
            return;
        }

        RetrofitClient.getInstance().createApi().register(mobile, pwd, code)
                .compose(RxUtils.<HttpResult<CommonBean>>io_main())
                .subscribe(new BaseObjObserver<CommonBean>(this, "注册中") {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        showToastMsg("注册成功");
                        login(pwd);
                    }
                });
    }

    /**
     * 登录
     */
    private void login(String pwd) {

        RetrofitClient.getInstance().createApi().login(mobile, pwd)
                .compose(RxUtils.<HttpResult<LoginBean>>io_main())
                .subscribe(new BaseObjObserver<LoginBean>(this, "登录中") {
                    @Override
                    protected void onHandleSuccess(LoginBean bean) {
                        loginSuccess(bean);
                    }
                });
    }

    /**
     * 登录成功设置数据
     *
     * @param bean
     */
    private void loginSuccess(LoginBean bean) {
        ReaderApplication.token = bean.getToken();
        //保存用户信息
        ReaderApplication.uid = bean.getUid() + "";
        SettingManager.getInstance().saveUserInfo(bean.getUid() + "", bean.getToken(), bean.getNickname());
        showToastMsg("登录成功");
        MobclickAgent.onProfileSignIn(bean.getUid() + "");
        finish();
    }



    @Override
    public void onStartCount() {

    }

    @Override
    public void onFinishCount() {
        if(tvRegisterCode==null){
            return;
        }
        tvRegisterCode.setEnabled(true);
        tvRegisterCode.setText("重新获取");
    }

    @Override
    public void onUpdateCount(int currentRemainingSeconds) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDown.isRunning()) {
            countDown.stop();
        }
    }

}
