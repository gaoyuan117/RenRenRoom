package com.justwayward.renren.manager;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.justwayward.renren.ReaderApplication;
import com.justwayward.renren.base.Constant;
import com.justwayward.renren.bean.BookBean;
import com.justwayward.renren.bean.BookLists;
import com.justwayward.renren.bean.BookMixAToc;
import com.justwayward.renren.utils.ACache;
import com.justwayward.renren.utils.AppUtils;
import com.justwayward.renren.utils.FileUtils;
import com.justwayward.renren.utils.LogUtils;
import com.justwayward.renren.utils.SharedPreferencesUtil;
import com.justwayward.renren.utils.StringUtils;
import com.justwayward.renren.utils.ToastUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yuyh.
 * @date 2016/9/28.
 */
public class CacheManager {

    private static CacheManager manager;

    public static CacheManager getInstance() {
        return manager == null ? (manager = new CacheManager()) : manager;
    }

    public List<String> getSearchHistory() {
        return SharedPreferencesUtil.getInstance().getObject(getSearchHistoryKey(), List.class);
    }

    public void saveSearchHistory(Object obj) {
        SharedPreferencesUtil.getInstance().putObject(getSearchHistoryKey(), obj);
    }

    private String getSearchHistoryKey() {
        return "searchHistory";
    }

    /**
     * 获取我收藏的书单列表
     *
     * @return
     */
    public List<BookLists.BookListsBean> getCollectionList() {
        List<BookLists.BookListsBean> list = (ArrayList<BookLists.BookListsBean>) ACache.get(
                ReaderApplication.getsInstance()).getAsObject(getCollectionKey());
        return list == null ? null : list;
    }

    public void removeCollection(String bookListId) {
        List<BookLists.BookListsBean> list = getCollectionList();
        if (list == null) {
            return;
        }
        for (BookLists.BookListsBean bean : list) {
            if (bean != null) {
                if (TextUtils.equals(bean._id, bookListId)) {
                    list.remove(bean);
                    ACache.get(ReaderApplication.getsInstance()).put(getCollectionKey(), (Serializable) list);
                    break;
                }
            }
        }
    }

    public void addCollection(BookLists.BookListsBean bean) {
        List<BookLists.BookListsBean> list = getCollectionList();
        if (list == null) {
            list = new ArrayList<>();
        }
        for (BookLists.BookListsBean data : list) {
            if (data != null) {
                if (TextUtils.equals(data._id, bean._id)) {
                    ToastUtils.showToast("已经收藏过啦");
                    return;
                }
            }
        }
        list.add(bean);
        ACache.get(ReaderApplication.getsInstance()).put(getCollectionKey(), (Serializable) list);
        ToastUtils.showToast("收藏成功");
    }

    private String getCollectionKey() {
        return "my_book_lists";
    }

    /**
     * 获取目录缓存
     *
     * @param mContext
     * @param bookId
     * @return
     */
    public List<BookMixAToc.mixToc.Chapters> getTocList(Context mContext, String bookId) {
        Object obj = ACache.get(mContext).getAsObject(getTocListKey(bookId));
        if (obj != null) {
            try {
                BookMixAToc data = (BookMixAToc) obj;
                List<BookMixAToc.mixToc.Chapters> list = data.mixToc.chapters;
                if (list != null && !list.isEmpty()) {
                    return list;
                }
            } catch (Exception e) {
                ACache.get(mContext).remove(getTocListKey(bookId));
            }
        }
        return null;
    }

    public void saveTocList(Context mContext, String bookId, BookMixAToc data) {
        ACache.get(mContext).put(getTocListKey(bookId), data);
    }

    public void removeTocList(Context mContext, String bookId) {
        ACache.get(mContext).remove(getTocListKey(bookId));
    }

    private String getTocListKey(String bookId) {
        return bookId + "-bookToc";
    }

    public File getChapterFile(String bookId, int chapter) {
        File file = FileUtils.getChapterFile(bookId, chapter);
        if (file != null && file.length() > 50)
            return file;
        return null;
    }

    public void saveChapterFile(String bookId, int chapter, BookBean data) {
        File file = FileUtils.getChapterFile(bookId, chapter);
        FileUtils.writeFile(file.getAbsolutePath(), StringUtils.formatContent(data.getContent()), false);
    }

    public void delChapterFile(String bookId, int chapter) {
        File file = FileUtils.getChapterFile(bookId, chapter);
        file.delete();
//        FileUtils.writeFile(file.getAbsolutePath(), StringUtils.formatContent(data.getContent()), false);
    }

    /**
     * 获取缓存大小
     *
     * @return
     */
    public synchronized String getCacheSize() {
        long cacheSize = 0;

        try {
            String cacheDir = Constant.BASE_PATH;
            LogUtils.e("cache："+cacheDir);
            cacheSize += FileUtils.getFolderSize(cacheDir);
            if (FileUtils.isSdCardAvailable()) {
                String extCacheDir = AppUtils.getAppContext().getExternalCacheDir().getPath();
                cacheSize += FileUtils.getFolderSize(extCacheDir);
            }
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }

        return FileUtils.formatFileSizeToString(cacheSize);
    }

    /**
     * 清除缓存
     *
     * @param clearReadPos 是否删除阅读记录
     */
    public synchronized void clearCache(boolean clearReadPos, boolean clearCollect) {
        try {
            // 删除内存缓存
            String cacheDir = AppUtils.getAppContext().getCacheDir().getPath();
            FileUtils.deleteFileOrDirectory(new File(cacheDir));
            if (FileUtils.isSdCardAvailable()) {
                // 删除SD书籍缓存
                FileUtils.deleteFileOrDirectory(new File(Constant.PATH_DATA));
            }
            // 删除阅读记录（SharePreference）
            if (clearReadPos) {
                //防止再次弹出性别选择框，sp要重写入保存的性别
                String chooseSex = SettingManager.getInstance().getUserChooseSex();
                SharedPreferencesUtil.getInstance().removeAll();
                SettingManager.getInstance().saveUserChooseSex(chooseSex);
                SharedPreferencesUtil.getInstance().putString("token",ReaderApplication.token);
                SharedPreferencesUtil.getInstance().putString("uid",ReaderApplication.uid);
            }
            // 清空书架
            if (clearCollect) {
                CollectionsManager.getInstance().clear();
            }
            // 清除其他缓存
            ACache.get(AppUtils.getAppContext()).clear();
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
    }

}
