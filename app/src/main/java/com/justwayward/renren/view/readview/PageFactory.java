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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.justwayward.renren.R;
import com.justwayward.renren.bean.ChapterListBean;
import com.justwayward.renren.manager.SettingManager;
import com.justwayward.renren.ui.activity.ReadActivity;
import com.justwayward.renren.utils.AppUtils;
import com.justwayward.renren.utils.FileUtils;
import com.justwayward.renren.utils.LogUtils;
import com.justwayward.renren.utils.ScreenUtils;
import com.justwayward.renren.utils.SharedPreferencesUtil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class PageFactory {
    private Context mContext;
    /**
     * 屏幕宽高
     */
    private int mHeight, mWidth;
    /**
     * 文字区域宽高
     */
    private int mVisibleHeight, mVisibleWidth;
    /**
     * 间距
     */
    private int marginHeight, marginWidth;
    /**
     * 字体大小
     */
    private int mFontSize, mNumFontSize;
    /**
     * 每页行数
     */
    private int mPageLineCount;
    /**
     * 行间距
     **/
    private int mLineSpace;
    /**
     * 字节长度
     */
    private int mbBufferLen;
    /**
     * MappedByteBuffer：高效的文件内存映射
     */
    private MappedByteBuffer mbBuff;
    /**
     * 页首页尾的位置
     */
    private int curEndPos = 0, curBeginPos = 0, tempBeginPos, tempEndPos;
    private int currentChapter, tempChapter;
    private Vector<String> mLines = new Vector<>();
    private Vector<String> mNextLines = new Vector<>();

    private Paint mPaint;
    private Paint mTitlePaint;
    private Paint volumePaint;
    private Bitmap mBookPageBg;

    private DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
    private int timeLen = 0, percentLen = 0;
    private String time;
    private int battery = 40;
    private Rect rectF;
    private ProgressBar batteryView;
    private Bitmap batteryBitmap;

    private String bookId;
    private List<ChapterListBean> chaptersList;
    private int chapterSize = 0;
    private int currentPage = 1;

    private OnReadStateChangeListener listener;
    private String charset = "UTF-8";
    private String currentText = "";
    private boolean openVolume;
    private int percent = -1, beginPos, endPos;
    private String chapterSingleText;

    public PageFactory(Context context, String bookId, List<ChapterListBean> chaptersList) {
        this(context, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight(),
                //SettingManager.getInstance().getReadFontSize(bookId),
                (int) ScreenUtils.spToPx(SettingManager.getInstance().getReadFontSize()),
                bookId, chaptersList);
    }

    public PageFactory(Context context, int width, int height, int fontSize, String bookId, List<ChapterListBean> chaptersList) {
        mContext = context;
        mWidth = width;
        mHeight = height;
        mFontSize = fontSize;
        mLineSpace = mFontSize / 5 * 2;
        mNumFontSize = ScreenUtils.dpToPxInt(16);
        marginWidth = ScreenUtils.dpToPxInt(15);
        marginHeight = ScreenUtils.dpToPxInt(15);
        mVisibleHeight = mHeight - marginHeight * 2 - mNumFontSize * 2 - mLineSpace * 2;
        mVisibleWidth = mWidth - marginWidth * 2;
        mPageLineCount = mVisibleHeight / (mFontSize + mLineSpace);
        rectF = new Rect(0, 0, mWidth, mHeight);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mFontSize);
        mPaint.setTextSize(ContextCompat.getColor(context, R.color.chapter_content_day));
        mPaint.setColor(Color.BLACK);

        mTitlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTitlePaint.setTextSize(mNumFontSize);
        mTitlePaint.setColor(ContextCompat.getColor(AppUtils.getAppContext(), R.color.chapter_title_day));

        volumePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        volumePaint.setTextSize(mFontSize);
        volumePaint.setTextSize(ContextCompat.getColor(context, R.color.chapter_content_day));
        volumePaint.setColor(Color.BLUE);

        openVolume = false;

        timeLen = (int) mTitlePaint.measureText("00:00");
        percentLen = (int) mTitlePaint.measureText("00.00%");
        try {
            String path = SharedPreferencesUtil.getInstance().getString("font");
            Typeface typeface = null;
            if (TextUtils.isEmpty(path)) {
                typeface = Typeface.MONOSPACE;
            } else {
                typeface = Typeface.createFromFile(path);
            }

            mPaint.setTypeface(typeface);
            mTitlePaint.setTypeface(typeface);

        } catch (Exception e) {
            e.printStackTrace();
        }


        this.bookId = bookId;
        this.chaptersList = chaptersList;

        time = dateFormat.format(new Date());
    }

    public File getBookFile(int chapter) {
        File file = FileUtils.getChapterFile(bookId, chapter);
        if (file != null && file.length() > 1) {
            // 解决空文件造成编码错误的问题
            charset = FileUtils.getCharset(file.getAbsolutePath());
        }
        return file;
    }

    public void openBook() {
        openBook(new int[]{0, 0});
    }

    public void openBook(int[] position) {
        openBook(1, position);
    }

    /**
     * 打开书籍文件
     *
     * @param chapter  阅读章节
     * @param position 阅读位置
     * @return 0：文件不存在或打开失败  1：打开成功
     */
    public int openBook(int chapter, int[] position) {
        this.currentChapter = chapter;
        this.chapterSize = chaptersList.size();
        String path = getBookFile(currentChapter).getPath();
        try {
            File file = new File(path);
            long length = file.length();
            if (length > 1) {
                mbBufferLen = (int) length;
                // 创建文件通道，映射为MappedByteBuffer
                mbBuff = new RandomAccessFile(file, "r")
                        .getChannel()
                        .map(FileChannel.MapMode.READ_ONLY, 0, length);

                curBeginPos = position[0];
                curEndPos = position[1];
                onChapterChanged(chapter);
                mLines.clear();
                return 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取当前缓存的章节内容
     *
     * @return
     */
    public String getCurrentText() {
        if (TextUtils.isEmpty(currentText)) {
            currentText = " ";
        }

//        return currentText.replaceAll("@", "");
        return aVoid();
    }

    public String aVoid() {
        String s = "";
//        new int[]{currentChapter, curBeginPos, curEndPos};
        LogUtils.e("mbBufferLen："+mbBufferLen);
        LogUtils.e("mbBuffecurBeginPosrLen："+curBeginPos);
        byte[] buf = new byte[mbBufferLen-curBeginPos];
        for (int i = curBeginPos; i < mbBufferLen; i++) {
            buf[i-curBeginPos] = mbBuff.get(i);
        }

        try {
            s = new String(buf, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return s;
    }


    public String getTitle() {
        int i = -1;
        for (int i1 = 0; i1 < chaptersList.size(); i1++) {
            if (chaptersList.get(i1).getId() == currentChapter) {
                i = i1;
            }
        }
        if (i != -1) {
            return chaptersList.get(i).getChapter();
        }
        return "";
    }

    public float getPercent() {
        int current = 1;
        for (int i1 = 0; i1 < chaptersList.size(); i1++) {
            if (chaptersList.get(i1).getId() == currentChapter) {
                current = i1;
            }
        }

        float percent = (float) current * 100 / chapterSize;
        return percent;
    }

    public void openVolume(boolean open) {
        if (open != openVolume) {
            openVolume = open;
        }
    }

    //
    public synchronized void onDraw(Canvas canvas) {
        if (mLines.size() == 0) {
            curEndPos = curBeginPos;
            mLines = pageDown();
        }
        if (mLines.size() > 0) {
            int y = marginHeight + (mLineSpace << 1);
            // 绘制背景
            if (mBookPageBg != null && !mBookPageBg.isRecycled()) {
                canvas.drawBitmap(mBookPageBg, null, rectF, null);
            } else {
                canvas.drawColor(Color.WHITE);
            }
            onDrawTitle(canvas);

            y += mLineSpace + mNumFontSize;
            // 绘制阅读页面文字
            StringBuffer sb = new StringBuffer();
            Paint cachePaint;
            for (int j = 0; j < mLines.size(); j++) {
                chapterSingleText = mLines.get(j);
                sb.append(chapterSingleText);
                y += mLineSpace;
                if (Math.abs(currentText.indexOf(chapterSingleText) - beginPos) <= chapterSingleText.length() && openVolume) {
                    cachePaint = volumePaint;
                } else {
                    cachePaint = mPaint;
                }

                if (chapterSingleText.endsWith("@")) {
                    canvas.drawText(chapterSingleText.substring(0, chapterSingleText.length() - 1), marginWidth, y, cachePaint);
                    y += mLineSpace;
                } else {
                    canvas.drawText(chapterSingleText, marginWidth, y, cachePaint);
                }
                y += mFontSize;
            }
            currentText = sb.toString();

            sb.setLength(0);

            onDrawOther(canvas);

            // 保存阅读进度
            SettingManager.getInstance().saveReadProgress(bookId, currentChapter, curBeginPos, curEndPos);

            int[] readProgress = SettingManager.getInstance().getReadProgress(bookId);

            curBeginPos  = readProgress[1];
            curEndPos  = readProgress[2];

        }
    }

    private void onDrawTitle(Canvas canvas) {
        int y = marginHeight + (mLineSpace << 1);
        String title = "";
        // 绘制标题
        int i = -1;
        for (int i1 = 0; i1 < chaptersList.size(); i1++) {
            if (chaptersList.get(i1).getId() == currentChapter) {
                i = i1;
            }
        }
        if (i != -1) {
            title = chaptersList.get(i).getChapter();
        }
        canvas.drawText(title, marginWidth, y, mTitlePaint);
    }

    private void onDrawOther(Canvas canvas) {
        // 绘制提示内容
        if (batteryBitmap != null) {
            canvas.drawBitmap(batteryBitmap, marginWidth + 2,
                    mHeight - marginHeight - ScreenUtils.dpToPxInt(12), mTitlePaint);
        }

        int current = 1;
        for (int i1 = 0; i1 < chaptersList.size(); i1++) {
            if (chaptersList.get(i1).getId() == currentChapter) {
                current = i1;
            }
        }

        float percent = (float) current * 100 / chapterSize;


        canvas.drawText(decimalFormat.format(percent) + "%", (mWidth - percentLen) / 2,
                mHeight - marginHeight, mTitlePaint);

        String mTime = dateFormat.format(new Date());
        canvas.drawText(mTime, mWidth - marginWidth - timeLen, mHeight - marginHeight, mTitlePaint);
    }

    //draw the canvas with percent
    public synchronized void onDraw2(Canvas canvas) {
        String title = "";
        if (mLines.size() == 0) {
            curEndPos = curBeginPos;
            mLines = pageDown();
        }
        if (mLines.size() > 0) {
            int y = marginHeight + (mLineSpace << 1);
            // 绘制背景
            if (mBookPageBg != null && !mBookPageBg.isRecycled()) {
                canvas.drawBitmap(mBookPageBg, null, rectF, null);
            } else {
                canvas.drawColor(Color.WHITE);
            }
            // 绘制标题
            int i = -1;
            for (int i1 = 0; i1 < chaptersList.size(); i1++) {
                if (chaptersList.get(i1).getId() == currentChapter) {
                    i = i1;
                }
            }
            if (i != -1) {
                title = chaptersList.get(i).getChapter();
            }
            canvas.drawText(title, marginWidth, y, mTitlePaint);
            y += mLineSpace + mNumFontSize;
            // 绘制阅读页面文字
            StringBuffer sb = new StringBuffer();
            int progressInt = 0;
            for (int j = 0; j < mLines.size(); j++) {
                chapterSingleText = mLines.get(j);
                sb.append(chapterSingleText);
                y += mLineSpace;
                progressInt = (100 / mLines.size()) * j - percent;
                if (percent != -1 && Math.abs(progressInt) <= 5 && openVolume) {
                    if (chapterSingleText.endsWith("@")) {
                        canvas.drawText(chapterSingleText.substring(0, chapterSingleText.length() - 1), marginWidth, y, volumePaint);
                        y += mLineSpace;
                    } else {
                        canvas.drawText(chapterSingleText, marginWidth, y, volumePaint);
                    }
                } else {
                    if (chapterSingleText.endsWith("@")) {
                        canvas.drawText(chapterSingleText.substring(0, chapterSingleText.length() - 1), marginWidth, y, mPaint);
                        y += mLineSpace;
                    } else {
                        canvas.drawText(chapterSingleText, marginWidth, y, mPaint);
                    }
                }
                y += mFontSize;
            }
            currentText = sb.toString();

            sb.setLength(0);

            // 绘制提示内容
            if (batteryBitmap != null) {
                canvas.drawBitmap(batteryBitmap, marginWidth + 2,
                        mHeight - marginHeight - ScreenUtils.dpToPxInt(12), mTitlePaint);
            }

            int current = 1;
            for (int i1 = 0; i1 < chaptersList.size(); i1++) {
                if (chaptersList.get(i1).getId() == currentChapter) {
                    current = i1;
                }
            }

            float percent = (float) current * 100 / chapterSize;


            canvas.drawText(decimalFormat.format(percent) + "%", (mWidth - percentLen) / 2,
                    mHeight - marginHeight, mTitlePaint);

            String mTime = dateFormat.format(new Date());
            canvas.drawText(mTime, mWidth - marginWidth - timeLen, mHeight - marginHeight, mTitlePaint);

            // 保存阅读进度
            SettingManager.getInstance().saveReadProgress(bookId, currentChapter, curBeginPos, curEndPos);
        }
    }

    /**
     * 指针移到上一页页首
     */
    private void pageUp() {
        String strParagraph = "";
        Vector<String> lines = new Vector<>(); // 页面行
        int paraSpace = 0;
        mPageLineCount = mVisibleHeight / (mFontSize + mLineSpace);
        while ((lines.size() < mPageLineCount) && (curBeginPos > 0)) {
            Vector<String> paraLines = new Vector<>(); // 段落行
            byte[] parabuffer = readParagraphBack(curBeginPos); // 1.读取上一个段落

            curBeginPos -= parabuffer.length; // 2.变换起始位置指针
            try {
                strParagraph = new String(parabuffer, charset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            strParagraph = strParagraph.replaceAll("\r\n", "  ");
            strParagraph = strParagraph.replaceAll("\n", " ");

            while (strParagraph.length() > 0) { // 3.逐行添加到lines
                int paintSize = mPaint.breakText(strParagraph, true, mVisibleWidth, null);
                paraLines.add(strParagraph.substring(0, paintSize));
                strParagraph = strParagraph.substring(paintSize);
            }
            lines.addAll(0, paraLines);

            while (lines.size() > mPageLineCount) { // 4.如果段落添加完，但是超出一页，则超出部分需删减
                try {
                    curBeginPos += lines.get(0).getBytes(charset).length; // 5.删减行数同时起始位置指针也要跟着偏移
                    lines.remove(0);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            curEndPos = curBeginPos; // 6.最后结束指针指向下一段的开始处
            paraSpace += mLineSpace;
            mPageLineCount = (mVisibleHeight - paraSpace) / (mFontSize + mLineSpace); // 添加段落间距，实时更新容纳行数
            strParagraph = null;
        }
    }

    /**
     * 根据起始位置指针，读取一页内容
     *
     * @return
     */
    private Vector<String> pageDown() {
        String strParagraph = "";
        Vector<String> lines = new Vector<>();
        int paraSpace = 0;
        mPageLineCount = mVisibleHeight / (mFontSize + mLineSpace);
        while ((lines.size() < mPageLineCount) && (curEndPos < mbBufferLen)) {
            byte[] parabuffer = readParagraphForward(curEndPos);
            curEndPos += parabuffer.length;
            try {
                strParagraph = new String(parabuffer, charset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            strParagraph = strParagraph.replaceAll("\r\n", "  ")
                    .replaceAll("\n", " "); // 段落中的换行符去掉，绘制的时候再换行

            while (strParagraph.length() > 0) {
                int paintSize = mPaint.breakText(strParagraph, true, mVisibleWidth, null);
                lines.add(strParagraph.substring(0, paintSize));
                strParagraph = strParagraph.substring(paintSize);
                if (lines.size() >= mPageLineCount) {
                    break;
                }
            }
            lines.set(lines.size() - 1, lines.get(lines.size() - 1) + "@");
            if (strParagraph.length() != 0) {
                try {
                    curEndPos -= (strParagraph).getBytes(charset).length;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            paraSpace += mLineSpace;
            mPageLineCount = (mVisibleHeight - paraSpace) / (mFontSize + mLineSpace);
            parabuffer = null;
            strParagraph = null;
        }
        return lines;
    }

    public String getPrePage() {
        if (prePage() != BookStatus.LOAD_SUCCESS) {
            return "";
        }
        String title = "";
        if (mLines.size() == 0) {
            curEndPos = curBeginPos;
            mLines = pageDown();
        }
        if (mLines.size() > 0) {
            int y = marginHeight + (mLineSpace << 1);
            StringBuffer sb = new StringBuffer();
            for (String line : mLines) {
                sb.append(line);
            }
            currentText = sb.toString();
            currentText = currentText.substring(0, currentText.lastIndexOf("@"));
            currentText = currentText.replaceAll("@", "\n");
            sb.setLength(0);
            // 保存阅读进度
            SettingManager.getInstance().saveReadProgress(bookId, currentChapter, curBeginPos, curEndPos);
//        String title = "";
//        if (mLines.size() == 0) {
//            curEndPos = curBeginPos;
//            mLines = pageDown();
//        }
//        if (mLines.size() > 0) {
//            SettingManager.getInstance().saveReadProgress(bookId, currentChapter, curBeginPos, curEndPos);
//        }
//        StringBuffer sb = new StringBuffer();
//        for (String line : mLines) {
//            sb.append(line);
//        }
//        currentText = sb.toString();
        }
        return currentText;
    }

    public double getPlayPercent() {

        String s = "";
        byte[] buf = new byte[curEndPos-curBeginPos];
        for (int i = curBeginPos; i < curEndPos; i++) {
            buf[i-curBeginPos] = mbBuff.get(i);
        }

        try {
            s = new String(buf, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return s.length();
    }

    public String getAllChapter(int chapter) {
        openBook(chapter, new int[]{0, 0});
        boolean canload = true;
        StringBuffer sb = new StringBuffer();

        while (canload) {
            if (!hasNextPage()) { // 最后一章的结束页
                canload = false;
            } else {
                tempChapter = currentChapter;
                tempBeginPos = curBeginPos;
                tempEndPos = curEndPos;
                if (curEndPos >= mbBufferLen) { // 中间章节结束页
                    canload = false;
                    continue;
                } else {
                    curBeginPos = curEndPos; // 起始指针移到结束位置
                }
                mLines.clear();
                mLines = pageDown(); // 读取一页内容
                if (mLines.size() > 0) {
                    for (String line : mLines) {
                        sb.append(line);
                    }
                }
            }
        }
        onChapterChanged(chapter);
        currentText = sb.toString();
        currentText = currentText.replaceAll("@", "\n");
        sb.setLength(0);
        // 保存阅读进度
        SettingManager.getInstance().saveReadProgress(bookId, currentChapter, curBeginPos, curEndPos);
        return currentText;
    }

    /**
     * 获取最后一页的内容。比较繁琐，待优化
     *
     * @return
     */
    public Vector<String> pageLast() {
        String strParagraph = "";
        Vector<String> lines = new Vector<>();
        currentPage = 0;
        while (curEndPos < mbBufferLen) {
            int paraSpace = 0;
            mPageLineCount = mVisibleHeight / (mFontSize + mLineSpace);
            curBeginPos = curEndPos;
            while ((lines.size() < mPageLineCount) && (curEndPos < mbBufferLen)) {
                byte[] parabuffer = readParagraphForward(curEndPos);
                curEndPos += parabuffer.length;
                try {
                    strParagraph = new String(parabuffer, charset);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                strParagraph = strParagraph.replaceAll("\r\n", "  ");
                strParagraph = strParagraph.replaceAll("\n", " "); // 段落中的换行符去掉，绘制的时候再换行

                while (strParagraph.length() > 0) {
                    int paintSize = mPaint.breakText(strParagraph, true, mVisibleWidth, null);
                    lines.add(strParagraph.substring(0, paintSize));
                    strParagraph = strParagraph.substring(paintSize);
                    if (lines.size() >= mPageLineCount) {
                        break;
                    }
                }
                lines.set(lines.size() - 1, lines.get(lines.size() - 1) + "@");

                if (strParagraph.length() != 0) {
                    try {
                        curEndPos -= (strParagraph).getBytes(charset).length;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                paraSpace += mLineSpace;
                mPageLineCount = (mVisibleHeight - paraSpace) / (mFontSize + mLineSpace);
                parabuffer = null;
            }
            if (curEndPos < mbBufferLen) {
                lines.clear();
            }
            currentPage++;
        }
        //SettingManager.getInstance().saveReadProgress(bookId, currentChapter, curBeginPos, curEndPos);
        return lines;
    }

    /**
     * 读取下一段落
     *
     * @param curEndPos 当前页结束位置指针
     * @return
     */
    private byte[] readParagraphForward(int curEndPos) {
        byte b0;
        int i = curEndPos;
        while (i < mbBufferLen) {
            b0 = mbBuff.get(i++);
            if (b0 == 0x0a) {
                break;
            }
        }
        int nParaSize = i - curEndPos;
        byte[] buf = new byte[nParaSize];
        for (i = 0; i < nParaSize; i++) {
            buf[i] = mbBuff.get(curEndPos + i);
        }
        return buf;
    }

    /**
     * 读取上一段落
     *
     * @param curBeginPos 当前页起始位置指针
     * @return
     */
    private byte[] readParagraphBack(int curBeginPos) {
        byte b0;
        int i = curBeginPos - 1;
        while (i > 0) {
            b0 = mbBuff.get(i);
            if (b0 == 0x0a && i != curBeginPos - 1) {
                i++;
                break;
            }
            i--;
        }
        int nParaSize = curBeginPos - i;
        byte[] buf = new byte[nParaSize];
        for (int j = 0; j < nParaSize; j++) {
            buf[j] = mbBuff.get(i + j);
        }
        return buf;
    }

    public boolean hasNextPage() {
        int i = -1;
        for (int i1 = 0; i1 < chaptersList.size(); i1++) {
            if (chaptersList.get(i1).getId() == currentChapter) {
                i = i1;
            }
        }
        return i < chaptersList.size() - 1 || curEndPos < mbBufferLen;
    }

    public boolean hasPrePage() {
        int i = 1;
        for (int i1 = 0; i1 < chaptersList.size(); i1++) {
            if (chaptersList.get(i1).getId() == currentChapter) {
                i = i1;
            }
        }
        return i > 0 || (i == 0 && curBeginPos > 0);
    }

    /**
     * 跳转下一页
     */
    public BookStatus nextPage() {
        if (!hasNextPage()) { // 最后一章的结束页
            openVolume(false);
            openVolume = false;
            try {
                ReadActivity.ttsUtis.stop();
            } catch (Exception e) {

            }
            return BookStatus.NO_NEXT_PAGE;
        } else {
            tempChapter = currentChapter;
            tempBeginPos = curBeginPos;
            tempEndPos = curEndPos;
            if (curEndPos >= mbBufferLen) { // 中间章节结束页
                currentChapter++;
                int ret = openBook(currentChapter, new int[]{0, 0}); // 打开下一章
                if (ret == 0) {
                    onLoadChapterFailure(currentChapter);
                    currentChapter--;
                    curBeginPos = tempBeginPos;
                    curEndPos = tempEndPos;
                    return BookStatus.NEXT_CHAPTER_LOAD_FAILURE;
                } else {
                    currentPage = 0;
                    onChapterChanged(currentChapter);
                }
            } else {
                curBeginPos = curEndPos; // 起始指针移到结束位置
            }
            mLines.clear();
            mLines = pageDown(); // 读取一页内容
            onPageChanged(currentChapter, ++currentPage);
        }
        return BookStatus.LOAD_SUCCESS;
    }


    /**
     * 跳转上一页
     */
    public BookStatus prePage() {
        if (!hasPrePage()) { // 第一章第一页
            return BookStatus.NO_PRE_PAGE;
        } else {
            // 保存当前页的值
            tempChapter = currentChapter;
            tempBeginPos = curBeginPos;
            tempEndPos = curEndPos;
            if (curBeginPos <= 0) {
                currentChapter--;
                int ret = openBook(currentChapter, new int[]{0, 0});
                if (ret == 0) {
                    onLoadChapterFailure(currentChapter);
                    currentChapter++;
                    return BookStatus.PRE_CHAPTER_LOAD_FAILURE;
                } else { // 跳转到上一章的最后一页
                    mLines.clear();
                    mLines = pageLast();
                    onChapterChanged(currentChapter);
                    onPageChanged(currentChapter, currentPage);
                    return BookStatus.LOAD_SUCCESS;
                }
            }
            mLines.clear();
            pageUp(); // 起始指针移到上一页开始处
            mLines = pageDown(); // 读取一页内容
            onPageChanged(currentChapter, --currentPage);
        }
        return BookStatus.LOAD_SUCCESS;
    }

    public void cancelPage() {
        currentChapter = tempChapter;
        curBeginPos = tempBeginPos;
        curEndPos = curBeginPos;

        int ret = openBook(currentChapter, new int[]{curBeginPos, curEndPos});
        if (ret == 0) {
            onLoadChapterFailure(currentChapter);
            return;
        }
        mLines.clear();
        mLines = pageDown();
    }

    /**
     * 获取当前阅读位置
     *
     * @return index 0：起始位置 1：结束位置
     */
    public int[] getPosition() {
        return new int[]{currentChapter, curBeginPos, curEndPos};
    }

    public String getHeadLineStr() {
        if (mLines != null && mLines.size() > 1) {
            return mLines.get(0);
        }
        return "";
    }

    /**
     * 设置字体大小
     *
     * @param fontsize 单位：px
     */
    public void setTextFont(int fontsize) {
        LogUtils.i("fontSize=" + fontsize);
//        mFontSize = fontsize;
        mFontSize = (int) ScreenUtils.spToPx(fontsize);
        mLineSpace = mFontSize / 5 * 2;
        mPaint.setTextSize(mFontSize);
        mPageLineCount = mVisibleHeight / (mFontSize + mLineSpace);
        curEndPos = curBeginPos;
        nextPage();
    }

    /**
     * 设置字体颜色
     *
     * @param textColor
     * @param titleColor
     */
    public void setTextColor(int textColor, int titleColor) {
        mPaint.setColor(textColor);
        mTitlePaint.setColor(titleColor);
    }

    public void setTextType(Context context, String path) {
        Typeface typeface = null;
        if (TextUtils.isEmpty(path)) {
            typeface = Typeface.MONOSPACE;
        } else {
            typeface = Typeface.createFromFile(path);
        }

        mPaint.setTypeface(typeface);
        mTitlePaint.setTypeface(typeface);
        curEndPos = curBeginPos;
        nextPage();
    }


    public void setBgBitmap(Bitmap BG) {
        mBookPageBg = BG;
    }

    public void setOnReadStateChangeListener(OnReadStateChangeListener listener) {
        this.listener = listener;
    }

    private void onChapterChanged(int chapter) {
        if (listener != null) {
            listener.onChapterChanged(chapter);
        }
    }

    private void onPageChanged(int chapter, int page) {
        if (listener != null) {
            listener.onPageChanged(chapter, page);
        }
    }

    private void onLoadChapterFailure(int chapter) {
        if (listener != null) {
            listener.onLoadChapterFailure(chapter);
        }
    }

    public void convertBetteryBitmap() {
        batteryView = (ProgressBar) LayoutInflater.from(mContext).inflate(R.layout.layout_battery_progress, null);
        batteryView.setProgressDrawable(ContextCompat.getDrawable(mContext,
                SettingManager.getInstance().getReadTheme() < 4 ?
                        R.drawable.seekbar_battery_bg : R.drawable.seekbar_battery_night_bg));
        batteryView.setProgress(battery);
        batteryView.setDrawingCacheEnabled(true);
        batteryView.measure(View.MeasureSpec.makeMeasureSpec(ScreenUtils.dpToPxInt(26), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(ScreenUtils.dpToPxInt(14), View.MeasureSpec.EXACTLY));
        batteryView.layout(0, 0, batteryView.getMeasuredWidth(), batteryView.getMeasuredHeight());
        batteryView.buildDrawingCache();
        //batteryBitmap = batteryView.getDrawingCache();
        // tips: @link{https://github.com/JustWayward/BookReader/issues/109}
        batteryBitmap = Bitmap.createBitmap(batteryView.getDrawingCache());
        batteryView.setDrawingCacheEnabled(false);
        batteryView.destroyDrawingCache();
    }

    public void setBattery(int battery) {
        this.battery = battery;
        convertBetteryBitmap();
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void recycle() {
        if (mBookPageBg != null && !mBookPageBg.isRecycled()) {
            mBookPageBg.recycle();
            mBookPageBg = null;
        }

        if (chaptersList != null) {
            chaptersList.clear();
            chaptersList = null;
        }

        currentText = null;
        if (mbBuff != null) {
            mbBuff.clear();
        }

        if (mLines != null) {
            mLines.clear();
            mLines = null;
        }

        if (batteryBitmap != null && !batteryBitmap.isRecycled()) {
            batteryBitmap.recycle();
            batteryBitmap = null;
        }
    }

    public void setReadProgress(int percent, int beginPos, int endPos) {
        this.percent = percent;
        this.beginPos = beginPos;
        this.endPos = endPos;
    }

}
