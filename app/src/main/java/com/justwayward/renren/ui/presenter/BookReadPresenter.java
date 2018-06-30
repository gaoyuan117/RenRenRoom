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
package com.justwayward.renren.ui.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.justwayward.renren.ReaderApplication;
import com.justwayward.renren.api.BookApi;
import com.justwayward.renren.base.RxPresenter;
import com.justwayward.renren.bean.BookBean;
import com.justwayward.renren.bean.ChapterListBean;
import com.justwayward.renren.retrofit.BaseObjObserver;
import com.justwayward.renren.retrofit.HttpResult;
import com.justwayward.renren.retrofit.RetrofitClient;
import com.justwayward.renren.retrofit.RxUtils;
import com.justwayward.renren.ui.contract.BookReadContract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * @author lfh.
 * @date 2016/8/7.
 */
public class BookReadPresenter extends RxPresenter<BookReadContract.View>
        implements BookReadContract.Presenter<BookReadContract.View> {

    private Context mContext;
    private BookApi bookApi;

    @Inject
    public BookReadPresenter(Context mContext, BookApi bookApi) {
        this.mContext = mContext;
        this.bookApi = bookApi;
    }

    @Override
    public void getBookMixAToc(final String bookId, String viewChapters) {

        RetrofitClient.getInstance().createApi().getChapterList(ReaderApplication.token, bookId)
                .compose(RxUtils.<HttpResult<List<ChapterListBean>>>io_main())
                .subscribe(new BaseObjObserver<List<ChapterListBean>>(mContext) {
                    @Override
                    protected void onHandleSuccess(List<ChapterListBean> listBeen) {
                        if (listBeen == null || listBeen.size() == 0) {
                            return;
                        }
                        if (mView == null) {
                            return;
                        }
                        mView.showBookToc(listBeen);
                    }

                });


    }

    @Override
    public void getChapterRead(String chapterId, String sourceId) {

        Map<String, Object> map = new HashMap<>();
        map.put("token", ReaderApplication.token);
        map.put("id", chapterId);

        if (!TextUtils.isEmpty(sourceId)) {
            map.put("source_id", sourceId);
        }


        RetrofitClient.getInstance().createApi().getChapterContent(map)
                .compose(RxUtils.<HttpResult<BookBean>>io_main())
                .subscribe(new BaseObjObserver<BookBean>(mContext) {
                    @Override
                    protected void onHandleSuccess(BookBean bookBean) {
                        if (mView == null || bookBean == null) {
//                            mView.netError(0);
                            return;
                        }

                        mView.showChapterRead(bookBean, bookBean.getId());
                    }
                });
    }
}