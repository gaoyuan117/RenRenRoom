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

import android.content.Context;

import com.justwayward.renren.api.BookApi;
import com.justwayward.renren.base.RxPresenter;
import com.justwayward.renren.ui.contract.BookSourceContract;

import javax.inject.Inject;

/**
 * @author yuyh.
 * @date 2016/9/8.
 */
public class BookSourcePresenter extends RxPresenter<BookSourceContract.View> implements BookSourceContract.Presenter {

    private BookApi bookApi;
    private Context context;

    @Inject
    public BookSourcePresenter(BookApi bookApi) {
        this.bookApi = bookApi;
    }

    @Override
    public void getBookSource( String book,String chapter) {


    }
}
