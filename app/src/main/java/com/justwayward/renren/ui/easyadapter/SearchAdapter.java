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
package com.justwayward.renren.ui.easyadapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.justwayward.renren.R;
import com.justwayward.renren.bean.SearchBean;
import com.justwayward.renren.view.recyclerview.adapter.BaseViewHolder;
import com.justwayward.renren.view.recyclerview.adapter.RecyclerArrayAdapter;

/**
 * 查询
 *
 * @author yuyh.
 * @date 16/9/3.
 */
public class SearchAdapter extends RecyclerArrayAdapter<SearchBean.DataBean> {

    public SearchAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder<SearchBean.DataBean>(parent, R.layout.item_search_result_list) {
            @Override
            public void setData(SearchBean.DataBean item) {
                String percent = "0";
                if (item.getCollect_num() != 0 && item.getView_num() != 0) {
                    double d = (item.getCollect_num() * 100 / item.getView_num());
                    percent = String.format("%.0f", d);
                }

                holder.setRoundImageUrl(R.id.ivBookCover, item.getPic(), R.drawable.cover_default)
                        .setText(R.id.tvBookListTitle, item.getTitle())
                        .setText(R.id.tvLatelyFollower, String.format(mContext.getString(R.string.search_result_lately_follower), item.getCollect_num()))
                        .setText(R.id.tvRetentionRatio, String.format(mContext.getString(R.string.search_result_retention_ratio), percent))
                        .setText(R.id.tvBookListAuthor, item.getAuthor());
            }
        };
    }
}