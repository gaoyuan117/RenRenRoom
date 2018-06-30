package com.justwayward.renren.api;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.justwayward.renren.R;
import com.justwayward.renren.ReaderApplication;
import com.justwayward.renren.bean.BookCityCategoryBean;
import com.justwayward.renren.bean.CategoryBean;
import com.justwayward.renren.bean.CommonBean;
import com.justwayward.renren.retrofit.BaseObjObserver;
import com.justwayward.renren.retrofit.HttpResult;
import com.justwayward.renren.retrofit.RetrofitClient;
import com.justwayward.renren.retrofit.RxUtils;
import com.justwayward.renren.ui.activity.CommonActivity;
import com.justwayward.renren.ui.activity.MyVipActivity;
import com.justwayward.renren.ui.activity.login.LoginActivity;
import com.justwayward.renren.ui.fragment.BookCityFragment;
import com.justwayward.renren.utils.LogUtils;
import com.justwayward.renren.utils.ScreenUtils;
import com.justwayward.renren.utils.ToastUtils;

import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by gaoyuan on 2017/11/14.
 */

public class CoomonApi {

    private static AlertDialog dialog;

    /**
     * 获取验证码
     */
    public static void sendsms(Context context, String type, String phone) {
        RetrofitClient.getInstance().createApi().sendsms(phone, type)
                .compose(RxUtils.<HttpResult<CommonBean>>io_main())
                .subscribe(new BaseObjObserver<CommonBean>(context) {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {

                    }
                });
    }

    /**
     * 是否登录
     *
     * @param context
     * @return
     */
    public static boolean isLogin(Context context) {
        if (TextUtils.isEmpty(ReaderApplication.token)) {
            showLoginDialog(context);
            return false;
        }
        return true;
    }

    /**
     * 提示登录对话框
     *
     * @param context
     */
    public static void showLoginDialog(final Context context) {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(context)
                    .setTitle("提示")
                    .setMessage("您还没有登录，是否前往登录页面")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            context.startActivity(new Intent(context, LoginActivity.class).putExtra("type", "login"));
                        }
                    }).create();
        }

        dialog.show();

    }

    /**
     * 判断跳转类型，后台不给判断
     */
    public static void check(final Context context, LinearLayout layout, final String s, final String category_id) {
        String type = "";
        String id = "";
        if (s.equals("热门推荐")) {
            type = "is_recommend";
            id = category_id;
        } else if (s.equals("主编推荐")) {
            type = "main_recommend";
            id = category_id;
        } else if (s.equals("热门")) {
            type = "is_hot";
            id = category_id;
        } else if (s.equals("猜您喜欢的")) {
            type = "";
            id = category_id;
        } else if (s.equals("最新小说")) {
            type = "is_new";
            id = category_id;
        } else {

            if (category_id.startsWith("-")) {
                id = category_id.replace("-", "");

            } else {
                if (BookCityFragment.categoryList == null) {
                    LogUtils.e("分类是空的");
                    return;
                }
                for (int i = 0; i < BookCityFragment.categoryList.size(); i++) {
                    CategoryBean bean = BookCityFragment.categoryList.get(i);
                    if (category_id.equals(bean.getId() + "")) {
                        for (int i1 = 0; i1 < bean.getSub_category().size(); i1++) {
                            if (s.equals(bean.getSub_category().get(i1).getCategory())) {
                                id = bean.getSub_category().get(i1).getId() + "";
                            }
                        }
                    }
                }
            }


        }

        final String finalType = type;
        final String finalId = id;
        final String finalId1 = id;
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(finalType) && TextUtils.isEmpty(finalId)) {
                    LogUtils.e("什么是空的：" + finalType, finalId);
                    return;
                }
                context.startActivity(new Intent(context, CommonActivity.class)
                        .putExtra("type", finalType)
                        .putExtra("title", s)
                        .putExtra("category_id", finalId1)
                        .putExtra("id", finalId));
            }
        });
    }

    /**
     * 分享
     */
    public static void share(final Context context, String name, PlatformActionListener listener) {
        OnekeyShare oks = new OnekeyShare();
        oks.setSilent(true);
        oks.disableSSOWhenAuthorize();
        if (!TextUtils.isEmpty(name)) {
            oks.setPlatform(name);
        }
        oks.setTitle(context.getString(R.string.app_name));
        oks.setText("我正在这里阅读小说，大家一起来啊！");
        oks.setImageUrl("http://novel.jzbwlkj.com/logo.png");

        oks.setUrl(ReaderApplication.shareUrl);
        oks.setSiteUrl(ReaderApplication.shareUrl);
        oks.setTitleUrl(ReaderApplication.shareUrl);

        oks.setSite(context.getString(R.string.app_name));
        oks.setCallback(listener);
        oks.show(context);
    }

    /**
     * 购买会员提示框
     */
    public static void showBuyVipDialog(final Context context) {
        new AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage("当前时间,只有会员才能阅读,是否前往购买页面?")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(context, MyVipActivity.class));
            }
        }).create().show();
    }

    /**
     * 章节付费提示
     */
    public static void buyDialog(final Activity context, double money) {
        new AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage("本章需要" + money + "金币，是否购买？")
                .setCancelable(false)
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                            dialog.dismiss();
                            context.finish();
                        }
                        return false;
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.startActivity(new Intent(context, MyVipActivity.class));
                    }
                }).create().show();
    }

    //打开浏览器
    public static void toBrowser(Context context, String url) {
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            context.startActivity(intent);
        } catch (Exception e) {
            ToastUtils.showToast("链接错误");
        }

    }

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     *
     * @param anchorView  呼出window的view
     * @param contentView window的内容布局
     * @return window显示的左上角的xOff, yOff坐标
     */
    public static int[] calculatePopWindowPos(final View anchorView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = ScreenUtils.getScreenHeight(anchorView.getContext());
        final int screenWidth = ScreenUtils.getScreenWidth(anchorView.getContext());
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
        if (isNeedShowUp) {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] - windowHeight;
        } else {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight;
        }
        return windowPos;
    }

    public static void copy(Activity activity, String s) {
        // 从API11开始android推荐使用android.content.ClipboardManager
        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
        ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(s);
        ToastUtils.showToast("复制成功");
    }


}
