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
package com.justwayward.renren.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.justwayward.renren.R;
import com.justwayward.renren.ReaderApplication;
import com.justwayward.renren.base.BaseActivity;
import com.justwayward.renren.bean.CommonBean;
import com.justwayward.renren.bean.FeedBackBean;
import com.justwayward.renren.component.AppComponent;
import com.justwayward.renren.retrofit.BaseObjObserver;
import com.justwayward.renren.retrofit.HttpResult;
import com.justwayward.renren.retrofit.RetrofitClient;
import com.justwayward.renren.retrofit.RxUtils;
import com.justwayward.renren.ui.adapter.FeedbackAdapter;
import com.justwayward.renren.view.recyclerview.decoration.DividerDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class FeedbackActivity extends BaseActivity implements TextWatcher, BaseQuickAdapter.OnItemClickListener {

    @Bind(R.id.et_input)
    EditText etInput;
    @Bind(R.id.tv_num)
    TextView tvNum;
    @Bind(R.id.bt_commit)
    Button btCommit;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;

    private int id = -1;

    private List<FeedBackBean> mList = new ArrayList<>();
    private FeedbackAdapter mAdapter;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, FeedbackActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

        mAdapter = new FeedbackAdapter(R.layout.item_feed, mList);

        etInput.addTextChangedListener(this);

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
        DividerDecoration itemDecoration = new DividerDecoration(ContextCompat.getColor(getActivity(), R.color.white), 20, 0, 0);
        itemDecoration.setDrawLastItem(false);
        recyclerView.addItemDecoration(itemDecoration);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setTitle("意见反馈");
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
    }

    @Override
    public void initDatas() {
        getType();
    }

    @Override
    public void configViews() {

    }


    @OnClick(R.id.bt_commit)
    public void onViewClicked() {
        feed();
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        tvNum.setText(s.length()+"/200");
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        mAdapter.setPosition(position);
        id = mList.get(position).getId();
    }

    private void getType(){
        RetrofitClient.getInstance().createApi().getType("")
                .compose(RxUtils.<HttpResult<List<FeedBackBean>>>io_main())
                .subscribe(new BaseObjObserver<List<FeedBackBean>>(this) {
                    @Override
                    protected void onHandleSuccess(List<FeedBackBean> list) {
                        if(list==null||list.isEmpty()){
                            return;
                        }
                        mList.clear();
                        mList.addAll(list);
                        mAdapter.notifyDataSetChanged();

                    }
                });
    }

    private void feed(){
        if(id==-1){
            showToastMsg("请选择反馈类型");
            return;
        }
        String content = etInput.getText().toString();
        if(TextUtils.isEmpty(content)){
            showToastMsg("请输入反馈内容");
            return;
        }

        RetrofitClient.getInstance().createApi().feedback(ReaderApplication.token,id+"",content)
                .compose(RxUtils.<HttpResult<CommonBean>>io_main())
                .subscribe(new BaseObjObserver<CommonBean>(this) {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        showToastMsg("反馈成功");
                        finish();
                    }
                });
    }
}
