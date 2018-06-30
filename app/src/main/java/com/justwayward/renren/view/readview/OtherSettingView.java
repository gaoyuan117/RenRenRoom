package com.justwayward.renren.view.readview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.justwayward.renren.R;
import com.justwayward.renren.manager.SettingManager;
import com.justwayward.renren.utils.AppUtils;
import com.justwayward.renren.utils.ScreenUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Project_Name :RenRenRoom
 * @package_Name :com.justwayward.book.view.readview
 * @AUTHOR :xzwzz@vip.qq.com
 * @DATE :2018/1/29  19:49
 */
public class OtherSettingView extends View {

    private DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
    private int timeLen = 0, percentLen = 0;

    private String time;
    private int battery = 40;
    private Rect rectF;
    private ProgressBar batteryView;
    private Bitmap batteryBitmap;

    private int marginHeight, marginWidth;
    private int mHeight, mWidth;
    private Paint mTitlePaint;
    private int mLineSpace;
    private String title;
    private float percent;

    public OtherSettingView(Context context) {
        this(context, null);
    }

    public OtherSettingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        marginWidth = ScreenUtils.dpToPxInt(15);
        marginHeight = ScreenUtils.dpToPxInt(15);
        mWidth = ScreenUtils.getScreenWidth();
        mHeight = ScreenUtils.getScreenHeight();

        mTitlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTitlePaint.setTextSize(ScreenUtils.dpToPxInt(16));
        mTitlePaint.setColor(ContextCompat.getColor(AppUtils.getAppContext(), R.color.chapter_title_day));
        timeLen = (int) mTitlePaint.measureText("00:00");
        percentLen = (int) mTitlePaint.measureText("00.00%");
        title = "";
        percent = 100f;
    }

    public int getLineHeight() {
        return marginHeight + (mLineSpace << 1);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int y = marginHeight + (mLineSpace / 2);
        canvas.drawText(title, marginWidth, y, mTitlePaint);

        // 绘制提示内容
        if (batteryBitmap != null) {
            canvas.drawBitmap(batteryBitmap, marginWidth + 2, mHeight - marginHeight - ScreenUtils.dpToPxInt(12), mTitlePaint);
        }
        canvas.drawText(decimalFormat.format(percent) + "%", (mWidth - percentLen) / 2, mHeight - marginHeight, mTitlePaint);
        String mTime = dateFormat.format(new Date());
        canvas.drawText(mTime, mWidth - marginWidth - timeLen, mHeight - marginHeight, mTitlePaint);
    }

    public void setnewTitle(String title) {
        this.title = title;
        postInvalidate();
    }

    public void setLineSpace(int mLineSpace) {
        this.mLineSpace = mLineSpace;
        postInvalidate();
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public void convertBetteryBitmap() {
        batteryView = (ProgressBar) LayoutInflater.from(getContext()).inflate(R.layout.layout_battery_progress, null);
        batteryView.setProgressDrawable(ContextCompat.getDrawable(getContext(),
                SettingManager.getInstance().getReadTheme() < 4 ?
                        R.drawable.seekbar_battery_bg : R.drawable.seekbar_battery_night_bg));
        batteryView.setProgress(battery);
        batteryView.setDrawingCacheEnabled(true);
        batteryView.measure(MeasureSpec.makeMeasureSpec(ScreenUtils.dpToPxInt(26), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(ScreenUtils.dpToPxInt(14), MeasureSpec.EXACTLY));
        batteryView.layout(0, 0, batteryView.getMeasuredWidth(), batteryView.getMeasuredHeight());
        batteryView.buildDrawingCache();
        //batteryBitmap = batteryView.getDrawingCache();
        // tips: @link{https://github.com/JustWayward/BookReader/issues/109}
        batteryBitmap = Bitmap.createBitmap(batteryView.getDrawingCache());
        batteryView.setDrawingCacheEnabled(false);
        batteryView.destroyDrawingCache();
        postInvalidate();
    }

    public void setBattery(int battery) {
        this.battery = battery;
        convertBetteryBitmap();
    }

    public void setTime(String time) {
        this.time = time;
    }
}
