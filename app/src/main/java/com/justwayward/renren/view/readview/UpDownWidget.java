package com.justwayward.renren.view.readview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;
import android.view.MotionEvent;

import com.justwayward.renren.bean.ChapterListBean;
import com.justwayward.renren.manager.SettingManager;
import com.justwayward.renren.manager.ThemeManager;
import com.justwayward.renren.ui.activity.ReadActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * @Project_Name :RenRenRoom
 * @package_Name :com.justwayward.book.view.readview
 * @AUTHOR :xzwzz@vip.qq.com
 * @DATE :2018/1/31  13:43
 */
public class UpDownWidget extends BaseReadView {

    private Path mPath0;

    GradientDrawable mBackShadowDrawableLR;
    GradientDrawable mBackShadowDrawableRL;

    public UpDownWidget(Context context, String bookId,
                        List<ChapterListBean> chaptersList,
                        OnReadStateChangeListener listener) {
        super(context, bookId, chaptersList, listener);

        mTouch.x = 0.01f;
        mTouch.y = 0.01f;

        mPath0 = new Path();

        int[] mBackShadowColors = new int[]{0xaa666666, 0x666666};
        mBackShadowDrawableRL = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, mBackShadowColors);
        mBackShadowDrawableRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        mBackShadowDrawableLR = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mBackShadowColors);
        mBackShadowDrawableLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (ReadActivity.ttsUtis != null && ReadActivity.ttsUtis.isPlay()) {
            EventBus.getDefault().postSticky("play");
            return true;
        }
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                et = System.currentTimeMillis();
                dx = (int) e.getX();
                dy = (int) e.getY();
                down = dx;
                mTouch.x = dx;
                mTouch.y = dy;
                actiondownX = dx;
                actiondownY = dy;
                touch_down = 0;
                pagefactory.onDraw(mCurrentPageCanvas);
                if (actiondownX >= mScreenWidth / 3 && actiondownX <= mScreenWidth * 2 / 3
                        && actiondownY >= mScreenHeight / 3 && actiondownY <= mScreenHeight * 2 / 3) {
                    center = true;
                } else {
                    center = false;
                    NeedCalcu = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (center) {
                    break;
                }
                int mx = (int) e.getX();
                int my = (int) e.getY();
                if (Math.abs(my - actiondownY) < 10) {
                    cancel = true;
                    return true;
                } else {
                    cancel = false;
                }
                if (NeedCalcu) {
                    calcCornerXY(actiondownX, actiondownY);
                    if (my > actiondownY + 30) {
                        if (!isNextPage(true)) {
                            return true;
                        }
                    } else if (my < actiondownY - 30) {
                        if (!isNextPage(false)) {
                            return true;
                        }
                    } else {
                        return false;
                    }
                    NeedCalcu = false;
                    listener.onFlip();
                    setBitmaps(mCurPageBitmap, mNextPageBitmap);
                }
                cancel = (prePage && my < actiondownY) || (!prePage && my > actiondownY);

                mTouch.x = mx;
                mTouch.y = my;
                touch_down = mTouch.y - actiondownY;
                this.postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                long t = System.currentTimeMillis();
                int ux = (int) e.getX();
                int uy = (int) e.getY();
                ups = ux;
                if (center) { // ACTION_DOWN的位置在中间，则不响应滑动事件
                    resetTouchPoint();
                    if (Math.abs(ux - actiondownX) < 5 && Math.abs(uy - actiondownY) < 5) {
                        listener.onCenterClick();
                        return false;
                    }
                    break;
                }

                if ((Math.abs(ux - actiondownX) < 30) && (Math.abs(uy - actiondownY) < 30)) {
                    if ((t - et < 1000)) { // 单击
                        if (actiondownY < mScreenHeight / 3) {
                            if (!isNextPage(true)) {
                                return true;
                            }
                        } else if (actiondownY > mScreenHeight / 3 * 2) {
                            if (!isNextPage(false)) {
                                return true;
                            }
                        } else {
                            return false;
                        }
                        calcCornerXY(actiondownX, actiondownY);
                        NeedCalcu = false;
                        listener.onFlip();
                        setBitmaps(mCurPageBitmap, mNextPageBitmap);

                        mTouch.x = ux;
                        mTouch.y = uy;
                        touch_down = mTouch.y - actiondownY;

                        startAnimation();
                    } else { // 长按
                        pagefactory.cancelPage();
                        restoreAnimation();
                    }
                    postInvalidate();
                    return true;
                }
                if (cancel) {
                    pagefactory.cancelPage();
                    restoreAnimation();
                    postInvalidate();
                } else {
                    startAnimation();
                    postInvalidate();
                }
                cancel = false;
                center = false;
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void drawCurrentPageArea(Canvas canvas) {
        mPath0.reset();

        canvas.save();
        if (!prePage) {
            mPath0.moveTo(0, mScreenHeight + touch_down);
            mPath0.lineTo(mScreenWidth, mScreenHeight + touch_down);
            mPath0.lineTo(mScreenWidth, mScreenHeight);
            mPath0.lineTo(0, mScreenHeight);
            mPath0.lineTo(0, mScreenHeight + touch_down);
            mPath0.close();
            canvas.clipPath(mPath0, Region.Op.XOR);
            canvas.drawBitmap(mCurPageBitmap, 0, touch_down, null);
        } else {
            mPath0.moveTo(0, touch_down);
            mPath0.lineTo(mScreenWidth, touch_down);
            mPath0.lineTo(mScreenWidth, mScreenHeight);
            mPath0.lineTo(0, mScreenHeight);
            mPath0.lineTo(0, touch_down);
            mPath0.close();
            canvas.clipPath(mPath0);
            canvas.drawBitmap(mCurPageBitmap, 0, touch_down, null);
        }
        try {
            canvas.restore();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void drawCurrentPageShadow(Canvas canvas) {
        canvas.save();
        GradientDrawable shadow;
        if (!prePage) {
            shadow = mBackShadowDrawableLR;
//            shadow.setBounds((int) (mScreenWidth + touch_down - 5), 0, (int) (mScreenWidth + touch_down + 5), mScreenHeight);
            shadow.setBounds(0, (int) (mScreenHeight + touch_down - 5), mScreenWidth, (int) (mScreenHeight + touch_down + 5));

        } else {
            shadow = mBackShadowDrawableRL;
//            shadow.setBounds((int) (touch_down - 5), 0, (int) (touch_down + 5), mScreenHeight);
            shadow.setBounds(0, (int) (touch_down - 5), mScreenWidth, (int) (touch_down + 5));
        }
        shadow.draw(canvas);
        try {
            canvas.restore();
        } catch (Exception e) {

        }
    }

    @Override
    protected void drawCurrentBackArea(Canvas canvas) {
        // none
    }

    @Override
    protected void drawNextPageAreaAndShadow(Canvas canvas) {
        canvas.save();
        if (!prePage) {
            canvas.clipPath(mPath0);
            canvas.drawBitmap(mNextPageBitmap, 0, 0, null);
        } else {
            canvas.clipPath(mPath0, Region.Op.XOR);
            canvas.drawBitmap(mNextPageBitmap, 0, 0, null);
        }
        try {
            canvas.restore();
        } catch (Exception e) {

        }
    }

    @Override
    protected void calcPoints() {

    }

    @Override
    protected void calcCornerXY(float x, float y) {

    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            float x = mScroller.getCurrX();
            float y = mScroller.getCurrY();
            if (!prePage) {
                touch_down = -(mScreenHeight - y);
            } else {
                touch_down = y;
            }
            mTouch.x = x;
            postInvalidate();
        }
    }

    @Override
    protected void startAnimation() {
        int dy;
        if (!prePage) {
            dy = (int) -(mScreenHeight + touch_down);
//            mScroller.startScroll((int) (mScreenWidth + touch_down), (int) mTouch.y, dx, 0, 700);
            mScroller.startScroll((int) mTouch.x, (int) (mScreenHeight + touch_down), 0, dy, 700);
        } else {
            dy = (int) (mScreenHeight - touch_down);
            mScroller.startScroll((int) mTouch.x, (int) (touch_down), 0, dy, 700);
        }
    }

    @Override
    protected void abortAnimation() {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
    }

    @Override
    protected void restoreAnimation() {
        int dy;
        if (!prePage) {
            dy = (int) (mScreenHeight - mTouch.y);
        } else {
            dy = (int) (-mTouch.y);
        }
        mScroller.startScroll((int) mTouch.x, (int) mTouch.y, 0, dy, 300);
    }

    @Override
    public void setBitmaps(Bitmap bm1, Bitmap bm2) {
        mCurPageBitmap = bm1;
        mNextPageBitmap = bm2;
    }

    @Override
    public synchronized void setTheme(int theme) {
        resetTouchPoint();
        Bitmap bg = ThemeManager.getThemeDrawable(theme);
        if (bg != null) {
            pagefactory.setBgBitmap(bg);
            if (isPrepared) {
                pagefactory.onDraw(mCurrentPageCanvas);
                pagefactory.onDraw(mNextPageCanvas);
                postInvalidate();
            }
        }
        if (theme < 10) {
            SettingManager.getInstance().saveReadTheme(theme);
        }
    }

    @Override
    public double getPercent() {
        return pagefactory.getPlayPercent();
    }
}
