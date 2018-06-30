package com.justwayward.renren.view.readview;

/**
 * @Project_Name :RenRenRoom
 * @package_Name :com.justwayward.book.view.readview
 * @AUTHOR :xzwzz@vip.qq.com
 * @DATE :2018/1/29  10:59
 */
public interface ReadInterface {
    void setTextColor(int textColor, int titleColor);

    boolean isPrepare();

    void jumpToChapter(int chapter);

    String getCurrentText();

    double getPercent();

    void setTheme(int theme);

    int[] getReadPos();

    String getHeadLine();

    void setPosition(int[] pos);

    void setTextType(String path);

    void nextPage();

    void prePage();

    void setBattery(int battery);

    void setTime(String time);

    void setFontSize(final int fontSizePx);

    void init(int theme);

    void readProgress(int percent, int beginPos, int endPos);

    void stopRead();
}
