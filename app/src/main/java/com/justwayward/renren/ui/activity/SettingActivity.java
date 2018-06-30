package com.justwayward.renren.ui.activity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.justwayward.renren.R;
import com.justwayward.renren.ReaderApplication;
import com.justwayward.renren.base.BaseActivity;
import com.justwayward.renren.base.Constant;
import com.justwayward.renren.bean.BookShelfBean;
import com.justwayward.renren.bean.CommonBean;
import com.justwayward.renren.bean.HistoryBeanDao;
import com.justwayward.renren.component.AppComponent;
import com.justwayward.renren.component.DaggerMainComponent;
import com.justwayward.renren.manager.CacheManager;
import com.justwayward.renren.manager.EventManager;
import com.justwayward.renren.manager.SettingManager;
import com.justwayward.renren.retrofit.BaseObjObserver;
import com.justwayward.renren.retrofit.HttpResult;
import com.justwayward.renren.retrofit.RetrofitClient;
import com.justwayward.renren.retrofit.RxUtils;
import com.justwayward.renren.ui.activity.login.LoginActivity;
import com.justwayward.renren.utils.LogUtils;
import com.justwayward.renren.utils.SharedPreferencesUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by xiaoshu on 2016/10/8.
 */
public class SettingActivity extends BaseActivity {

    private HistoryBeanDao historyBeanDao;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }

    @Bind(R.id.mTvSort)
    TextView mTvSort;
    @Bind(R.id.tvFlipStyle)
    TextView mTvFlipStyle;
    @Bind(R.id.tvCacheSize)
    TextView mTvCacheSize;
    @Bind(R.id.noneCoverCompat)
    SwitchCompat noneCoverCompat;
    @Bind(R.id.img_notify)
    ImageView imgNotify;

    private String[] sort = {"update", "read", "name"};


    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setTitle("设置");
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
    }

    @Override
    public void initDatas() {

        historyBeanDao = ReaderApplication.getDaoInstant().getHistoryBeanDao();

        try {
            mTvSort.setText(sort(ReaderApplication.user.getBookshelf_sort()));
        } catch (Exception e) {

        }
        setCache();

        mTvFlipStyle.setText(getResources().getStringArray(R.array.setting_dialog_style_choice)[
                SharedPreferencesUtil.getInstance().getInt(Constant.FLIP_STYLE, 0)]);

        boolean aBoolean = SharedPreferencesUtil.getInstance().getBoolean(Constant.ISOPENNOTIFY);
        if (aBoolean){
            imgNotify.setImageDrawable(getDrawable(R.mipmap.notify_open));
        }else {
            imgNotify.setImageDrawable(getDrawable(R.mipmap.notify_close));
        }

    }

    private void setCache(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String cachesize = CacheManager.getInstance().getCacheSize();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvCacheSize.setText(cachesize);
                    }
                });

            }
        }).start();
    }


    @Override
    public void configViews() {
        noneCoverCompat.setChecked(SettingManager.getInstance().isNoneCover());
        noneCoverCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingManager.getInstance().saveNoneCover(isChecked);
            }
        });
    }

    @OnClick({R.id.bookshelfSort})
    public void onClickBookShelfSort() {
        int a = SharedPreferencesUtil.getInstance().getInt(Constant.ISBYUPDATESORT, 0);
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("书架排序方式")
                .setSingleChoiceItems(getResources().getStringArray(R.array.setting_dialog_sort_choice), a,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String type = getResources().getStringArray(R.array.setting_dialog_sort_choice)[which];
                                mTvSort.setText(type);

                                setBookshelfSort(sort[which]);

                                SharedPreferencesUtil.getInstance().putInt(Constant.ISBYUPDATESORT, which);
                                dialog.dismiss();
                            }
                        })
                .create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @OnClick({R.id.bookshelfNotify})
    public void onClickBookShelfNotify() {
        int a = SharedPreferencesUtil.getInstance().getInt(Constant.NOTIFYPOSITION, 0);
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("阅读休息提醒")
                .setSingleChoiceItems(getResources().getStringArray(R.array.setting_dialog_notify_choice), a,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferencesUtil.getInstance().putInt(Constant.NOTIFYPOSITION, which);

                                String type = getResources().getStringArray(R.array.setting_dialog_notify_choice)[which];
                                LogUtils.e("阅读休息：" + type);
                                if (type.equals("关闭")) {
                                    SharedPreferencesUtil.getInstance().putBoolean(Constant.ISOPENNOTIFY, false);
                                    imgNotify.setImageDrawable(getDrawable(R.mipmap.notify_close));
                                } else {
                                    SharedPreferencesUtil.getInstance().putBoolean(Constant.ISOPENNOTIFY, true);
                                    imgNotify.setImageDrawable(getDrawable(R.mipmap.notify_open));
                                }
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @OnClick(R.id.feedBack)
    public void feedBack() {
        FeedbackActivity.startActivity(this);
    }

    @OnClick(R.id.exit)
    public void exit() {//退出
        SharedPreferencesUtil.getInstance().putString("token", "");
        ReaderApplication.token = "";
        MobclickAgent.onProfileSignOff();
        startActivity(new Intent(this, LoginActivity.class).putExtra("type", "relogin"));
    }

    @OnClick(R.id.aboutUs)
    public void aboutUs() {
        startActivity(new Intent(this, AboutUsActivity.class));
    }

    @OnClick(R.id.changePwd)
    public void changePwd() {
        startActivity(new Intent(this, ChangePwdActivity.class));
    }

    @OnClick(R.id.cleanCache)
    public void onClickCleanCache() {
        //默认不勾选清空书架列表，防手抖！！
        final boolean selected[] = {true, false};
        new AlertDialog.Builder(mContext)
                .setTitle("清除缓存")
                .setCancelable(true)
                .setMultiChoiceItems(new String[]{"删除阅读记录", "清空书架列表"}, selected, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        selected[which] = isChecked;
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 清空书架
                                if (selected[1]) {
                                    ReaderApplication.getDaoInstant().getBookShelfCacheDao().deleteAll();
                                    getList();
                                }
                                historyBeanDao.deleteAll();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        CacheManager.getInstance().clearCache(selected[0], selected[1]);
                                        final String cachesize = CacheManager.getInstance().getCacheSize();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mTvCacheSize.setText(cachesize);
                                            }
                                        });

                                    }
                                }).start();
                            }
                        }).start();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    private void getList() {
        RetrofitClient.getInstance().createApi().getList(ReaderApplication.token)
                .compose(RxUtils.<HttpResult<BookShelfBean>>io_main())
                .subscribe(new BaseObjObserver<BookShelfBean>(this, false) {
                    @Override
                    protected void onHandleSuccess(BookShelfBean bookShelfBean) {
                        if (bookShelfBean.getData() == null || bookShelfBean.getData().size() <= 0) {
                            return;
                        }
                        String ids = "";
                        for (int i = 0; i < bookShelfBean.getData().size(); i++) {
                            ids += bookShelfBean.getData().get(i).getNovel_id() + ",";
                        }
                        delNovel(ids);
                    }
                });
    }

    /**
     * 移除书架
     */
    private void delNovel(String ids) {
        RetrofitClient.getInstance().createApi().delBook(ReaderApplication.token, ids)
                .compose(RxUtils.<HttpResult<CommonBean>>io_main())
                .subscribe(new BaseObjObserver<CommonBean>(this, false) {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        getList();
                    }
                });
    }

    /**
     * 设置书架排序规则
     */
    private void setBookshelfSort(final String type) {
        RetrofitClient.getInstance().createApi().setBookshelfSort(ReaderApplication.token, type)
                .compose(RxUtils.<HttpResult<CommonBean>>io_main())
                .subscribe(new BaseObjObserver<CommonBean>(this) {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        showToastMsg("设置成功");
                        ReaderApplication.user.setBookshelf_sort(type);
                    }
                });
    }

    private String sort(String type) {
        if (type.equals("update")) {
            return "按更新时间";
        } else if (type.equals("read")) {
            return "按最近阅读";
        } else if (type.equals("name")) {
            return "按书籍名称排序";
        }
        return "";
    }
}
