package net.performance.assessment.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import net.performance.assessment.R;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.CardApprovalInfo;
import net.performance.assessment.entity.QueryCardList;
import net.performance.assessment.entity.QueryGrabSingleBean;
import net.performance.assessment.network.http.AttendanceTaskAPI;
import net.performance.assessment.utils.JsonParser;
import net.performance.assessment.utils.ToastUtil;
import net.performance.assessment.view.adapter.CardApprovalAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询补卡申请
 */
public class CardApprovalListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private long mFlag;
    private ListView mListView;
    private LinearLayout mLlNoData;

    private List<CardApprovalInfo> mInfoList = new ArrayList<>();
    private CardApprovalInfo mSelectInfo;
    private QueryCardList mCard;
    private CardApprovalAdapter mAdapter;

    @Override
    protected void setContentView() {
        super.setContentView();
        setContentView(R.layout.activity_list);

        mListView = findViewById(R.id.task_listview);
        mListView.setOnItemClickListener(this);

        mLlNoData = findViewById(R.id.ll_no_data);

        mAdapter = new CardApprovalAdapter();
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void initView() {
        super.initView();
        mCustomTitleBar.setTitle("待审批");
    }

    @Override
    protected void initData() {
        super.initData();

        showProgressDialog("");
        mFlag = AttendanceTaskAPI.findWorkListQuery("001", mHttpCallback);
    }

    @Override
    protected void handleResponseSuccess(String result, long flag) {
        super.handleResponseSuccess(result, flag);
        BaseResultBean detectedBean = JsonParser.getInstance( ).getBeanFromJsonString( result , BaseResultBean.class );
        if (detectedBean.isSuccess()) {
            if (mFlag == flag) {
                mCard = JsonParser.getInstance( ).getBeanFromJsonString(result, QueryCardList.class );
                if (mCard.data != null && mCard.data.size() > 0) {
                    showListView();
                    mInfoList.addAll(mCard.data);
                    mAdapter.setDataList(mInfoList);
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
        mListView.setVisibility(View.VISIBLE);
        mLlNoData.setVisibility(View.GONE);
    }

    private void hideListView() {
        mListView.setVisibility(View.GONE);
        mLlNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mSelectInfo = mInfoList.get(position);
        if (mSelectInfo != null) {
            Intent intent = new Intent(this, CardApprovalActivity.class);
            intent.putExtra("card_approval", mSelectInfo);
            startActivityForResult(intent, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            mInfoList.remove(mSelectInfo);
            mAdapter.notifyDataSetChanged();
            if (mInfoList == null || mInfoList.size() <= 0) {
                hideListView();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInfoList.clear();
        mSelectInfo = null;
        mCard = null;
    }
}
