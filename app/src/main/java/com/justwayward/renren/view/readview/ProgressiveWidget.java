/**
 * Copyright 2016 JustWayward Team
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.justwayward.renren.view.readview;

/**
 * @author zths.
 * @date 2017/08/03.
 */
public class ProgressiveWidget /*extends RelativeLayout implements ReadInterface*/ {

//    /**
//     * 字体设置
//     */
//    private int textColor, titleColor, textSize;
//    private Typeface mTypeface;
//    private int mLineSpace;
//
//    /**
//     * 区域判断
//     */
//    private int mScreenHeight, mScreenWidth;
//    private boolean center = false;
//    private int dx, dy;
//
//    private boolean prepare = false;
//
//
//    protected OnReadStateChangeListener listener;
//    protected PageFactory pagefactory = null;
//    private ReadBookAdapter mAdapter;
//
//    private RecyclerView mRecyclerView;
//    private OtherSettingView mOtherSettingView;
//    private String bookId;
//    private List<ChapterListBean> chapterList;
//    private boolean loadMore = false;
//
//    public ProgressiveWidget(Context context, String bookId, List<ChapterListBean> chapterList, OnReadStateChangeListener readListener) {
//        super(context);
//        listener = readListener;
//        this.chapterList = chapterList;
//        mScreenWidth = ScreenUtils.getScreenWidth();
//        mScreenHeight = ScreenUtils.getScreenHeight();
//        textSize = SettingManager.getInstance().getReadFontSize();
//        textColor = ContextCompat.getColor(context, R.color.chapter_content_day);
//        titleColor = ContextCompat.getColor(AppUtils.getAppContext(), R.color.chapter_title_day);
//        setTextType(SharedPreferencesUtil.getInstance().getString("font"));
//        mLineSpace = textSize / 5 * 2;
//
//        pagefactory = new PageFactory(getContext(), bookId, chapterList);
//        pagefactory.setOnReadStateChangeListener(listener);
//        this.bookId = bookId;
//        initView();
//    }
//
//    private void initView() {
//        View view = View.inflate(getContext(), R.layout.item_noanimweidget, this);
//        mOtherSettingView = (OtherSettingView) view.findViewById(R.id.settingView_noanim);
//        mOtherSettingView.setLineSpace(mLineSpace);
//
////        view.findViewById(R.id.text_pre).setOnClickListener(new OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                pagefactory.prePage();
////                Log.e("NoAimWidget", pagefactory.getPrePage());
////            }
////        });
////        view.findViewById(R.id.text_next).setOnClickListener(new OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                pagefactory.nextPage();
////                Log.e("NoAimWidget", pagefactory.getNextPage());
////            }
////        });
//        initRecyclerView(view);
//    }
//
//    private void initRecyclerView(View view) {
//        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_noanim);
//        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mRecyclerView.getLayoutParams();
//        layoutParams.topMargin = mOtherSettingView.getLineHeight();
//        layoutParams.bottomMargin = mOtherSettingView.getLineHeight();
//        mRecyclerView.setLayoutParams(layoutParams);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        mAdapter = new ReadBookAdapter(R.layout.item_readtext, chapterList);
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                int firstVisibleItem = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
//                mOtherSettingView.setnewTitle(mAdapter.getItem(firstVisibleItem).getChapter());
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });
//    }
//
//    public void setTitle() {
//        mOtherSettingView.setnewTitle(pagefactory.getTitle());
//    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                dx = (int) ev.getX();
//                dy = (int) ev.getY();
//                center = dx >= mScreenWidth / 3 && dx <= mScreenWidth * 2 / 3 && dy >= mScreenHeight / 3 && dy <= mScreenHeight * 2 / 3;
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                int ux = (int) ev.getX();
//                int uy = (int) ev.getY();
//                if (center) { // ACTION_DOWN的位置在中间，则不响应滑动事件
//                    if (Math.abs(ux - dx) < 5 && Math.abs(uy - dy) < 5) {
//                        listener.onCenterClick();
//                        return true;
//                    }
//                    break;
//                }
//                break;
//            default:
//                break;
//        }
//        return super.onInterceptTouchEvent(ev);
//    }
//
//    @Override
//    public boolean isPrepare() {
//        return prepare;
//    }
//
//    @Override
//    public void jumpToChapter(int chapter) {
//        mRecyclerView.scrollToPosition(mAdapter.getItemIdByChapter(chapter));
//    }
//
//    @Override
//    public String getCurrentText() {
//        return pagefactory.getCurrentText();
//    }
//
//    @Override
//    public void setTheme(int theme) {
//
//    }
//
//    @Override
//    public int[] getReadPos() {
//        return pagefactory.getPosition();
//    }
//
//    @Override
//    public String getHeadLine() {
//        return pagefactory.getHeadLineStr().replaceAll("@", "");
//    }
//
//    @Override
//    public void setPosition(int[] pos) {
//    }
//
//    @Override
//    public void nextPage() {
//        mRecyclerView.scrollBy(0, -mScreenHeight);
//    }
//
//    @Override
//    public void prePage() {
//        mRecyclerView.scrollBy(0, mScreenHeight);
//    }
//
//    @Override
//    public void setBattery(int battery) {
//        mOtherSettingView.setBattery(battery);
//    }
//
//    @Override
//    public void setTime(String time) {
//        mOtherSettingView.setTime(time);
//    }
//
//    @Override
//    public void init(int theme) {
//        if (!prepare) {
//            try {
//                pagefactory.setBgBitmap(ThemeManager.getThemeDrawable(theme));
//                int pos[] = SettingManager.getInstance().getReadProgress(bookId);
//                int ret = pagefactory.openBook(pos[0], new int[]{0, 0});
//                if (ret == 0) {
//                    listener.onLoadChapterFailure(pos[0]);
//                    return;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            prepare = true;
//        }
//    }
//
//    @Override
//    public void setFontSize(int fontSizePx) {
//        this.textSize = fontSizePx;
//        mLineSpace = textSize / 5 * 2;
//        updateStatus();
//    }
//
//    @Override
//    public void setTextColor(int textColor, int titleColor) {
//        this.textColor = textColor;
//        this.titleColor = titleColor;
//        updateStatus();
//    }
//
//    @Override
//    public void setTextType(String path) {
//        if (TextUtils.isEmpty(path)) {
//            mTypeface = Typeface.MONOSPACE;
//        } else {
//            mTypeface = Typeface.createFromFile(path);
//        }
//        updateStatus();
//    }
//
//    private void updateStatus() {
//
//    }
//
//    public void setPercent() {
//        mOtherSettingView.setPercent(pagefactory.getPercent());
//    }
//
//    private class ReadBookAdapter extends BaseQuickAdapter<ChapterListBean, BaseViewHolder> {
//        public ReadBookAdapter(int layoutResId, @Nullable List<ChapterListBean> data) {
//            super(layoutResId, data);
//        }
//
//        @Override
//        protected void convert(BaseViewHolder helper, ChapterListBean item) {
//            NoScrollView view = helper.getView(R.id.read_scroll);
//            view.setFactory(pagefactory);
//            view.setChapter(item);
//            if (textColor != 0) {
//                view.setTextColor(textColor);
//            }
//            if (textSize != 0) {
//                view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
//            }
//            if (mTypeface != null) {
//                view.setTypeface(mTypeface);
//            }
//            if (mLineSpace != 0) {
//                view.setLineSpacing(mLineSpace, 1.1f);
//            }
//        }
//
//        public int getItemIdByChapter(int chapter) {
//            for (int i = 0; i < getItemCount(); i++) {
//                if (getItem(i).getId() == chapter) {
//                    return i;
//                }
//            }
//            return 0;
//        }
//
//    }

}
