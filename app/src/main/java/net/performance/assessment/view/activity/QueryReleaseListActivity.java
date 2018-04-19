package net.performance.assessment.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import net.performance.assessment.R;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.QueryReleaseListBean;
import net.performance.assessment.entity.ReleaseListBean;
import net.performance.assessment.network.http.ReleaseListTaskAPI;
import net.performance.assessment.utils.JsonParser;
import net.performance.assessment.utils.ToastUtil;
import net.performance.assessment.view.adapter.ReleaseListAdapter;
import net.performance.assessment.view.widget.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

public class QueryReleaseListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView mReleaseListView;
    private long queryFlag;

    private QueryReleaseListBean queryResult;
    private ReleaseListAdapter mAdapter;
    private List<ReleaseListBean> mReleaseList = new ArrayList<>();
    private ReleaseListBean mSelectReleaseBean;
    private LinearLayout mLlNoData;

    @Override
    protected void setContentView() {
        super.setContentView();
        setContentView(R.layout.activity_list);
    }

    @Override
    protected void initView() {
        super.initView();
        mCustomTitleBar.setTitle("查询发布单");
        mReleaseListView = findViewById(R.id.task_listview);
        mReleaseListView.setOnItemClickListener(this);

        mLlNoData = findViewById(R.id.ll_no_data);

        mAdapter = new ReleaseListAdapter();
        mReleaseListView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        super.initData();

        queryReleaseList();
    }

    private void queryReleaseList() {
        showProgressDialog("");
        queryFlag = ReleaseListTaskAPI.queryReleaseList("", "", "001", mHttpCallback);
    }

    @Override
    protected void handleResponseSuccess(String result, long flag) {
        super.handleResponseSuccess(result, flag);
        BaseResultBean detectedBean = JsonParser.getInstance( ).getBeanFromJsonString( result , BaseResultBean.class );
        if (detectedBean.isSuccess()) {
            if (queryFlag == flag) {
                queryResult = JsonParser.getInstance( ).getBeanFromJsonString(result, QueryReleaseListBean.class );
                if (queryResult.data != null && queryResult.data.size() > 0) {
                    showListView();
                    mReleaseList.addAll(queryResult.data);
                    mAdapter.setDataList(mReleaseList);
                    mAdapter.notifyDataSetChanged();
                } else {
                    hideListView();
                }
            } else {
                hideListView();
            }
        } else {
            hideListView();
            ToastUtil.showCenterTip(this, detectedBean.message);
        }
    }

    @Override
    protected void handleResponseFailure(int error, long flag) {
        super.handleResponseFailure(error, flag);
        hideListView();
    }

    @Override
    protected void handleResponseTimeout(long flag) {
        super.handleResponseTimeout(flag);
        hideListView();
    }

    private void showListView() {
        mReleaseListView.setVisibility(View.VISIBLE);
        mLlNoData.setVisibility(View.GONE);
    }

    private void hideListView() {
        mReleaseListView.setVisibility(View.GONE);
        mLlNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mSelectReleaseBean = mReleaseList.get(position);
        if (mSelectReleaseBean != null) {
            Intent intent = new Intent(this, GrabSingleActivity.class);
            intent.putExtra("release_list", mSelectReleaseBean);
            startActivityForResult(intent, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            mReleaseList.remove(mSelectReleaseBean);
            mAdapter.notifyDataSetChanged();
            if (mReleaseList == null || mReleaseList.size() <= 0) {
                hideListView();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReleaseList.clear();
        mReleaseList = null;
        mSelectReleaseBean = null;
        mAdapter = null;
    }
}
