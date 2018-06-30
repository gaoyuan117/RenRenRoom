/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.justwayward.renren.ui.presenter;

import android.text.TextUtils;

import com.justwayward.renren.ReaderApplication;
import com.justwayward.renren.api.BookApi;
import com.justwayward.renren.base.RxPresenter;
import com.justwayward.renren.bean.BookDetailBean;
import com.justwayward.renren.retrofit.BaseObjObserver;
import com.justwayward.renren.retrofit.HttpResult;
import com.justwayward.renren.retrofit.RetrofitClient;
import com.justwayward.renren.retrofit.RxUtils;
import com.justwayward.renren.ui.contract.BookDetailContract;

import javax.inject.Inject;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public class BookDetailPresenter extends RxPresenter<BookDetailContract.View> implements BookDetailContract.Presenter<BookDetailContract.View> {

    private BookApi bookApi;

    private static final String TAG = "BookDetailPresenter";

    @Inject
    public BookDetailPresenter(BookApi bookApi) {
        this.bookApi = bookApi;
    }

    public void getBookDetail(String bookId) {

        RetrofitClient.getInstance().createApi().getNovelInfo(ReaderApplication.token, bookId)
                .compose(RxUtils.<HttpResult<BookDetailBean>>io_main())
                .subscribe(new BaseObjObserver<BookDetailBean>(mView.getContext()) {
                    @Override
                    protected void onHandleSuccess(BookDetailBean bookDetailBean) {
                        if (bookDetailBean == null || mView == null) {
                            return;
                        }
                        mView.showBookDetail(bookDetailBean);
                    }
                });
    }

    @Override
    public void getHotReview(String book) {
//        Subscription rxSubscription = bookApi.getHotReview(book).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<HotReview>() {
//                    @Override
//                    public void onNext(HotReview data) {
//                        List<HotReview.Reviews> list = data.reviews;
//                        if (list != null && !list.isEmpty() && mView != null) {
//                            mView.showHotReview(list);
//                        }
//                    }
//
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                    }
//                });
//        addSubscrebe(rxSubscription);
    }

    @Override
    public void getRecommendBookList(String bookId, String limit) {
//        Subscription rxSubscription = bookApi.getRecommendBookList(bookId, limit).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<RecommendBookList>() {
//                    @Override
//                    public void onNext(RecommendBookList data) {
//                        LogUtils.i(data.booklists);
//                        List<RecommendBookList.RecommendBook> list = data.booklists;
//                        if (list != null && !list.isEmpty() && mView != null) {
//                            mView.showRecommendBookList(list);
//                        }
//                    }
//
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        LogUtils.e("+++" + e.toString());
//                    }
//                });
//        addSubscrebe(rxSubscription);
    }

}
