package com.justwayward.renren.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.justwayward.renren.R;
import com.justwayward.renren.bean.BookDetailBean;
import com.justwayward.renren.common.OnRvItemClickListener;
import com.justwayward.renren.utils.FormatUtils;
import com.justwayward.renren.utils.NoDoubleClickListener;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by gaoyuan on 2017/11/15.
 */

public class LikeBookListAdapter extends EasyRVAdapter<BookDetailBean.GuestListBean> {

    private OnRvItemClickListener itemClickListener;

    public LikeBookListAdapter(Context context, List<BookDetailBean.GuestListBean> list,
                                    OnRvItemClickListener listener) {
        super(context, list, R.layout.item_book_detail_like);
        this.itemClickListener = listener;
    }

    @Override
    protected void onBindData(final EasyRVHolder holder, final int position, final BookDetailBean.GuestListBean item) {

        holder.setRoundImageUrl(R.id.ivBookListCover, item.getPic(), R.drawable.cover_default);
                TextView tvNum = holder.getView(R.id.tvWordCount);

        holder.setText(R.id.tvBookListTitle, item.getTitle())
                .setText(R.id.tvBookListAuthor, item.getAuthor()+" | ")
                .setText(R.id.tvCatgory, item.getCategory_name()+" | ")
                .setText(R.id.time, FormatUtils.getDescriptionTimeFromDateString(item.getAdd_time()+"000"));

        if (item.getWord_num()<10000){
            tvNum.setText(item.getWord_num()+"字");
        }else {
            BigDecimal old = new BigDecimal(item.getWord_num() * 0.0001 ).setScale(2, BigDecimal.ROUND_HALF_UP);

            tvNum.setText(old+"万字");
        }

        holder.setOnItemViewClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                itemClickListener.onItemClick(holder.getItemView(), position, item);
            }
        });
    }

}