package com.justwayward.renren.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.justwayward.renren.R;
import com.justwayward.renren.ReaderApplication;
import com.justwayward.renren.base.BaseActivity;
import com.justwayward.renren.bean.CommonBean;
import com.justwayward.renren.component.AppComponent;
import com.justwayward.renren.retrofit.BaseObjObserver;
import com.justwayward.renren.retrofit.HttpResult;
import com.justwayward.renren.retrofit.RetrofitClient;
import com.justwayward.renren.retrofit.RxUtils;

import butterknife.Bind;
import butterknife.OnClick;

public class ChangePwdActivity extends BaseActivity {


    @Bind(R.id.edt_old_pwd)
    EditText edtOldPwd;
    @Bind(R.id.edt_forget_new)
    EditText edtForgetNew;
    @Bind(R.id.edt_forget_re_pwd)
    EditText edtForgetRePwd;
    @Bind(R.id.btn_forget)
    Button btnForget;

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_pwd;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
        mCommonToolbar.setTitle("修改密码");
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews() {
    }

    @OnClick({R.id.btn_forget})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.btn_forget:
                changePwd();
                break;
        }
    }


    /**
     * 找回密码
     */
    private void changePwd() {
        String oldPwd = edtOldPwd.getText().toString();
        String newPwd = edtForgetNew.getText().toString();
        String reNewPwd = edtForgetRePwd.getText().toString();


        if (TextUtils.isEmpty(oldPwd)) {
            showToastMsg("请输入原密码");
            return;
        }

        if (TextUtils.isEmpty(newPwd)) {
            showToastMsg("请输入新密码");
            return;
        }

        if (TextUtils.isEmpty(reNewPwd)) {
            showToastMsg("请确认新密码");
            return;
        }

        if (!newPwd.equals(reNewPwd)) {
            showToastMsg(getResources().getString(R.string.pwn_no_repwd));
            return;
        }

        RetrofitClient.getInstance().createApi().modifypwd(ReaderApplication.token, oldPwd, newPwd)
                .compose(RxUtils.<HttpResult<CommonBean>>io_main())
                .subscribe(new BaseObjObserver<CommonBean>(this, "修改中") {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        showToastMsg("密码修改成功");
                        finish();
                    }
                });
    }

}
