package com.justwayward.renren.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.justwayward.renren.R;
import com.justwayward.renren.ReaderApplication;
import com.justwayward.renren.api.CoomonApi;
import com.justwayward.renren.base.BaseActivity;
import com.justwayward.renren.base.Constant;
import com.justwayward.renren.bean.BookBean;
import com.justwayward.renren.bean.BookSourceBean;
import com.justwayward.renren.bean.BookSourceBeanDao;
import com.justwayward.renren.bean.CalPriceBean;
import com.justwayward.renren.bean.ChapterBean;
import com.justwayward.renren.bean.ChapterList;
import com.justwayward.renren.bean.ChapterListBean;
import com.justwayward.renren.bean.ChapterListDao;
import com.justwayward.renren.bean.CommonBean;
import com.justwayward.renren.bean.HistoryBean;
import com.justwayward.renren.bean.HistoryBeanDao;
import com.justwayward.renren.bean.ReadBook;
import com.justwayward.renren.bean.ReadBookDao;
import com.justwayward.renren.bean.TtsThemeBean;
import com.justwayward.renren.bean.UserBean;
import com.justwayward.renren.bean.support.BookMark;
import com.justwayward.renren.bean.support.DownloadMessage;
import com.justwayward.renren.bean.support.DownloadProgress;
import com.justwayward.renren.bean.support.DownloadQueue;
import com.justwayward.renren.bean.support.ReadTheme;
import com.justwayward.renren.component.AppComponent;
import com.justwayward.renren.component.DaggerBookComponent;
import com.justwayward.renren.manager.CacheManager;
import com.justwayward.renren.manager.CollectionsManager;
import com.justwayward.renren.manager.EventManager;
import com.justwayward.renren.manager.SettingManager;
import com.justwayward.renren.manager.ThemeManager;
import com.justwayward.renren.retrofit.BaseObjObserver;
import com.justwayward.renren.retrofit.HttpResult;
import com.justwayward.renren.retrofit.RetrofitClient;
import com.justwayward.renren.retrofit.RxUtils;
import com.justwayward.renren.service.DownloadBookService;
import com.justwayward.renren.ui.adapter.BookMarkAdapter;
import com.justwayward.renren.ui.adapter.ThemeAdapter;
import com.justwayward.renren.ui.adapter.TocListAdapter;
import com.justwayward.renren.ui.adapter.TtsAdapter;
import com.justwayward.renren.ui.contract.BookReadContract;
import com.justwayward.renren.ui.presenter.BookReadPresenter;
import com.justwayward.renren.utils.AppUtils;
import com.justwayward.renren.utils.LogUtils;
import com.justwayward.renren.utils.ScreenUtils;
import com.justwayward.renren.utils.SharedPreferencesUtil;
import com.justwayward.renren.utils.ToastUtils;
import com.justwayward.renren.utils.TtsUtis;
import com.justwayward.renren.view.readview.OnReadStateChangeListener;
import com.justwayward.renren.view.readview.OverlappedWidget;
import com.justwayward.renren.view.readview.PageWidget;
import com.justwayward.renren.view.readview.ReadInterface;
import com.justwayward.renren.view.readview.UpDownWidget;
import com.justwayward.renren.view.recyclerview.decoration.DividerDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class ReadActivity extends BaseActivity implements BookReadContract.View, View.OnClickListener, PlatformActionListener {

    @Bind(R.id.ivBack)
    ImageView mIvBack;
    @Bind(R.id.tvBookReadReading)
    TextView mTvBookReadReading;
    @Bind(R.id.tv)
    TextView tv;
    @Bind(R.id.tvBookReadCommunity)
    TextView mTvBookReadCommunity;
    @Bind(R.id.tvBookReadIntroduce)
    TextView mTvBookReadChangeSource;
    @Bind(R.id.tvBookReadSource)
    TextView mTvBookReadSource;
    @Bind(R.id.flReadWidget)
    FrameLayout flReadWidget;
    @Bind(R.id.llBookReadTop)
    LinearLayout mLlBookReadTop;
    @Bind(R.id.ll_top)
    LinearLayout ll_top;
    @Bind(R.id.tvBookReadTocTitle)
    TextView mTvBookReadTocTitle;
    @Bind(R.id.tvBookReadMode)
    TextView mTvBookReadMode;
    @Bind(R.id.tvBookReadSettings)
    TextView mTvBookReadSettings;
    @Bind(R.id.tvBookReadDownload)
    TextView mTvBookReadDownload;
    @Bind(R.id.tvBookReadToc)
    TextView mTvBookReadToc;
    @Bind(R.id.llBookReadBottom)
    LinearLayout mLlBookReadBottom;
    @Bind(R.id.rlBookReadRoot)
    RelativeLayout mRlBookReadRoot;
    @Bind(R.id.tvDownloadProgress)
    TextView mTvDownloadProgress;
    @Bind(R.id.rlReadAaSet)
    LinearLayout rlReadAaSet;
    @Bind(R.id.ivBrightnessMinus)
    ImageView ivBrightnessMinus;
    @Bind(R.id.seekbarLightness)
    SeekBar seekbarLightness;
    @Bind(R.id.ivBrightnessPlus)
    ImageView ivBrightnessPlus;
    @Bind(R.id.tvFontsizeMinus)
    TextView tvFontsizeMinus;
    @Bind(R.id.seekbarFontSize)
    SeekBar seekbarFontSize;
    @Bind(R.id.tvFontsizePlus)
    TextView tvFontsizePlus;
    @Bind(R.id.rlReadMark)
    LinearLayout rlReadMark;
    @Bind(R.id.tvAddMark)
    TextView tvAddMark;
    @Bind(R.id.lvMark)
    ListView lvMark;
    @Bind(R.id.cbVolume)
    CheckBox cbVolume;
    @Bind(R.id.cbAutoBuy)
    CheckBox cbAutoBuy;
    @Bind(R.id.cbAutoBrightness)
    CheckBox cbAutoBrightness;
    @Bind(R.id.gvTheme)
    GridView gvTheme;
    @Bind(R.id.tv_over)
    TextView tvOver;
    @Bind(R.id.tv_no)
    TextView tvNo;
    @Bind(R.id.tv_page)
    TextView tvPage;
    @Bind(R.id.tv_source)
    TextView tv_source;
    @Bind(R.id.tvMore)
    TextView tvMore;
    @Bind(R.id.sb_chapter)
    SeekBar seekBarChapter;
    @Bind(R.id.tv_pre_chapter)
    TextView tvPreChapter;
    @Bind(R.id.tv_next_chapter)
    TextView tvNextChapter;
    @Bind(R.id.tvFontSize)
    TextView tvFontSize;
    @Bind(R.id.tv_auto_light)
    TextView tvAutoLight;
    @Bind(R.id.tv_font_type)
    TextView tvFontType;
    @Bind(R.id.rv_theme)
    RecyclerView rvTheme;

    private View decodeView;
    private SharedPreferences sp;
    private int fontSize = 18;
    private int oldPercent;
    private int readProgressBegin;
    private int readProgressEnd;
    private double oldPlayNum;

    @Inject
    BookReadPresenter mPresenter;

    private List<ChapterListBean> mChapterList = new ArrayList<>();
    private ListPopupWindow mTocListPopupWindow;
    private TocListAdapter mTocListAdapter;
    private List<BookMark> mMarkList;
    private BookMarkAdapter mMarkAdapter;
    private int currentChapter = 0;

    /**
     * 是否开始阅读章节
     **/
    private boolean startRead = false;
    private ReadInterface mPageWidget;
    private int curTheme = -1;
    private List<ReadTheme> themes;
    //    private ReadThemeAdapter gvAdapter;
    private Receiver receiver = new Receiver();
    private IntentFilter intentFilter = new IntentFilter();
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    public static final String INTENT_BEAN = "recommendBooksBean";
    public static final String INTENT_SD = "isFromSD";

    private String title, des, author, bookId;

    private boolean isAutoLightness = false; // 记录其他页面是否自动调整亮度
    private boolean isFromSD = false;
    private boolean isShelf = false;
    public static TtsUtis ttsUtis;
    private String pic;//书籍图片
    private HistoryBeanDao historyBeanDao;
    private List<TtsThemeBean> list = new ArrayList<>();
    private boolean ttsIsChange = false;
    private PopupWindow window;
    private PopupWindow timeWindow;
    private int time;//定时器
    private Disposable disposable, readDisposable;
    private TextView tvTime;
    private SeekBar speedSeekBar;
    private RecyclerView ttsRecycler;
    private TextView tvCloseTts;
    private TtsAdapter ttsAdapter;
    private AlertDialog shareDialog;
    private PopupWindow sharePopWindow;

    private String currentDate;
    private ChapterListDao chapterListDao;
    private BookSourceBeanDao bookSourceBeanDao;
    private ReadBookDao readBookDao;
    private String source, source_url;
    private PopupWindow morePopWindow;
    private AlertDialog buyDialog;
    private AlertDialog.Builder buyBuilder;
    private PopupWindow buyWindow;
    private double coin;
    private int cMoney;
    private boolean isPay = false;
    private int cChapter;
    private View buyPopView;
    private TextView buyPopTvPay;
    private TextView buyPopTvYue;
    private TextView buyPopTvPre;
    private TextView buyPopTvNext;
    private Button buyPopBtBuy;
    private CheckBox buyPopCb;
    private LinearLayout llBuyPop;
    private TextView buyTvClose;
    private TextView buyTvTopic;
    private Button buyPopBtVip;
    private String novel_type;
    private boolean user_member;
    private View downPopView;
    private ViewHolder downViewHolder;
    private PopupWindow downWindow;
    //    int downNum = 0;
    int hasBuy = 0;
    int noBuy = 0;
    double totalWord = 0;
    private BigDecimal real;
    private PopupWindow sharePopWindow2;
    private int notify;
    private Disposable notifyDisposable;
    private ThemeAdapter adapter;

    //添加收藏需要，所以跳转的时候传递整个实体类
    public static void startActivity(Context context, String title, String novelId, boolean isShelf, String pic, String author, String des) {
        startActivity(context, title, novelId, isShelf, false, pic, author, des);
    }

    public static void startActivity(Context context, String title, String novelId, boolean isShelf, boolean isFromSD, String pic, String author, String des) {
        context.startActivity(new Intent(context, ReadActivity.class)
                .putExtra("tile", title)
                .putExtra("novel_id", novelId)
                .putExtra("isShelf", isShelf)
                .putExtra("pic", pic)
                .putExtra("des", des)
                .putExtra("author", author)
                .putExtra(INTENT_SD, isFromSD));
    }

    @Override
    public int getLayoutId() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        statusBarColor = ContextCompat.getColor(this, R.color.reader_menu_bg_color);
        return R.layout.activity_read;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerBookComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initDatas() {
        //阅读休息提醒
        notify = SharedPreferencesUtil.getInstance().getInt(Constant.NOTIFYPOSITION);
        notifytTime();

        sp = getSharedPreferences("tts", MODE_PRIVATE);

        addTtsList();
        historyBeanDao = ReaderApplication.getDaoInstant().getHistoryBeanDao();
        chapterListDao = ReaderApplication.getDaoInstant().getChapterListDao();
        bookSourceBeanDao = ReaderApplication.getDaoInstant().getBookSourceBeanDao();
        readBookDao = ReaderApplication.getDaoInstant().getReadBookDao();

        isFromSD = getIntent().getBooleanExtra(INTENT_SD, false);
        bookId = getIntent().getStringExtra("novel_id");
        title = getIntent().getStringExtra("tile");
        isShelf = getIntent().getBooleanExtra("isShelf", false);
        pic = getIntent().getStringExtra("pic");
        des = getIntent().getStringExtra("des");
        author = getIntent().getStringExtra("author");
        updateReadTime();

        boolean auto_buy = sp.getBoolean(bookId, false);
        cbAutoBuy.setChecked(auto_buy);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        showDialog();

        mTvBookReadTocTitle.setText(title);

        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);

        CollectionsManager.getInstance().setRecentReadingTime(bookId);

    }

    @Override
    public void configViews() {
        hideStatusBar();

        decodeView = getWindow().getDecorView();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLlBookReadTop.getLayoutParams();
        params.topMargin = ScreenUtils.getStatusBarHeight(this) - 2;
        mLlBookReadTop.setLayoutParams(params);

        initTocList();

        initAASet();

        initPagerWidget();

        //获余额
        getUser();

        mPresenter.attachView(this);

        if (!ReaderApplication.net) {//无网络
            try {

                ChapterBean chapterBean = null;
                mChapterList.clear();
                List<ChapterList> list1 = chapterListDao.queryBuilder().where(ChapterListDao.Properties.BookId.eq(bookId)).build().list();
                if (list1 != null && list1.size() > 0) {
                    for (int i = 0; i < list1.size(); i++) {
                        chapterBean = new Gson().fromJson(list1.get(i).getJs(), ChapterBean.class);
                    }
                    if (chapterBean.list == null) {
                        return;
                    }

                    int pos[] = SettingManager.getInstance().getReadProgress(bookId);
                    for (int i = 0; i < list.size(); i++) {
                        if (chapterBean.list.get(i).getId() == pos[0]) {
                            currentChapter = i;
                        }
                    }
                    initChapterSeekBar(mChapterList.size(), currentChapter);
                    mChapterList.addAll(chapterBean.list);
                    readCurrentChapter();
                } else {
                    hideDialog();
                    ToastUtils.showToast("暂无缓存");
                }
            } catch (Exception e) {
                hideDialog();
                ToastUtils.showToast("暂无缓存");
            }

        } else {
            mPresenter.getBookMixAToc(bookId, "chapters");
        }
    }

    private void initChapterSeekBar(int max, int progress) {
        seekBarChapter.setMax(max - 1);
        seekBarChapter.setProgress(progress);
        seekBarChapter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                hideReadBar();
                startRead = false;
                currentChapter = seekBar.getProgress();
                LogUtils.e("当前章节：" + mChapterList.get(currentChapter).getId());
                readCurrentChapter();
            }
        });
    }

    /**
     * 历史记录
     */
    private void addHistory() {

        HistoryBean bean = new HistoryBean();
        bean.setBookId(bookId);
        bean.setPic(pic);
        bean.setTitle(title);
        try {
            bean.setAuthor(mChapterList.get(currentChapter).getChapter() + "-" + author);
        } catch (Exception e) {
            bean.setAuthor(" " + "-" + author);
        }
        bean.setDes(des);
        bean.setIsShelf(isShelf);
        historyBeanDao.insertOrReplace(bean);
    }

    /**
     * 目录
     */
    private void initTocList() {
        mTocListAdapter = new TocListAdapter(this, mChapterList, bookId, currentChapter);
        mTocListPopupWindow = new ListPopupWindow(this);
        mTocListPopupWindow.setAdapter(mTocListAdapter);
        mTocListPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mTocListPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mTocListPopupWindow.setAnchorView(ll_top);
        mTocListPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("gy", "position：" + position);
                mTocListPopupWindow.dismiss();
                if (buyWindow != null && buyWindow.isShowing()) {
                    CacheManager.getInstance().delChapterFile(bookId, cChapter);
                    buyWindow.dismiss();
                }

                currentChapter = position;
                mTocListAdapter.setCurrentChapter(position);
                startRead = false;
                showDialog();
                readCurrentChapter();
                hideReadBar();
            }
        });
        mTocListPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                gone(mTvBookReadTocTitle);
                visible(mTvBookReadReading, mTvBookReadSource, tvMore);
            }
        });

    }

    /**
     * 时刻监听系统亮度改变事件
     */
    private ContentObserver Brightness = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            //LogUtils.d("BrightnessOnChange:" + ScreenUtils.getScreenBrightnessInt255());
            try {

            } catch (Exception e) {
                if (!ScreenUtils.isAutoBrightness(ReadActivity.this)) {
                    seekbarLightness.setProgress(ScreenUtils.getScreenBrightness());
                }
            }

        }
    };

    /**
     * 字体
     */
    private void initAASet() {

        curTheme = SettingManager.getInstance().getReadTheme();
        ThemeManager.setReaderTheme(curTheme, mRlBookReadRoot);

        seekbarFontSize.setMax(15);
        //int fontSizePx = SettingManager.getInstance().getReadFontSize(bookId);

        fontSize = SettingManager.getInstance().getReadFontSize();
        int progress = (int) ((ScreenUtils.pxToDpInt(fontSize) - 12) / 1.7f);
        LogUtils.e("progress:" + progress);

        seekbarFontSize.setProgress(progress + 1);
//        tvFontSize.setText((int) ScreenUtils.pxToSp(ScreenUtils.dpToPxInt(12 + 1.7f * progress)) + "");
        tvFontSize.setText(fontSize + "");
        seekbarFontSize.setOnSeekBarChangeListener(new SeekBarChangeListener());

        seekbarLightness.setMax(100);
        seekbarLightness.setOnSeekBarChangeListener(new SeekBarChangeListener());
        seekbarLightness.setProgress(ScreenUtils.getScreenBrightness());
        isAutoLightness = ScreenUtils.isAutoBrightness(this);


        this.getContentResolver().registerContentObserver(Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS), true, Brightness);

        if (SettingManager.getInstance().isAutoBrightness()) {
            startAutoLightness();
        } else {
            stopAutoLightness();
        }

        cbVolume.setChecked(SettingManager.getInstance().isVolumeFlipEnable());
        cbVolume.setOnCheckedChangeListener(new ChechBoxChangeListener());

        isAutoLight(SettingManager.getInstance().isAutoBrightness());
        cbAutoBrightness.setChecked(SettingManager.getInstance().isAutoBrightness());
        cbAutoBrightness.setOnCheckedChangeListener(new ChechBoxChangeListener());
        cbAutoBuy.setOnCheckedChangeListener(new ChechBoxChangeListener());

        changeGridView();

    }

    private void changeGridView() {

        adapter = new ThemeAdapter(R.layout.item_read_theme, (themes = ThemeManager.getReaderThemeData(curTheme)), curTheme);
        rvTheme.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvTheme.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position < themes.size() - 1) {
                    changedMode(false, position);
                } else {
                    changedMode(true, position);
                }
            }
        });
    }

    /**
     * 初始化阅读器
     */
    private void initPagerWidget() {
        int cache = SharedPreferencesUtil.getInstance().getInt(Constant.FLIP_STYLE, 1);
        switch (cache) {
            case 0:
                tvPage.setSelected(true);
                tvNo.setSelected(false);
                tvOver.setSelected(false);
                mPageWidget = new PageWidget(this, bookId, mChapterList, new ReadListener());
                break;
            case 1:
                tvPage.setSelected(false);
                tvNo.setSelected(false);
                tvOver.setSelected(true);
                mPageWidget = new OverlappedWidget(this, bookId, mChapterList, new ReadListener());
                break;
            case 2:
                tvPage.setSelected(false);
                tvNo.setSelected(true);
                tvOver.setSelected(false);
                mPageWidget = new UpDownWidget(this, bookId, mChapterList, new ReadListener());
            default:
                break;
        }

        registerReceiver(receiver, intentFilter);
        if (SharedPreferencesUtil.getInstance().getBoolean(Constant.ISNIGHT, false)) {
            mPageWidget.setTextColor(ContextCompat.getColor(this, R.color.chapter_content_night),
                    ContextCompat.getColor(this, R.color.chapter_title_night));
        }
        flReadWidget.removeAllViews();
        flReadWidget.addView((View) mPageWidget);


    }

    /**
     * 加载章节列表AppCompatDelegate
     *
     * @param list
     */
    @Override
    public void showBookToc(List<ChapterListBean> list) {
        mChapterList.clear();
        mChapterList.addAll(list);
        int pos[] = SettingManager.getInstance().getReadProgress(bookId);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == pos[0]) {
                currentChapter = i;
            }
        }
        initChapterSeekBar(mChapterList.size(), currentChapter);
        readCurrentChapter();
        ChapterBean bean = new ChapterBean();
        bean.list = list;
        ChapterList chapter = new ChapterList();
        chapter.setBookId(bookId);
        chapter.setJs(new Gson().toJson(bean));
        chapterListDao.insertOrReplace(chapter);
    }

    /**
     * 获取当前章节。章节文件存在则直接阅读，不存在则请求
     */
    public void readCurrentChapter() {
        File chapterFile = CacheManager.getInstance().getChapterFile(bookId, mChapterList.get(currentChapter).getId());
        if (chapterFile != null && chapterFile.length() > 10) {
            showChapterRead(null, mChapterList.get(currentChapter).getId());
            LogUtils.e("当前位置：" + currentChapter);
        } else {
            mPresenter.getChapterRead(mChapterList.get(currentChapter).getId() + "", source);
        }
    }

    @Override
    public synchronized void showChapterRead(BookBean bookBean, int chapter) {
        // 加载章节内容
        //小说类型 取值 collect 采集小说 chagndu 畅读书城 huiyuan 会员小说 other 其他小说

        List<ReadBook> readList = readBookDao.queryBuilder().where(ReadBookDao.Properties.ChapterId.eq(chapter + "")).build().list();

        if (bookBean != null) {//没有缓存过
            ReadBook readBook = new ReadBook();
            readBook.setId(Long.valueOf(chapter));
            readBook.setBookId(bookId);
            readBook.setChapterId(chapter + "");
            readBook.setType(bookBean.getNovel_type());

            novel_type = bookBean.getNovel_type();
            if (bookBean.getIs_vip() == 0) {//免费章节
                readBook.setIsbuy(true);
                CacheManager.getInstance().saveChapterFile(bookId, chapter, bookBean);
                showBookContent(bookBean, chapter);
            } else if (bookBean.getIs_vip() == 1) {//付费章节
                readBook.setIsbuy(bookBean.isUser_pay());
                if (novel_type.equals("changdu")) {//畅读书城。可以包月看，也可以按章节看
                    if (!bookBean.isUser_member()) {//不是会员
                        if (!bookBean.isUser_pay()) {//没有购买过
                            int money = 0;
                            int word_num = bookBean.getWord_num();
                            BigDecimal b = new BigDecimal(word_num * 0.001 * ReaderApplication.wordsPrice).setScale(0, BigDecimal.ROUND_HALF_UP);
                            money = b.intValue();
                            LogUtils.e("章节价格：" + money + "   章节字数：" + (word_num));
                            final boolean auto_buy = sp.getBoolean(bookId, false);
                            if (auto_buy) {
                                payChapter("buy", chapter, money, 1);
                            } else {
                                showBuyPop(chapter, money, 1);
                            }

                            showBookContent(bookBean, chapter);
                        } else {//购买过
                            showBookContent(bookBean, chapter);
                        }
                    } else {//是会员
                        showBookContent(bookBean, chapter);
                    }
                }
                if (novel_type.equals("huiyuan") || novel_type.equals("collect")) {//不是其他和畅读书城，需要按章节购买
                    if (!bookBean.isUser_pay()) {//没有购买过
                        int money = 0;
                        int word_num = bookBean.getWord_num();
                        //  四舍五入
                        BigDecimal b = new BigDecimal(word_num * 0.001 * ReaderApplication.wordsPrice).setScale(0, BigDecimal.ROUND_HALF_UP);
                        money = b.intValue();
                        LogUtils.e("章节价格：" + money + "   章节字数：" + (word_num));
                        final boolean auto_buy = sp.getBoolean(bookId, false);
                        if (auto_buy) {
                            payChapter("buy", chapter, money, 1);
                        } else {
                            showBuyPop(chapter, money, 1);
                        }

                        showBookContent(bookBean, chapter);
                    } else {//购买过
                        showBookContent(bookBean, chapter);
                    }
                }
            }

            if (novel_type.equals("other")) {//其他小说，免费阅读
                showBookContent(bookBean, chapter);
            }

            readBookDao.insertOrReplace(readBook);
            CacheManager.getInstance().saveChapterFile(bookId, chapter, bookBean);

        } else {//缓存过

            final int pos[] = SettingManager.getInstance().getReadProgress(bookId);
            novel_type = readList.get(0).getType();
            if (!ReaderApplication.net) {//无网络

                if (mChapterList.get(currentChapter).getIs_vip() == 1 && mChapterList.get(currentChapter).getIs_pay() == 0) {//缓存了 但是没有购买，无法阅读
                    LogUtils.e("缓存了 但是没有购买，无法阅读");
                    final BigDecimal b = new BigDecimal(mChapterList.get(currentChapter).getWord_num() * 0.001 * ReaderApplication.wordsPrice).setScale(0, BigDecimal.ROUND_HALF_UP);
                    if (buyWindow != null) {
                        showBuyPop(pos[0], b.intValue(), 1);
                    }
                } else {

                    if (buyWindow != null && buyWindow.isShowing()) {
                        buyWindow.dismiss();
                    }
                }
            } else {
                if (mChapterList.get(currentChapter).getIs_pay() == 1 && mChapterList.get(currentChapter).getIs_pay() == 0) {//缓存了 但是没有购买，无法阅读
                    mPresenter.getChapterRead(+pos[0] + "", source);
                    return;
                }
                if (buyWindow != null && buyWindow.isShowing()) {
                    buyWindow.dismiss();
                }
//                showBookContent(bookBean, chapter);
            //
            }
            showBookContent(bookBean, chapter);

            LogUtils.e("小说类型：" + novel_type);
            if (novel_type.equals("huiyuan")||novel_type.equals("changdu")){
                mTvBookReadSource.setVisibility(View.GONE);
            }else {
                mTvBookReadSource.setVisibility(View.VISIBLE);
            }
        }
    }




    /**
     *
     */
    private void select(){

    }


    /**
     * 这个函数在Activity创建完成之后会调用。购物车悬浮窗需要依附在Activity上，如果Activity还没有完全建好就去
     * 调用showCartFloatView()，则会抛出异常
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (ReaderApplication.net) return;
        if (mChapterList == null || mChapterList.size() == 0) return;
        final int pos[] = SettingManager.getInstance().getReadProgress(bookId);

        if (mChapterList.get(currentChapter).getIs_vip() == 1 && mChapterList.get(currentChapter).getIs_pay() == 0) {//缓存了 但是没有购买，无法阅读
            LogUtils.e("缓存了 但是没有购买，无法阅读");
            final BigDecimal b = new BigDecimal(mChapterList.get(currentChapter).getWord_num() * 0.001 * ReaderApplication.wordsPrice).setScale(0, BigDecimal.ROUND_HALF_UP);

            showBuyPop(pos[0], b.intValue(), 1);
        } else {

            if (buyWindow != null && buyWindow.isShowing()) {
                buyWindow.dismiss();
            }
        }
    }

    /**
     * 显示小说内容
     *
     * @param bookBean
     * @param chapter
     */
    private void showBookContent(BookBean bookBean, int chapter) {
        int pos[] = SettingManager.getInstance().getReadProgress(bookId);
        if (pos[1] == 0 && pos[2] == 0) {
            SettingManager.getInstance().saveReadProgress(bookId, mChapterList.get(0).getId(), 0, 0);
        }

        if (bookBean != null) {
            CacheManager.getInstance().saveChapterFile(bookId, chapter, bookBean);
            BookSourceBean bookSourceBean = new BookSourceBean();
            bookSourceBean.setBookId(bookId);
            bookSourceBean.setNovelId(chapter + "");
            bookSourceBean.setSource(bookBean.getSource_url());
            bookSourceBeanDao.insert(bookSourceBean);
            source_url = bookBean.getSource_url();
        } else {
            List<BookSourceBean> list = bookSourceBeanDao.queryBuilder().where(BookSourceBeanDao.Properties.NovelId.eq(chapter + "")).build().list();
            if (list != null && list.size() > 0) {
                source_url = list.get(0).getSource();
            }
        }

        if (TextUtils.isEmpty(source_url)) {
            tv_source.setVisibility(View.GONE);
        } else {
            tv_source.setVisibility(View.VISIBLE);
            tv_source.setText(source_url + "原网页阅读");
        }

        if (!startRead) {
            startRead = true;
            if (!mPageWidget.isPrepare()) {
                mPageWidget.init(curTheme);
            } else {
                mPageWidget.jumpToChapter(chapter);
            }
            hideDialog();
        }
    }

    @Override
    public void netError(int chapter) {
        hideDialog();//防止因为网络问题而出现dialog不消失
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showError() {
        hideDialog();
    }

    @Override
    public void complete() {
        hideDialog();
    }

    private synchronized void hideReadBar() {
        gone(mTvDownloadProgress, mLlBookReadBottom, mLlBookReadTop, rlReadAaSet, rlReadMark);
        hideStatusBar();
        decodeView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
    }

    private synchronized void showReadBar() { // 显示工具栏
        visible(mLlBookReadBottom, mLlBookReadTop);
        showStatusBar();
        decodeView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private synchronized void toggleReadBar() { // 切换工具栏 隐藏/显示 状态
        if (isVisible(mLlBookReadTop)) {
            hideReadBar();
        } else {
            showReadBar();
        }
    }

    /***************Title Bar*****************/

    @OnClick(R.id.tv_page)
    public void pageWeight() {//仿真
        tvOver.setSelected(false);
        tvNo.setSelected(false);
        tvPage.setSelected(true);
        SharedPreferencesUtil.getInstance().putInt(Constant.FLIP_STYLE, 0);
        finish();
        ReadActivity.startActivity(this, title, bookId, isShelf, pic, author, des);
    }

    @OnClick(R.id.tvMore)
    public void tvMore() {//更多
        showMorePopWindow();
    }

    @OnClick(R.id.tv_source)
    public void tvSource() {//换源
        LogUtils.e("source_ur：" + source_url);
        if (TextUtils.isEmpty(source_url)) {
            return;
        }
        CoomonApi.toBrowser(this, source_url);
    }

    @OnClick(R.id.tv_no)
    public void noWeight() {//简洁
        tvOver.setSelected(false);
        tvNo.setSelected(true);
        tvPage.setSelected(false);
        SharedPreferencesUtil.getInstance().putInt(Constant.FLIP_STYLE, 2);
        finish();
        ReadActivity.startActivity(this, title, bookId, isShelf, pic, author, des);
    }

    @OnClick(R.id.tv_over)
    public void overWeight() {//滑动
        tvOver.setSelected(true);
        tvNo.setSelected(false);
        tvPage.setSelected(false);
        SharedPreferencesUtil.getInstance().putInt(Constant.FLIP_STYLE, 1);
        finish();
        ReadActivity.startActivity(this, title, bookId, isShelf, pic, author, des);
    }

    @OnClick(R.id.ivBack)
    public void onClickBack() {
        if (mTocListPopupWindow.isShowing()) {
            mTocListPopupWindow.dismiss();
        } else if (!isShelf) {
            showJoinBookShelfDialog();
        } else {
            finish();
        }
    }

    @OnClick(R.id.tvBookReadReading)
    public void readBook() {
        oldPlayNum = 0;
        readProgressEnd = 0;
        hideStatusBar();
        gone(mTvDownloadProgress, mLlBookReadBottom, mLlBookReadTop, rlReadAaSet, rlReadMark);
        ReaderApplication.speed = sp.getInt("speed", 50);
        int position = sp.getInt("voicer", 0);
        ReaderApplication.vocher = list.get(position).voicer;

        String content = mPageWidget.getCurrentText();


        LogUtils.e("播放内容：" + content);

        if (ttsUtis == null) {
            ttsUtis = TtsUtis.getInstance();
        }
        if (ttsUtis.isPlay() && !mPageWidget.getCurrentText().equals(ttsUtis.getCurrentText())) {//正在播放，播放不同的小说
            ttsUtis.pause();
            ttsUtis.start(mPageWidget.getCurrentText());
        } else if (ttsUtis.isPlay() && content.equals(ttsUtis.getCurrentText())) {//正在播放，播放相同的小说
            showTts();
            setBackgroundAlpha(ReadActivity.this, 0.8f);
        } else if (!ttsUtis.isPlay()) {//没有播放
            ttsUtis.play(content, mTtsListener);
        }

    }

//    @OnClick(R.id.tvBookReadCommunity)
//    public void onClickCommunity() {
//        gone(rlReadAaSet, rlReadMark);
//        CoomonApi.share(this, "", this);
//    }

    @OnClick(R.id.tvBookReadIntroduce)
    public void onClickIntroduce() {
        gone(rlReadAaSet, rlReadMark);
        BookDetailActivity.startActivity(mContext, bookId);
    }

    @OnClick(R.id.tvBookReadSource)
    public void onClickSource() {//换源
        if (!ReaderApplication.net) {
            showToastMsg("网络连接异常");
            return;
        }
        BookSourceActivity.start(this, bookId, mChapterList.get(currentChapter).getChapter(), 1);
    }

    /***************Bottom Bar*****************/

    @OnClick(R.id.tvBookReadMode)
    public void onClickChangeMode() { // 日/夜间模式切换
        gone(rlReadAaSet, rlReadMark);

        boolean isNight = !SharedPreferencesUtil.getInstance().getBoolean(Constant.ISNIGHT, false);
        changedMode(isNight, -1);
    }

    /**
     * 切换日间夜间
     *
     * @param isNight
     * @param position
     */
    private void changedMode(boolean isNight, int position) {
        SharedPreferencesUtil.getInstance().putBoolean(Constant.ISNIGHT, isNight);
//        AppCompatDelegate.setDefaultNightMode(isNight ? AppCompatDelegate.MODE_NIGHT_YES
//                : AppCompatDelegate.MODE_NIGHT_NO);

        if (position >= 0) {
            curTheme = position;
        } else {
            curTheme = SettingManager.getInstance().getReadTheme();
        }

        adapter.select(curTheme);

        mPageWidget.setTheme(isNight ? ThemeManager.NIGHT : curTheme);


        mPageWidget.setTextColor(ContextCompat.getColor(mContext, isNight ? R.color.chapter_content_night : R.color.chapter_content_day),
                ContextCompat.getColor(mContext, isNight ? R.color.chapter_title_night : R.color.chapter_title_day));

        mTvBookReadMode.setText(getString(isNight ? R.string.book_read_mode_day_manual_setting
                : R.string.book_read_mode_night_manual_setting));

        Drawable drawable = ContextCompat.getDrawable(this, isNight ? R.drawable.ic_menu_mode_day_manual
                : R.drawable.ic_menu_mode_night_manual);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mTvBookReadMode.setCompoundDrawables(null, drawable, null, null);

        ThemeManager.setReaderTheme(curTheme, mRlBookReadRoot);
    }

    @OnClick(R.id.tvBookReadSettings)
    public void setting() {
        gone(mLlBookReadBottom);
        visible(rlReadAaSet);
        boolean auto_buy = sp.getBoolean(bookId, false);
        cbAutoBuy.setChecked(auto_buy);
//        if (isVisible(mLlBookReadBottom)) {
//            if (isVisible(rlReadAaSet)) {
//                gone(rlReadAaSet);
//            } else {
//                visible(rlReadAaSet);
//                gone(rlReadMark);
//            }
//        }
    }

    @OnClick(R.id.tv_pre_chapter)
    public void preChapter() {
        if (currentChapter == 0) {
            showToastMsg("已经是第一章了哦");
            return;
        } else {
            currentChapter = currentChapter - 1;
            startRead = false;
            readCurrentChapter();
        }
    }

    @OnClick(R.id.tv_font_type)
    public void setFontType() {
        Intent intent = new Intent(this, FontListActivity.class);
        startActivityForResult(intent, 123);
//        mPageWidget.setTextType("");
    }

    @OnClick(R.id.tv_next_chapter)
    public void nextChapter() {

        if (currentChapter == mChapterList.size() - 1) {
            showToastMsg("已经是最后一章了哦");
            return;
        } else {
            currentChapter = currentChapter + 1;
            startRead = false;
            LogUtils.e("当前章节：" + currentChapter);
            readCurrentChapter();
        }

    }

    @OnClick(R.id.tvBookReadDownload)
    public void downloadBook() {

        if (!ReaderApplication.net) {
            ToastUtils.showSingleToast("请检查网络链接");
            return;
        }
        gone(rlReadAaSet);
        Log.e("gy", "mChapterList：" + mChapterList.size());

        if (novel_type == null) {
            return;
        }

        if (novel_type.equals("other")) {//其他类型的小说免费下载
            showDownloadDialog();
        } else if (novel_type.equals("changdu") && user_member) {//畅读书城小说，会员免费下载
            showDownloadDialog();
        } else {//收费缓存
            showDownPop();
        }

    }

    /**
     * 显示下载对话框
     */
    private void showDownloadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("缓存多少章？")
                .setItems(new String[]{"后面五十章", "后面全部", "全部"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                DownloadBookService.post(new DownloadQueue(bookId, mChapterList, currentChapter + 1, currentChapter + 50));
                                break;
                            case 1:
                                DownloadBookService.post(new DownloadQueue(bookId, mChapterList, currentChapter + 1, mChapterList.size()));
                                break;
                            case 2:
                                DownloadBookService.post(new DownloadQueue(bookId, mChapterList, 1, mChapterList.size()));
                                break;
                            default:
                                break;
                        }
                    }
                });
        builder.show();
    }

    @OnClick(R.id.tvBookMark)
    public void onClickMark() {
        if (isVisible(mLlBookReadBottom)) {
            if (isVisible(rlReadMark)) {
                gone(rlReadMark);
            } else {
                gone(rlReadAaSet);

                updateMark();

                visible(rlReadMark);
            }
        }
    }

    @OnClick(R.id.tvBookReadToc)
    public void onClickToc() {
        mTocListPopupWindow.setAnchorView(ll_top);
        showTopicListPop();
    }

    /**
     * 显示目录的ListPopWindow
     */
    private void showTopicListPop() {
        gone(rlReadAaSet, rlReadMark);
        if (!mTocListPopupWindow.isShowing()) {
            visible(mTvBookReadTocTitle);
            gone(mTvBookReadReading, mTvBookReadSource, tvMore);
            mTocListPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            mTocListPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            mTocListPopupWindow.show();
            mTocListPopupWindow.setSelection(currentChapter);
            mTocListPopupWindow.getListView().setFastScrollEnabled(true);
        }
    }


    /***************Setting Menu*****************/

    @OnClick(R.id.ivBrightnessMinus)
    public void brightnessMinus() {
//        int curBrightness = SettingManager.getInstance().getReadBrightness();
//        if (curBrightness > 5 && !SettingManager.getInstance().isAutoBrightness()) {
//            seekbarLightness.setProgress((curBrightness = curBrightness - 2));
//            ScreenUtils.saveScreenBrightnessInt255(curBrightness, ReadActivity.this);
//        }
    }

    @OnClick(R.id.ivBrightnessPlus)
    public void brightnessPlus() {
//        int curBrightness = SettingManager.getInstance().getReadBrightness();
//        if (!SettingManager.getInstance().isAutoBrightness()) {
//            seekbarLightness.setProgress((curBrightness = curBrightness + 2));
//            ScreenUtils.saveScreenBrightnessInt255(curBrightness, ReadActivity.this);
//        }
    }

    @OnClick(R.id.tvFontsizeMinus)
    public void fontsizeMinus() {
        fontSize--;

        calcFontSize(seekbarFontSize.getProgress() - 1);
    }

    @OnClick(R.id.tvFontsizePlus)
    public void fontsizePlus() {
        fontSize++;
        calcFontSize(seekbarFontSize.getProgress() + 1);
    }

    @OnClick(R.id.tvClear)
    public void clearBookMark() {
        SettingManager.getInstance().clearBookMarks(bookId);

        updateMark();
    }

    @OnClick(R.id.tv_auto_light)
    public void autoLight() {
        isAutoLight(!SettingManager.getInstance().isAutoBrightness());
    }

    private void isAutoLight(boolean isAutoLightness) {

        if (isAutoLightness) {
            startAutoLightness();
            tvAutoLight.setBackground(getResources().getDrawable(R.drawable.coner_orange_light));
        } else {
            stopAutoLightness();
            ScreenUtils.saveScreenBrightnessInt255(ScreenUtils.getScreenBrightnessInt255(), AppUtils.getAppContext());
            tvAutoLight.setBackground(getResources().getDrawable(R.drawable.shape_gray));
        }
    }

    /***************Book Mark*****************/

    @OnClick(R.id.tvAddMark)
    public void addBookMark() {
        int[] readPos = mPageWidget.getReadPos();
        BookMark mark = new BookMark();
        mark.chapter = readPos[0];
        mark.startPos = readPos[1];
        mark.endPos = readPos[2];
        if (mark.chapter >= 1 && mark.chapter <= mChapterList.size()) {
            mark.title = mChapterList.get(mark.chapter - 1).getChapter();
        }
        mark.desc = mPageWidget.getHeadLine();
        if (SettingManager.getInstance().addBookMark(bookId, mark)) {
            ToastUtils.showSingleToast("添加书签成功");
            updateMark();
        } else {
            ToastUtils.showSingleToast("书签已存在");
        }
    }

    private void updateMark() {
        if (mMarkAdapter == null) {
            mMarkAdapter = new BookMarkAdapter(this, new ArrayList<BookMark>());
            lvMark.setAdapter(mMarkAdapter);
            lvMark.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    BookMark mark = mMarkAdapter.getData(position);
                    if (mark != null) {
                        mPageWidget.setPosition(new int[]{mark.chapter, mark.startPos, mark.endPos});
                        hideReadBar();
                    } else {
                        ToastUtils.showSingleToast("书签无效");
                    }
                }
            });
        }
        mMarkAdapter.clear();

        mMarkList = SettingManager.getInstance().getBookMarks(bookId);
        if (mMarkList != null && mMarkList.size() > 0) {
            Collections.reverse(mMarkList);
            mMarkAdapter.addAll(mMarkList);
        }
    }

    /***************Event*****************/

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showDownProgress(DownloadProgress progress) {
        if (bookId.equals(progress.bookId)) {
            if (isVisible(mLlBookReadBottom)) { // 如果工具栏显示，则进度条也显示
                visible(mTvDownloadProgress);
                // 如果之前缓存过，就给提示
                mTvDownloadProgress.setText(progress.message);
            } else {
                gone(mTvDownloadProgress);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void downloadMessage(final DownloadMessage msg) {
        if (isVisible(mLlBookReadBottom)) { // 如果工具栏显示，则进度条也显示
            if (bookId.equals(msg.bookId)) {
                visible(mTvDownloadProgress);
                mTvDownloadProgress.setText(msg.message);
                if (msg.isComplete) {
                    mTvDownloadProgress.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            gone(mTvDownloadProgress);
                        }
                    }, 2500);
                }
            }
        }
    }

    /**
     * 显示加入书架对话框
     */
    private void showJoinBookShelfDialog() {
        new AlertDialog.Builder(mContext)
                .setTitle(getString(R.string.book_read_add_book))
                .setMessage(getString(R.string.book_read_would_you_like_to_add_this_to_the_book_shelf))
                .setPositiveButton(getString(R.string.book_read_join_the_book_shelf), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        addNovel();
                    }
                })
                .setNegativeButton(getString(R.string.book_read_not), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .create()
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 1:
                source = data.getStringExtra("source");
                LogUtils.e("源id：" + source);
                mPresenter.getChapterRead(mChapterList.get(currentChapter).getId() + "", source);
                hideStatusBar();
                gone(mTvDownloadProgress, mLlBookReadBottom, mLlBookReadTop, rlReadAaSet, rlReadMark);
                break;

            case 100:
                getUser();
                break;
            case 111:
                getUser();
                break;
            case 110:
                if (buyWindow != null && buyWindow.isShowing()) {
                    showToastMsg("会员开通成功");
                    buyWindow.dismiss();
                }
                break;

            case 123:
                LogUtils.e("字体选择");
                if (mPageWidget != null) {
                    mPageWidget.setTextType(getFontType());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                if (buyWindow != null && buyWindow.isShowing()) {
                    finish();
                    LogUtils.e("😄哈");
                    return true;
                }

                if (ttsUtis != null && ttsUtis.isPlay()) {
                    ttsUtis.stop();
                    mPageWidget.stopRead();
                    ToastUtils.showToast("已退出语音朗读");
                    return true;
                }

                if (mTocListPopupWindow != null && mTocListPopupWindow.isShowing()) {
                    mTocListPopupWindow.dismiss();
                    gone(mTvBookReadTocTitle);
//                    visible(mTvBookReadReading, mTvBookReadCommunity, mTvBookReadChangeSource);
                    return true;
                } else if (isVisible(rlReadAaSet)) {
                    gone(rlReadAaSet);
                    hideReadBar();
                    return true;
                } else if (isVisible(mLlBookReadBottom)) {
                    hideReadBar();
                    return true;
                } else if (!isShelf) {
                    showJoinBookShelfDialog();
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_MENU:
                toggleReadBar();
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (SettingManager.getInstance().isVolumeFlipEnable()) {
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (SettingManager.getInstance().isVolumeFlipEnable()) {
                    return true;
                }
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (SettingManager.getInstance().isVolumeFlipEnable()) {
                if (ttsUtis.isPlay()) {
                    return true;
                }
                mPageWidget.nextPage();
                return true;// 防止翻页有声音
            }
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (SettingManager.getInstance().isVolumeFlipEnable()) {
                if (ttsUtis.isPlay()) {
                    return true;
                }
                mPageWidget.prePage();
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        addHistory();
        EventManager.refreshCollectionIcon();
        EventManager.refreshCollectionList();
        EventBus.getDefault().unregister(this);
        this.getContentResolver().unregisterContentObserver(Brightness);

        try {
            unregisterReceiver(receiver);
            ttsUtis.destroy();
            handler.removeMessages(1);
            handler = null;

        } catch (Exception e) {
            LogUtils.e("Receiver not registered");
        }

        if (isAutoLightness) {
            ScreenUtils.startAutoBrightness(ReadActivity.this);
        } else {
            ScreenUtils.stopAutoBrightness(ReadActivity.this);
        }

        if (mPresenter != null) {
            mPresenter.detachView();
        }
        mPresenter = null;
        Brightness = null;

        if (buyWindow != null && buyWindow.isShowing()) {
            buyWindow.dismiss();
            CacheManager.getInstance().delChapterFile(bookId, cChapter);
        }


        if (readDisposable != null && !readDisposable.isDisposed()) {
            readDisposable.dispose();
        }

    }

    private class ReadListener implements OnReadStateChangeListener {
        @Override
        public void onChapterChanged(int chapter) {

            LogUtils.i("onChapterChanged:" + chapter);
            getUser();
            for (int i1 = 0; i1 < mChapterList.size(); i1++) {
                if (mChapterList.get(i1).getId() == chapter) {
                    currentChapter = i1;
                }
            }
            initChapterSeekBar(mChapterList.size(), currentChapter);
            mTocListAdapter.setCurrentChapter(currentChapter);
        }

        @Override
        public void onPageChanged(int chapter, int page) {
            int[] readPos = mPageWidget.getReadPos();
//            if (mPageWidget instanceof ProgressiveWidget) {
//                ((ProgressiveWidget) mPageWidget).setTitle();
//                ((ProgressiveWidget) mPageWidget).setPercent();
//            }
        }

        @Override
        public void onLoadChapterFailure(int chapter) {
            LogUtils.i("onLoadChapterFailure:" + chapter);
            getUser();
            try {
                int i1 = 0;
                for (int i = 0; i < mChapterList.size(); i++) {
                    if (mChapterList.get(i).getId() == chapter) {
                        i1 = i;
                    }
                }
                startRead = false;
                if (CacheManager.getInstance().getChapterFile(bookId, chapter) == null) {

                    mPresenter.getChapterRead(mChapterList.get(i1).getId() + "", source);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onCenterClick() {
            LogUtils.i("onCenterClick");
            toggleReadBar();
        }

        @Override
        public void onFlip() {
            hideReadBar();
        }
    }

    private class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (seekBar.getId() == seekbarFontSize.getId() && fromUser) {
                calcFontSize(progress);
            } else if (seekBar.getId() == seekbarLightness.getId() && fromUser
                    && !SettingManager.getInstance().isAutoBrightness()) { // 非自动调节模式下 才可调整屏幕亮度
                ScreenUtils.saveScreenBrightnessInt100(progress, ReadActivity.this);
                //SettingManager.getInstance().saveReadBrightness(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    private class ChechBoxChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.getId() == cbVolume.getId()) {
                SettingManager.getInstance().saveVolumeFlipEnable(isChecked);
            } else if (buttonView.getId() == cbAutoBrightness.getId()) {
                if (isChecked) {
                    startAutoLightness();
                } else {
                    stopAutoLightness();
                    ScreenUtils.saveScreenBrightnessInt255(ScreenUtils.getScreenBrightnessInt255(), AppUtils.getAppContext());
                }
            } else if (buttonView.getId() == cbAutoBuy.getId()) {//自动购买
                cbAutoBuy.setChecked(isChecked);

                sp.edit().putBoolean(bookId, isChecked).commit();
                LogUtils.e("自动购买：" + bookId + "    " + sp.getBoolean(bookId, false));
            }
        }
    }

    class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (mPageWidget != null) {
                if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                    int level = intent.getIntExtra("level", 0);
                    mPageWidget.setBattery(100 - level);
                } else if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
                    mPageWidget.setTime(sdf.format(new Date()));
                }
            }
        }
    }

    private void startAutoLightness() {
        SettingManager.getInstance().saveAutoBrightness(true);
        ScreenUtils.startAutoBrightness(ReadActivity.this);
        seekbarLightness.setEnabled(false);
    }

    private void stopAutoLightness() {
        SettingManager.getInstance().saveAutoBrightness(false);
        ScreenUtils.stopAutoBrightness(ReadActivity.this);
        seekbarLightness.setProgress((int) (ScreenUtils.getScreenBrightnessInt255() / 255.0F * 100));
        seekbarLightness.setEnabled(true);
    }

    private void calcFontSize(int progress) {
        // progress range 1 - 10
//        if (progress >= 0 && progress <= 10) {
//
//            seekbarFontSize.setProgress(progress);
//            LogUtils.e("progress:"+(ScreenUtils.pxToSp(ScreenUtils.dpToPxInt(12 + 1.7f * progress))) );
//            tvFontSize.setText(((int) ScreenUtils.pxToSp(ScreenUtils.dpToPxInt(12 + 1.7f * progress))) + "");
//            mPageWidget.setFontSize(ScreenUtils.dpToPxInt(12 + 1.7f * progress));
//        }
        if (fontSize >= 12 && fontSize <= 29) {
            tvFontSize.setText(fontSize + "");
            mPageWidget.setFontSize(fontSize);
        }

    }

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
//            ToastUtils.showToast("开始播放");
        }

        @Override
        public void onSpeakPaused() {
//            ToastUtils.showToast("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
//            ToastUtils.showToast("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            // 合成进度
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
                        Log.e("ReadActivity", percent + "  " + (beginPos-readProgressEnd) + "   " + (endPos-readProgressEnd));
            mPageWidget.readProgress(percent, beginPos-readProgressEnd, endPos-readProgressEnd);

            // 播放进度
//            Log.e("ReadActivity", percent + "  " + beginPos + "   " + endPos);
//            if (oldPercent == percent) {
//                return;
//            } else {
//                oldPercent = percent;
//            }
            double p = mPageWidget.getPercent();
            Log.e("ReadActivity", "进度2：" + endPos);


//            if ((endPos / ttsUtis.getCurrentText().length()) >= (mPageWidget.getReadPos()[2] / p)) {
//                mPageWidget.nextPage();
//            }
            if (endPos >= oldPlayNum + p) {
                oldPlayNum += p;
                readProgressEnd = endPos;
                mPageWidget.nextPage();
            }
        }

        @Override
        public void onCompleted(SpeechError error) {
            oldPlayNum = 0;
            readProgressEnd = 0;
            if (error == null) {
                mPageWidget.nextPage();
                if (!ttsUtis.isPlay()) {
                    ttsUtis.start(mPageWidget.getCurrentText(), mTtsListener);
                    return;
                }
            } else if (error != null) {
                ToastUtils.showToast(error.getPlainDescription(true));
                mPageWidget.stopRead();
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

        }
    };

    /**
     * 添加书架
     */
    private void addNovel() {
        RetrofitClient.getInstance().createApi().addNovel(ReaderApplication.token, bookId)
                .compose(RxUtils.<HttpResult<CommonBean>>io_main())
                .subscribe(new BaseObjObserver<CommonBean>(this, "添加中") {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        ToastUtils.showToast("添加成功");
                        finish();
                    }
                });
    }

    /**
     * 弹出朗读的对话框
     */
    private void showTts() {
        ttsUtis.pause();
        View view = View.inflate(ReadActivity.this, R.layout.layout_tts, null);

        if (window == null) {
            window = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            speedSeekBar = (SeekBar) view.findViewById(R.id.seekbarSpeed);
            ttsRecycler = (RecyclerView) view.findViewById(R.id.recyclerview);
            tvCloseTts = (TextView) view.findViewById(R.id.tv_open);
            tvTime = (TextView) view.findViewById(R.id.tv_set_time);
            ttsAdapter = new TtsAdapter(R.layout.item_tts, list);
            ttsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            DividerDecoration itemDecoration = new DividerDecoration(ContextCompat.getColor(getActivity(), R.color.reader_menu_bg_color), 30, 0, 0);
            itemDecoration.setDrawLastItem(false);
            ttsRecycler.addItemDecoration(itemDecoration);
            ttsRecycler.setAdapter(ttsAdapter);
            window.setTouchable(true);
            window.setOutsideTouchable(true);
            window.setBackgroundDrawable(new ColorDrawable());
            window.setFocusable(true);
        }

        if (time == 0) {
            tvTime.setText("定时");
        }

        speedSeekBar.setProgress(ReaderApplication.speed);
        int position = sp.getInt("voicer", 0);
        ttsAdapter.setSelect(position);

        window.showAtLocation(mRlBookReadRoot, Gravity.BOTTOM, 0, 0);

        ttsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter2, View view, int position) {
                ReaderApplication.vocher = list.get(position).voicer;
                ttsAdapter.setSelect(position);
                sp.edit().putInt("voicer", position).commit();
                ttsIsChange = true;
            }
        });

        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ReaderApplication.speed = progress;
                sp.edit().putInt("speed", progress).commit();
                ttsIsChange = true;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tvCloseTts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttsUtis.stop();
                mPageWidget.stopRead();
                window.dismiss();
            }
        });

        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeWindow();
            }
        });

        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (this != null) {
                    setBackgroundAlpha(ReadActivity.this, 1f);
                }

                if (ttsIsChange) {
                    ttsUtis.setParam();
                    ttsIsChange = false;
                } else {
                    ttsUtis.reStart();
                    ttsIsChange = false;
                }
                hideStatusBar();
                gone(mTvDownloadProgress, mLlBookReadBottom, mLlBookReadTop, rlReadAaSet, rlReadMark);
            }
        });

    }

    /**
     * 设置页面的透明度
     *
     * @param bgAlpha 1表示不透明
     */
    public static void setBackgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        activity.getWindow().setAttributes(lp);
    }

    /**
     * 初始化语音播报的功能
     */
    private void addTtsList() {
        String[] types = getResources().getStringArray(R.array.voicer_cloud_entries);
        String[] values = getResources().getStringArray(R.array.voicer_cloud_values);

        for (int i = 0; i < types.length; i++) {
            TtsThemeBean bean = new TtsThemeBean();
            bean.type = types[i];
            bean.voicer = values[i];
            list.add(bean);
        }
    }

    /**
     * 定时时间
     */
    private void timeWindow() {
        View view = View.inflate(ReadActivity.this, R.layout.pop_time, null);
        timeWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        view.findViewById(R.id.tv_time_5).setOnClickListener(this);
        view.findViewById(R.id.tv_time_15).setOnClickListener(this);
        view.findViewById(R.id.tv_time_30).setOnClickListener(this);
        view.findViewById(R.id.tv_time_60).setOnClickListener(this);
        view.findViewById(R.id.tv_time_90).setOnClickListener(this);
        view.findViewById(R.id.tv_close).setOnClickListener(this);

        timeWindow.setTouchable(true);
        timeWindow.setOutsideTouchable(true);
        timeWindow.setBackgroundDrawable(new ColorDrawable());
        timeWindow.setFocusable(true);
        timeWindow.showAtLocation(mRlBookReadRoot, Gravity.BOTTOM, 0, 0);

        view.findViewById(R.id.tv_time_5).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        time = -1;
        switch (v.getId()) {
            case R.id.tv_time_5:
                time = 5;
                break;
            case R.id.tv_time_15:
                time = 15;
                break;
            case R.id.tv_time_30:
                time = 30;
                break;
            case R.id.tv_time_60:
                time = 60;
                break;
            case R.id.tv_time_90:
                time = 90;
                break;
            case R.id.tv_close:
                time = 0;
                timeWindow.dismiss();
                disposable.dispose();
                break;
            case R.id.ll_live_shar_qq:
                CoomonApi.share(this, QQ.NAME, this);
                break;
            case R.id.ll_live_shar_qqzone:
                CoomonApi.share(this, QZone.NAME, this);
                break;
            case R.id.ll_live_shar_wechat:
                CoomonApi.share(this, Wechat.NAME, this);
                break;
            case R.id.ll_live_shar_pyq://朋友圈
                CoomonApi.share(this, WechatMoments.NAME, this);
                break;
            case R.id.ll_live_share_qq:
                CoomonApi.share(this, QQ.NAME, this);
                break;
            case R.id.ll_live_share_qqzone:
                CoomonApi.share(this, QZone.NAME, this);
                break;
            case R.id.ll_live_share_wechat:
                CoomonApi.share(this, Wechat.NAME, this);
                break;
            case R.id.ll_live_share_sinna:
                CoomonApi.share(this, SinaWeibo.NAME, this);
                break;
            case R.id.ll_live_share_pyq://朋友圈
                CoomonApi.share(this, WechatMoments.NAME, this);
                break;
            case R.id.tv_share_copy://复制
                CoomonApi.copy(this, ReaderApplication.shareUrl);
                sharePopWindow2.dismiss();
                break;
            case R.id.tvBookShare://分享
                morePopWindow.dismiss();
                hideReadBar();
                showSharePopWindow2();
//                CoomonApi.share(this, "", this);
                break;

            case R.id.tvBookDetail://小说详情
                morePopWindow.dismiss();
                gone(rlReadAaSet, rlReadMark);
                BookDetailActivity.startActivity(mContext, bookId);
                break;
            default:
                break;
        }
        if (time == -1) {
            return;
        }

        if (time == 0) {
            tvTime.setText("定时");
        } else {
            tvTime.setText(time + "分钟");
            startTime();
        }
        if (timeWindow.isShowing()) {
            timeWindow.dismiss();
        }
    }

    /**
     * 朗读开始计时
     */
    private void startTime() {
        if (disposable != null) {
            disposable.dispose();
        }

        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Long value) {
                        Log.e("gy", "value：" + value);
                        if (value > 0 && value % 60 == 0) {//一分钟
                            time--;
                            if (time == 0) {
                                tvTime.setText("定时");
                                disposable.dispose();
                                ttsUtis.stop();
                                mPageWidget.stopRead();
                            }
                            tvTime.setText(time + "分钟");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 阅读计时
     */
    private void readTime() {
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        readDisposable = d;
                    }

                    @Override
                    public void onNext(Long value) {
                        Log.e("gy", "value：" + value);
                        if (value == 60 * 60) {//阅读了一分钟了
                            readDisposable.dispose();
                            handler.sendEmptyMessage(1);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        readDisposable.dispose();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                showShareDialog();
            } else if (msg.what == 2) {
                notifyDialog(msg.arg1);
            }

        }
    };

    /**
     * 阅读休息计时
     */
    private void notifytTime() {
        int time = 0;
        if (notify == 0) {
            return;
        } else if (notify == 1) {
            LogUtils.e("阅读15分钟休息");
            time = 15;
        } else if (notify == 2) {
            LogUtils.e("阅读30分钟休息");
            time = 30;
        } else if (notify == 3) {
            LogUtils.e("阅读60分钟休息");
            time = 60;
        }

        final int finalTime = time;
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        notifyDisposable = d;
                    }

                    @Override
                    public void onNext(Long value) {
                        Log.e("gy", "value：" + value);
                        if (value == finalTime * 60) {//阅读了一分钟了,该休息了
                            notifyDisposable.dispose();
                            Message message = new Message();
                            message.what = 2;
                            message.arg1 = finalTime;
                            handler.sendMessage(message);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        notifyDisposable.dispose();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 阅读休息提示
     */
    private void notifyDialog(int arg1) {
        shareDialog = new AlertDialog.Builder(this)
                .setTitle("温馨提示")
                .setMessage("亲 您已阅读" + arg1 + "分钟了哦!是否休息一下再看？")
                .setCancelable(false)
                .setPositiveButton("继续看", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        notifytTime();
                    }
                })
                .setNegativeButton("休息一下", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })

                .create();
        shareDialog.show();
    }


    /**
     * 显示分享提示对话框
     */
    private void showShareDialog() {
        shareDialog = new AlertDialog.Builder(this)
                .setTitle("温馨提示")
                .setMessage("亲 您已阅读一个小时了哦!好东西要分享，分享后可以继续阅读哦!")
                .setCancelable(false)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        showSharePopWindow();

                    }
                })
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                            dialog.dismiss();
                            finish();
                        }
                        return false;
                    }
                })
                .create();
        shareDialog.show();
    }

    /**
     * 分享pop弹窗
     */
    public void showSharePopWindow() {
        View view = LayoutInflater.from(ReadActivity.this).inflate(R.layout.pop_view_share, null);
        view.findViewById(R.id.ll_live_shar_qqzone).setOnClickListener(this);
        view.findViewById(R.id.ll_live_shar_qq).setOnClickListener(this);
        view.findViewById(R.id.ll_live_shar_pyq).setOnClickListener(this);
        view.findViewById(R.id.ll_live_shar_wechat).setOnClickListener(this);
        sharePopWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        sharePopWindow.setBackgroundDrawable(new BitmapDrawable());
        sharePopWindow.setFocusable(false);
        sharePopWindow.setOutsideTouchable(false);
        sharePopWindow.showAtLocation(mRlBookReadRoot, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (sharePopWindow != null && sharePopWindow.isShowing()) {
            return false;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 分享回掉
     *
     * @param platform
     * @param i
     * @param hashMap
     */
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        ToastUtils.showToast("分享成功");
        sp.edit().putString(currentDate, currentDate).commit();
        if (readDisposable != null) {
            readDisposable.dispose();
        }
        if (sharePopWindow != null && sharePopWindow.isShowing()) {
            sharePopWindow.dismiss();

        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        ToastUtils.showToast("分享出错");
        finish();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        ToastUtils.showToast("取消分享");
    }

    @Subscribe
    public void isPlay(String play) {//语音正在播放 停止播放
        if ("play".equals(play)) {
            gone(mTvDownloadProgress, mLlBookReadBottom, mLlBookReadTop, rlReadAaSet, rlReadMark);
            if (window != null && window.isShowing()) {
                window.dismiss();
                hideStatusBar();
            } else {
                showTts();
                showStatusBar();
            }
        }
    }

    /**
     * 更新阅读时间
     */
    private void updateReadTime() {
        RetrofitClient.getInstance().createApi().updateReadTime(ReaderApplication.token, bookId)
                .compose(RxUtils.<HttpResult<CommonBean>>io_main())
                .subscribe(new BaseObjObserver<CommonBean>(this, false) {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {

                    }
                });
    }

    /**
     * 显示更多的pop
     */
    private void showMorePopWindow() {
        View view = LayoutInflater.from(ReadActivity.this).inflate(R.layout.pop_read_more, null);
        view.findViewById(R.id.tvBookShare).setOnClickListener(this);
        view.findViewById(R.id.tvBookDetail).setOnClickListener(this);
        morePopWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        morePopWindow.setBackgroundDrawable(new BitmapDrawable());
        morePopWindow.setFocusable(true);
        int windowPos[] = CoomonApi.calculatePopWindowPos(tvMore, view);
        int xOff = 50;// 可以自己调整偏移
        windowPos[0] -= xOff;
        windowPos[1] += 20;
        morePopWindow.showAtLocation(view, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);
    }

    /**
     * 显示购买的pop
     */
    private void showBuyPop(final int chapter, final int money, final int num) {
        hideReadBar();
        try {
            ttsUtis.stop();
            mPageWidget.stopRead();
        } catch (Exception e) {

        }

        final boolean auto_buy = sp.getBoolean(bookId, false);
        cChapter = chapter;
        cMoney = money;
        if (buyPopView == null) {
            buyPopView = LayoutInflater.from(ReadActivity.this).inflate(R.layout.pop_pay_chapter, null);
            buyPopTvPay = (TextView) buyPopView.findViewById(R.id.tv_pay);
            buyPopTvYue = (TextView) buyPopView.findViewById(R.id.tv_yue);
            buyPopTvPre = (TextView) buyPopView.findViewById(R.id.tv_pre);
            buyPopTvNext = (TextView) buyPopView.findViewById(R.id.tv_next);
            buyPopBtBuy = (Button) buyPopView.findViewById(R.id.bt_buy);
            buyPopBtVip = (Button) buyPopView.findViewById(R.id.bt_vip);
            buyPopCb = (CheckBox) buyPopView.findViewById(R.id.cb_auto_buy);
            buyTvClose = (TextView) buyPopView.findViewById(R.id.close);
            buyTvTopic = (TextView) buyPopView.findViewById(R.id.tv_topic);
        }

        buyPopCb.setChecked(auto_buy);

        if (buyWindow == null) {
            buyWindow = new PopupWindow(buyPopView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            buyWindow.setBackgroundDrawable(new BitmapDrawable());
            buyWindow.setTouchable(true);
            buyWindow.setFocusable(false);//设置点击menu以外其他地方以及返回键退出
            buyWindow.setOutsideTouchable(false);//设置触摸外面时消失
        }

        buyPopTvYue.setText("余额：" + coin + "金豆");
        buyPopTvPay.setText("价格：" + money + "金豆");

        if ("changdu".equals(novel_type)) {
            buyPopBtVip.setVisibility(View.VISIBLE);
        } else {
            buyPopBtVip.setVisibility(View.GONE);
        }

        if (coin - money < 0) {//余额不足，前往充值
            isPay = false;
            buyPopBtBuy.setText("余额不足");
        } else {
            isPay = true;
            buyPopBtBuy.setText("订阅本章节");
        }


        buyWindow.showAtLocation(mRlBookReadRoot, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        buyPopCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e("选中的状态：" + buyPopCb.isChecked());
                sp.edit().putBoolean(bookId, buyPopCb.isChecked()).commit();
            }
        });

        buyPopTvPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//前一章
                LogUtils.e("chapter：" + chapter);
                int preChapter = 0;
                for (int i = 0; i < mChapterList.size(); i++) {
                    if (mChapterList.get(i).getId() == chapter) {
                        preChapter = i;
                    }
                }
                if (preChapter == 0) {
                    currentChapter = 0;
                } else {
                    currentChapter = preChapter - 1;
                }
                startRead = false;
                LogUtils.e("当前章节：" + mChapterList.get(currentChapter).getId());
                readCurrentChapter();
                CacheManager.getInstance().delChapterFile(bookId, chapter);
            }
        });

        buyPopTvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//下一章
                int preChapter = 0;
                for (int i = 0; i < mChapterList.size(); i++) {
                    if (mChapterList.get(i).getId() == chapter) {
                        preChapter = i;
                    }
                }
                if (preChapter == mChapterList.size() - 1) {
                    showToastMsg("已经是最后一章了哦");
                } else {
                    currentChapter = preChapter + 1;
                    startRead = false;
                    LogUtils.e("当前章节：" + currentChapter);
                    readCurrentChapter();
                    CacheManager.getInstance().delChapterFile(bookId, chapter);
                }

            }
        });

        buyPopBtBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPay) {
                    ReadActivity.this.startActivityForResult(new Intent(ReadActivity.this, MyAssetsActivity.class)
                            .putExtra("start", "read")
                            .putExtra("coin", coin + ""), 100);
                } else {
                    payChapter("buy", chapter, money, num);
                }
            }
        });

        buyPopBtVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadActivity.this.startActivityForResult(new Intent(ReadActivity.this, MyVipActivity.class)
                        .putExtra("start", "read")
                        .putExtra("coin", coin + ""), 110);

            }
        });

        buyTvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadActivity.this.finish();
            }
        });

        buyTvTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTocListPopupWindow.setAnchorView(tv);
                showTopicListPop();
            }
        });

    }

    /**
     * 显示下载的pop
     */
    private void showDownPop() {

        if (downPopView == null) {
            downPopView = LayoutInflater.from(ReadActivity.this).inflate(R.layout.pop_download, null);
            downViewHolder = new ViewHolder(downPopView);
        }

        if (downWindow == null) {
            downWindow = new PopupWindow(downPopView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            downWindow.setBackgroundDrawable(new BitmapDrawable());
            downWindow.setFocusable(true);
        }

        downViewHolder.tv20.setSelected(false);
        downViewHolder.tv50.setSelected(false);
        downViewHolder.tv100.setSelected(false);
        downViewHolder.tvAll.setSelected(false);

        int i = mChapterList.size() - currentChapter - 1;//剩余章节

        LogUtils.e("当前章节：" + currentChapter);
        LogUtils.e("当前章节2：" + i);

        if (i >= 100) {
            downViewHolder.llAssets.setVisibility(View.VISIBLE);
            downViewHolder.tv20.setVisibility(View.VISIBLE);
            downViewHolder.tv50.setVisibility(View.VISIBLE);
            downViewHolder.tv100.setVisibility(View.VISIBLE);
            downViewHolder.img20.setVisibility(View.GONE);
            downViewHolder.img50.setVisibility(View.GONE);
            downViewHolder.img100.setVisibility(View.GONE);
            downViewHolder.tvAll.setVisibility(View.VISIBLE);
        } else if (i >= 50 && i < 100) {
            downViewHolder.llAssets.setVisibility(View.VISIBLE);
            downViewHolder.tv20.setVisibility(View.VISIBLE);
            downViewHolder.tv50.setVisibility(View.VISIBLE);
            downViewHolder.tv100.setVisibility(View.GONE);
            downViewHolder.img20.setVisibility(View.GONE);
            downViewHolder.img50.setVisibility(View.GONE);
            downViewHolder.img100.setVisibility(View.GONE);
            downViewHolder.tvAll.setVisibility(View.VISIBLE);
        } else if (i < 50 && i >= 20) {
            downViewHolder.llAssets.setVisibility(View.VISIBLE);
            downViewHolder.tv20.setVisibility(View.VISIBLE);
            downViewHolder.tv50.setVisibility(View.GONE);
            downViewHolder.tv100.setVisibility(View.GONE);
            downViewHolder.img20.setVisibility(View.GONE);
            downViewHolder.img50.setVisibility(View.GONE);
            downViewHolder.img100.setVisibility(View.GONE);
            downViewHolder.tvAll.setVisibility(View.VISIBLE);
        } else if (i < 20) {
            downViewHolder.llAssets.setVisibility(View.GONE);
            downViewHolder.tvAll.setVisibility(View.VISIBLE);
        }

        downViewHolder.tvDownYue.setText("·余额：" + coin + "金豆");
        downViewHolder.tvDownChapterFree.setText("·免费/已购章节：0章");
        downViewHolder.tvDownChapterPay.setText("·付费章节：0章");
        downViewHolder.tvDownOldMoney.setText("0金豆");
        downViewHolder.tvDownOldMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        downViewHolder.tvDownRealMoney.setText("0金豆");
        downViewHolder.tvDownYue.setText("·余额：" + coin + "金豆");

        downWindow.showAtLocation(mRlBookReadRoot, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        downViewHolder.tvAll.setText("剩余" + i + "章");

        downViewHolder.tvDownClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downWindow.dismiss();
            }
        });

        downViewHolder.tv20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downViewHolder.tv20.setSelected(true);
                downViewHolder.tv50.setSelected(false);
                downViewHolder.tv100.setSelected(false);
                downViewHolder.tvAll.setSelected(false);
//                calDownMoney(20, 1);
                calPrice(20);
            }
        });

        downViewHolder.tv50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downViewHolder.tv20.setSelected(false);
                downViewHolder.tv50.setSelected(true);
                downViewHolder.tv100.setSelected(false);
                downViewHolder.tvAll.setSelected(false);
//                calDownMoney(50, 1);
                calPrice(50);
            }
        });

        downViewHolder.tv100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downViewHolder.tv20.setSelected(false);
                downViewHolder.tv50.setSelected(false);
                downViewHolder.tv100.setSelected(true);
                downViewHolder.tvAll.setSelected(false);
//                calDownMoney(100, 1);
                calPrice(100);
            }
        });

        downViewHolder.tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downViewHolder.tv20.setSelected(false);
                downViewHolder.tv50.setSelected(false);
                downViewHolder.tv100.setSelected(false);
                downViewHolder.tvAll.setSelected(true);
//                calDownMoney(mChapterList.size(), 1);
                calPrice(mChapterList.size());
            }
        });

    }

    private void calPrice(final int num) {

        String id = currentChapter + "";

        try {
            id = mChapterList.get(currentChapter + 1).getId() + "";
        } catch (Exception e) {
            id = currentChapter + "";
        }
        RetrofitClient.getInstance().createApi().calculatePrice(ReaderApplication.token, id, num)
                .compose(RxUtils.<HttpResult<CalPriceBean>>io_main())
                .subscribe(new BaseObjObserver<CalPriceBean>(this, "计算中") {
                    @Override
                    protected void onHandleSuccess(final CalPriceBean calPriceBean) {

                        int oldPrice = 0;
                        for (int i = 0; i < calPriceBean.getPay_ids().size(); i++) {
                            oldPrice += calPriceBean.getPay_ids().get(i).getCoin();
                        }
                        downViewHolder.tvDownChapterFree.setText("·免费/已购章节：" + calPriceBean.getChapter_free_num() + "章");
                        downViewHolder.tvDownChapterPay.setText("·付费章节：" + calPriceBean.getChapter_vip_num() + "章");
                        downViewHolder.tvDownOldMoney.setText(oldPrice + "金豆");
                        downViewHolder.tvDownOldMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                        downViewHolder.tvDownRealMoney.setText(calPriceBean.getVip_price() + "金豆");
                        downViewHolder.tvDownYue.setText("·余额：" + coin + "金豆");
                        downViewHolder.tvDownDiscount.setText("·折扣：" + calPriceBean.getDiscount() + "折");

                        if (coin < calPriceBean.getVip_price()) {
                            downViewHolder.btDownBuy.setText("余额不足，请充值后再下载");
                        } else {
                            downViewHolder.btDownBuy.setText("确认下载");
                        }

                        downViewHolder.btDownBuy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (!downViewHolder.tv20.isSelected() && !downViewHolder.tv50.isSelected() && !downViewHolder.tv100.isSelected() && !downViewHolder.tvAll.isSelected()) {
                                    ToastUtils.showSingleToast("请选择缓存章节");
                                    return;
                                }

                                if (!downViewHolder.btDownBuy.getText().equals("确认下载")) {
                                    ReadActivity.this.startActivityForResult(new Intent(ReadActivity.this, MyAssetsActivity.class)
                                            .putExtra("start", "read")
                                            .putExtra("coin", coin + ""), 111);
                                } else {
                                    downWindow.dismiss();
                                    payChapter("down", mChapterList.get(currentChapter).getId(), calPriceBean.getVip_price(), num);
                                }
                            }
                        });
                    }
                });
    }

    /**
     * 购买章节
     *
     * @param num
     */
    private void payChapter(final String type, int chapter, final int money, final int num) {

        LogUtils.e("chapter：" + chapter);
        if (type.equals("down")) {
            chapter = mChapterList.get(currentChapter + 1).getId();
        }

        final int finalChapter = chapter;
        RetrofitClient.getInstance().createApi().payChapter(ReaderApplication.token, chapter + "", num)
                .compose(RxUtils.<HttpResult>io_main())
                .subscribe(new Consumer<HttpResult>() {
                    @Override
                    public void accept(HttpResult httpResult) throws Exception {
                        if (httpResult.code == 200) {
                            getUser();
                            if (type.equals("buy")) {
                                ToastUtils.showSingleToast("购买成功");
                                ReadBook readBook = new ReadBook();
                                readBook.setId(Long.valueOf(finalChapter));
                                readBook.setBookId(bookId);
                                readBook.setChapterId(finalChapter + "");
                                readBook.setIsbuy(true);
                                readBook.setType(novel_type);
                                readBookDao.insertOrReplace(readBook);
                                if (buyWindow != null && buyWindow.isShowing()) {
                                    buyWindow.dismiss();
                                }
                            } else if (type.equals("down")) {
                                ToastUtils.showSingleToast("开始下载");
                                DownloadBookService.post(new DownloadQueue(bookId, mChapterList, currentChapter + 1, currentChapter + 1 + num));
                            }


                            mPresenter.getBookMixAToc(bookId, "chapters");
                        } else {
                            sp.edit().putBoolean(bookId, false).commit();
                            showBuyPop(finalChapter, money, num);
                            showToastMsg(httpResult.message);
                        }
                    }
                });
    }

    /**
     * 获取余额
     */
    private void getUser() {
        RetrofitClient.getInstance().createApi().getUser(ReaderApplication.token)
                .compose(RxUtils.<HttpResult<UserBean>>io_main())
                .subscribe(new BaseObjObserver<UserBean>(getActivity(), false) {
                    @Override
                    protected void onHandleSuccess(UserBean userBean) {
                        coin = userBean.getCoin();
                        long expire_time = userBean.getExpire_time();//到期时间
                        Date currentTime = new Date();
                        long diff = expire_time * 1000 - currentTime.getTime();

                        long days = diff / (1000 * 60 * 60 * 24);
                        LogUtils.e("剩余时间：" + days);

                        if (days <= 0) {
                            user_member = false;
                        } else {
                            user_member = true;
                        }

                        try {
                            if (buyPopTvYue != null && buyPopTvPay != null) {
                                buyPopTvYue.setText("余额：" + coin + "金豆");
                                if (coin > cMoney) {
                                    isPay = true;
                                    buyPopBtBuy.setText("订阅本章节");
                                } else {
                                    isPay = false;
                                    buyPopBtBuy.setText("余额不足");
                                }
                            }

                            if (downViewHolder != null) {
                                downViewHolder.tvDownYue.setText("·余额：" + coin + "金豆");
                                if (coin < real.intValue()) {
                                    downViewHolder.btDownBuy.setText("余额不足，请充值后再下载");
                                } else {
                                    downViewHolder.btDownBuy.setText("确认下载");
                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                });
    }

    /**
     * 下载的ViewHolder
     */
    static class ViewHolder {
        @Bind(R.id.tv_down_close)
        TextView tvDownClose;
        @Bind(R.id.tv_20)
        TextView tv20;
        @Bind(R.id.tv_50)
        TextView tv50;
        @Bind(R.id.tv_100)
        TextView tv100;
        @Bind(R.id.img_20)
        ImageView img20;
        @Bind(R.id.img_50)
        ImageView img50;
        @Bind(R.id.img_100)
        ImageView img100;
        @Bind(R.id.ll_assets)
        LinearLayout llAssets;
        @Bind(R.id.tv_all)
        TextView tvAll;
        @Bind(R.id.tv_down_chapter_free)
        TextView tvDownChapterFree;
        @Bind(R.id.tv_down_chapter_pay)
        TextView tvDownChapterPay;
        @Bind(R.id.tv_down_old_money)
        TextView tvDownOldMoney;
        @Bind(R.id.tv_down_real_money)
        TextView tvDownRealMoney;
        @Bind(R.id.tv_down_yue)
        TextView tvDownYue;
        @Bind(R.id.tv_down_refresh)
        TextView tvDownReFresh;
        @Bind(R.id.tv_down_chapter_discount)
        TextView tvDownDiscount;
        @Bind(R.id.bt_down_buy)
        Button btDownBuy;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 右上角的分享
     */
    public void showSharePopWindow2() {
        View view = LayoutInflater.from(ReadActivity.this).inflate(R.layout.pop_share, null);
        view.findViewById(R.id.ll_live_share_qqzone).setOnClickListener(this);
        view.findViewById(R.id.ll_live_share_qq).setOnClickListener(this);
        view.findViewById(R.id.ll_live_share_pyq).setOnClickListener(this);
        view.findViewById(R.id.ll_live_share_wechat).setOnClickListener(this);
        view.findViewById(R.id.ll_live_share_sinna).setOnClickListener(this);
        view.findViewById(R.id.tv_share_copy).setOnClickListener(this);
        sharePopWindow2 = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        sharePopWindow2.setBackgroundDrawable(new BitmapDrawable());
        sharePopWindow2.setFocusable(false);
        sharePopWindow2.setOutsideTouchable(true);
        sharePopWindow2.showAtLocation(mRlBookReadRoot, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


    public String getFontType() { //搜索目录，扩展名，是否进入子文件夹
        LogUtils.e("小说字体类型：" + SharedPreferencesUtil.getInstance().getString("font"));

        return SharedPreferencesUtil.getInstance().getString("font");
    }
}
