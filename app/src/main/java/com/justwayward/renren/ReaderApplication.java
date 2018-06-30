package com.justwayward.renren;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.baidu.mobads.AdSettings;
import com.baidu.mobads.AppActivity;
import com.iflytek.cloud.SpeechUtility;
import com.justwayward.renren.base.CrashHandler;
import com.justwayward.renren.bean.AboutBean;
import com.justwayward.renren.bean.DaoMaster;
import com.justwayward.renren.bean.DaoSession;
import com.justwayward.renren.bean.UserBean;
import com.justwayward.renren.component.AppComponent;
import com.justwayward.renren.component.DaggerAppComponent;
import com.justwayward.renren.module.AppModule;
import com.justwayward.renren.module.BookApiModule;
import com.justwayward.renren.utils.AppUtils;
import com.justwayward.renren.utils.SharedPreferencesUtil;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.analytics.MobclickAgent;

/**
 * @author yuyh.
 * @date 2016/8/3.
 */
public class ReaderApplication extends MultiDexApplication {

    private static ReaderApplication sInstance;
    private AppComponent appComponent;

    public static String token;
    public static String uid;
    public static String dushi;//都市
    public static String xianyan;//现言
    public static String qita;//其他
    public static int vipPosition;//其他
    public static UserBean user;
    private static DaoSession daoSession;
    public static int speed = 50;
    public static String vocher = "xiaoyan";
    public static String shareUrl;
    public static float wordsPrice;
    public static long days;//注册的时间
    public static boolean isvip = false;
    public static boolean net = false;
    public static AboutBean aboutBean ;
    private RefWatcher refWatcher;


    public static RefWatcher getRefWatcher(Context context) {
        ReaderApplication application = (ReaderApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType. E_UM_NORMAL);
        MultiDex.install(this);
        sInstance = this;
        initCompoent();
        AppUtils.init(this);
        refWatcher = LeakCanary.install(this);
        CrashHandler.getInstance().init(this);
        initPrefs();
        initNightMode();
        SpeechUtility.createUtility(this, "appid=" + "5a1a5dc3");
        String token = SharedPreferencesUtil.getInstance().getString("token");
        if (!TextUtils.isEmpty(token)) {
            ReaderApplication.token = token;
        }
        setupDatabase();

        AppActivity.setActionBarColorTheme(AppActivity.ActionBarColorTheme.ACTION_BAR_GREEN_THEME);
        AdSettings.setSupportHttps(true);

    }

    public static ReaderApplication getsInstance() {
        return sInstance;
    }

    private void initCompoent() {
        appComponent = DaggerAppComponent.builder()
                .bookApiModule(new BookApiModule())
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    /**
     * 初始化SharedPreference
     */
    protected void initPrefs() {
        SharedPreferencesUtil.init(getApplicationContext(), getPackageName() + "_preference", Context.MODE_MULTI_PROCESS);
    }

    protected void initNightMode() {
//        boolean isNight = SharedPreferencesUtil.getInstance().getBoolean(Constant.ISNIGHT, false);
//        LogUtils.d("isNight=" + isNight);
//        if (isNight) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        } else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        }
    }

    /**
     * 配置数据库
     */
    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "GreenDao.db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoInstant() {
        return daoSession;
    }



}
