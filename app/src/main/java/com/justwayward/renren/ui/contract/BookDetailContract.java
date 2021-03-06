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
package com.justwayward.renren.ui.contract;

import android.content.Context;

import com.justwayward.renren.base.BaseContract;
import com.justwayward.renren.bean.BookDetailBean;
import com.justwayward.renren.bean.HotReview;
import com.justwayward.renren.bean.RecommendBookList;

import java.util.List;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface BookDetailContract {

    interface View extends BaseContract.BaseView {
        void showBookDetail(BookDetailBean data);

        void showHotReview(List<HotReview.Reviews> list);

        void showRecommendBookList(List<RecommendBookList.RecommendBook> list);
        Context getContext();

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getBookDetail(String bookId);

        void getHotReview(String book);

        void getRecommendBookList(String bookId, String limit);

    }

}
