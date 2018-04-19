package net.performance.assessment.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import net.performance.assessment.R;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.GrabSingleBean;
import net.performance.assessment.entity.QueryGrabSingleBean;
import net.performance.assessment.network.http.OrderSheetTaskAPI;
import net.performance.assessment.utils.JsonParser;
import net.performance.assessment.utils.ToastUtil;
import net.performance.assessment.view.adapter.GrabSingleAdapter;
import net.performance.assessment.view.widget.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 抢单进行中列表
 */
public class QueryProcessingGrabSingleActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private List<GrabSingleBean> mGrabList = new ArrayList<>();
    private GrabSingleBean mSelectGrabBean;
    private QueryGrabSingleBean mQueryGrabBean;
    private GrabSingleAdapter mAdapter;
    private long mQueryFlag;
    private LinearLayout mLlNoData;

    @Override
    protected void setContentView() {
        super.setContentView();
        setContentView(R.layout.activity_list);
    }

    @Override
    protected void initView() {
        super.initView();
        mCustomTitleBar.setTitle("抢单进行中");
        mListView = findViewById(R.id.task_listview);
        mListView.setOnItemClickListener(this);

        mLlNoData = findViewById(R.id.ll_no_data);

        mAdapter = new GrabSingleAdapter();
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        super.initData();
        mLoadingDialog = new LoadingDialog(this);
        mLoadingDialog.show();
        mQueryFlag = OrderSheetTaskAPI.orderSheetQuery("", "", "002", mHttpCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGrabList.clear();
        mGrabList = null;
        mSelectGrabBean = null;
    }

    @Override
    protected void handleResponseSuccess(String result, long flag) {
        super.handleResponseSuccess(result, flag);
        BaseResultBean detectedBean = JsonParser.getInstance( ).getBeanFromJsonString( result , BaseResultBean.class );
        if (detectedBean.isSuccess()) {
            if (mQueryFlag == flag) {
                mQueryGrabBean = JsonParser.getInstance( ).getBeanFromJsonString(result, QueryGrabSingleBean.class );
                if (mQueryGrabBean.data != null && mQueryGrabBean.data.size() > 0) {
                    showListView();
                    mGrabList.addAll(mQueryGrabBean.data);
                    mAdapter.setDataList(mGrabList);
                    mAdapter.notifyDataSetChanged();
                } else {
                    hideListView();
                }
            }
        } else {
            hideListView();
            ToastUtil.showCenterTip(this, detectedBean.message);
        }
    }

    @Override
    protected void handleResponseTimeout(long flag) {
        super.handleResponseTimeout(flag);
        hideListView();
    }

    @Override
    protected void handleResponseFailure(int error, long flag) {
        super.handleResponseFailure(error, flag);
        hideListView();
    }

    private void showListView() {
        mListView.setVisibility(View.VISIBLE);
        mLlNoData.setVisibility(View.GONE);
    }

    private void hideListView() {
        mListView.setVisibility(View.GONE);
        mLlNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mSelectGrabBean = mGrabList.get(position);
        if (mSelectGrabBean != null) {
            Intent intent = new Intent(this, GrabSingleProgressActivity.class);
            intent.putExtra("grab_single", mSelectGrabBean);
            startActivityForResult(intent, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            mGrabList.remove(mSelectGrabBean);
            mAdapter.notifyDataSetChanged();
            if (mGrabList == null || mGrabList.size() <= 0) {
                hideListView();
            }
        }
    }

}
