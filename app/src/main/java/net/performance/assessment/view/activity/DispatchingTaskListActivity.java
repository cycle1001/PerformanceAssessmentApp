package net.performance.assessment.view.activity;

import net.performance.assessment.R;
import net.performance.assessment.cache.LoginInfoCache;
import net.performance.assessment.common.Constant;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.DispatchTaskInfo;
import net.performance.assessment.entity.DispatchingTaskListBean;
import net.performance.assessment.entity.LoginInfo;
import net.performance.assessment.entity.type.DispatchStatus;
import net.performance.assessment.network.http.DispatchTaskAPI;
import net.performance.assessment.utils.IntentUtils;
import net.performance.assessment.utils.JsonParser;
import net.performance.assessment.utils.ToastUtil;
import net.performance.assessment.utils.ViewUtils;
import net.performance.assessment.view.adapter.DispatchingTaskAdapter;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 可接任务单列表页面
 */
public class DispatchingTaskListActivity extends BaseActivity {
    private ListView mListView;
    private DispatchingTaskAdapter mAdapter;
    private List<DispatchTaskInfo> mTaskList;

    private long getTaskListFlag;

    @Override
    protected void setContentView() {
        super.setContentView();
        setContentView(R.layout.activity_dispatching_task_list);
    }

    @Override
    protected void initView() {
        super.initView();
        mListView = ViewUtils.xFindViewById(this, R.id.task_listview);

        mAdapter = new DispatchingTaskAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DispatchTaskInfo info = mTaskList.get(position);
            if (DispatchStatus.DISPATCHED.equals(info.dispatchStatus)) {
                IntentUtils.startActivity(mContext, ReceiveTaskActivity.class,
                        Constant.DISPATCH_TASK_INFO, info);
            } else {
                ToastUtil.showTip(mContext, getString(R.string.toast_task_received_or_finished));
            }
        }
    };

    @Override
    protected void initData() {
        super.initData();
        LoginInfo loginInfo = LoginInfoCache.getInstance().getLoginInfo();
        if (loginInfo != null) {
            showProgressDialog("");
            getTaskListFlag = DispatchTaskAPI.queryDispatchingTask("", loginInfo.id, "",
                    mHttpCallback);
        }
    }

    @Override
    protected void handleResponseSuccess(String result, long flag) {
        super.handleResponseSuccess(result, flag);
        BaseResultBean detectedBean = JsonParser.getInstance().getBeanFromJsonString(result,
                BaseResultBean.class);
        if (detectedBean.isSuccess()) {
            if (getTaskListFlag == flag) {
                DispatchingTaskListBean bean = JsonParser.getInstance().getBeanFromJsonString(
                        result, DispatchingTaskListBean.class);
                if (bean.data != null && bean.data.size() > 0) {
                    if (mTaskList == null) {
                        mTaskList = new ArrayList<>();
                        mAdapter.setDataList(mTaskList);
                    }

                    mTaskList.addAll(bean.data);
                    mAdapter.notifyDataSetChanged();
                } else {

                }
            }
        } else {
            ToastUtil.showErrorMessage(mContext, detectedBean.message, detectedBean.errorCode);
        }
    }
}
