package net.performance.assessment.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import net.performance.assessment.R;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.GrabSingleBean;
import net.performance.assessment.entity.PersonUnSignBean;
import net.performance.assessment.entity.QueryPersonUnSignBean;
import net.performance.assessment.network.http.AttendanceTaskAPI;
import net.performance.assessment.network.http.OrderSheetTaskAPI;
import net.performance.assessment.utils.JsonParser;
import net.performance.assessment.utils.ToastUtil;
import net.performance.assessment.view.adapter.PersonUnSignAdapter;
import net.performance.assessment.view.widget.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 待考勤列表
 */
public class QueryPersonUnsignActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private List<PersonUnSignBean> mUnsignList = new ArrayList<>();
    private PersonUnSignBean mSelectUnSign;
    private QueryPersonUnSignBean mQueryUnsign;
    private PersonUnSignAdapter mAdapter;
    private long mQueryFlag;
    private LinearLayout mLlNoData;
    private int flagSource;

    @Override
    protected void setContentView() {
        super.setContentView();
        setContentView(R.layout.activity_list);
    }

    @Override
    protected void initView() {
        super.initView();
        mCustomTitleBar.setTitle("待考勤");
        mListView = findViewById(R.id.task_listview);
        mListView.setOnItemClickListener(this);

        mLlNoData = findViewById(R.id.ll_no_data);

        mAdapter = new PersonUnSignAdapter();
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        super.initData();
        flagSource = getIntent().getExtras().getInt("sign_flag", 0);

        mLoadingDialog = new LoadingDialog(this);
        mLoadingDialog.show();
        mQueryFlag = AttendanceTaskAPI.personUnSignTask(mHttpCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnsignList.clear();
        mUnsignList = null;
        mQueryUnsign = null;
        mSelectUnSign = null;
    }

    @Override
    protected void handleResponseSuccess(String result, long flag) {
        super.handleResponseSuccess(result, flag);
        BaseResultBean detectedBean = JsonParser.getInstance( ).getBeanFromJsonString( result , BaseResultBean.class );
        if (detectedBean.isSuccess()) {
            if (mQueryFlag == flag) {
                mQueryUnsign = JsonParser.getInstance( ).getBeanFromJsonString(result, QueryPersonUnSignBean.class );
                if (mQueryUnsign.data != null && mQueryUnsign.data.size() > 0) {
                    showListView();
                    mUnsignList.addAll(mQueryUnsign.data);
                    mAdapter.setDataList(mUnsignList);
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
        mSelectUnSign = mUnsignList.get(position);
        if (mSelectUnSign != null) {
            Intent intent = null;
            if (flagSource == 0) {
                intent = new Intent(this, FieldPunchActivity.class);
            } else if (flagSource == 1) {
                intent = new Intent(this, MakeUpCardApplicationActivity.class);
            }
            intent.putExtra("person_unsign", mSelectUnSign);
            startActivityForResult(intent, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            mUnsignList.remove(mSelectUnSign);
            mAdapter.notifyDataSetChanged();
            if (mUnsignList == null || mUnsignList.size() <= 0) {
                hideListView();
            }
        }
    }

}
