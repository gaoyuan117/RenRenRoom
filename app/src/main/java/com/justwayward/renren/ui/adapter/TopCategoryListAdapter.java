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
package com.justwayward.renren.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.justwayward.renren.R;
import com.justwayward.renren.bean.CategoryBean;
import com.justwayward.renren.ui.activity.SubCategoryActivity;
import com.justwayward.renren.ui.activity.WordNumRangeActivity;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.util.List;

/**
 * @author lfh.
 * @date 16/8/30.
 */
public class TopCategoryListAdapter extends EasyRVAdapter<CategoryBean.SubCategoryBean> {

    private String type;

    public TopCategoryListAdapter(Context context, List<CategoryBean.SubCategoryBean> list, String type) {
        super(context, list, R.layout.item_top_category_list);
        this.type = type;
    }

    @Override
    protected void onBindData(final EasyRVHolder holder, final int position, final CategoryBean.SubCategoryBean item) {
        TextView tvNum = holder.getView(R.id.tvBookCount);

        String name = "";
        if (type.equals("word_count")) {
            name = item.getName();
//            tvNum.setVisibility(View.GONE);
        } else {
            name = item.getCategory();
//            tvNum.setVisibility(View.VISIBLE);
        }

        holder.setText(R.id.tvName, name)
                .setText(R.id.tvBookCount, String.format(mContext.getString(R.string
                        .category_book_count), item.getNovel_num()));

        holder.getView(R.id.item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (type.equals("word_count")) {
                    mContext.startActivity(new Intent(mContext,
                            WordNumRangeActivity.class).putExtra("range", item.getMin() + "|" + item.getMax()).putExtra("title", item.getName() + ""));
                } else {
                    mContext.startActivity(new Intent(mContext,
                            SubCategoryActivity.class).putExtra("type", item.getCategory()).putExtra("id", item.getId() + ""));
                }
            }
        });

    }

}
