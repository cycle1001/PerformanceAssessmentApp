package net.performance.assessment.view.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.performance.assessment.R;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.MyTaskBean;
import net.performance.assessment.entity.QueryCurrentTaskBean;
import net.performance.assessment.entity.QueryGrabSingleBean;
import net.performance.assessment.network.http.AttendanceTaskAPI;
import net.performance.assessment.utils.IntentUtils;
import net.performance.assessment.utils.JsonParser;
import net.performance.assessment.utils.ToastUtil;
import net.performance.assessment.view.adapter.MyTaskAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的任务列表页
 */

public class MyTaskListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private List<MyTaskBean> mTaskList = new ArrayList<>();
    private QueryCurrentTaskBean mCurrentTaskBean;
    private MyTaskAdapter mAdapter;

    private long mFlag;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_my_task_list);
    }

    @Override
    protected void initView() {
        super.initView();
        mListView = findViewById(R.id.task_listview);
        mListView.setAdapter(null);
        mListView.setOnItemClickListener(this);

        mAdapter = new MyTaskAdapter();
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        super.initData();
        getCurrentTask();
    }

    private void getCurrentTask() {
        showProgressDialog("");
        mFlag = AttendanceTaskAPI.personCurrentTask(mHttpCallback);
    }

    @Override
    protected void handleResponseSuccess(String result, long flag) {
        super.handleResponseSuccess(result, flag);
        BaseResultBean detectedBean = JsonParser.getInstance( ).getBeanFromJsonString( result , BaseResultBean.class );
        if (detectedBean.isSuccess()) {
            if (mFlag == flag) {
                mCurrentTaskBean = JsonParser.getInstance( ).getBeanFromJsonString(result, QueryCurrentTaskBean.class );
                if (mCurrentTaskBean.data != null && mCurrentTaskBean.data.size() > 0) {
                    mTaskList.addAll(mCurrentTaskBean.data);
                    mAdapter.setDataList(mTaskList);
                    mAdapter.notifyDataSetChanged();
                }
            }
        } else {
            ToastUtil.showCenterTip(this, detectedBean.message);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mTaskList != null && mTaskList.size() > 0) {
            MyTaskBean taskBean = mTaskList.get(position);
            if (taskBean == null) {
                return;
            }
            String sheetType = taskBean.sheetType;
            String orderStatus = taskBean.orderStatus;
            if (sheetType.equalsIgnoreCase("pd")) {
                if (orderStatus.equalsIgnoreCase("001")) {
                    IntentUtils.startActivity(mContext, DispatchingTaskListActivity.class );
                } else if (orderStatus.equalsIgnoreCase("002")) {
                    IntentUtils.startActivity(mContext, MyReceivedTaskListActivity.class );
                }
            } else if (sheetType.equalsIgnoreCase("qd")) {
                if (orderStatus.equalsIgnoreCase("001")) {
                    IntentUtils.startActivity(mContext, QueryGrabSingleActivity.class);
                } else if (orderStatus.equalsIgnoreCase("002")) {
                    IntentUtils.startActivity(mContext, QueryProcessingGrabSingleActivity.class);
                }
            }
        }
    }
}
