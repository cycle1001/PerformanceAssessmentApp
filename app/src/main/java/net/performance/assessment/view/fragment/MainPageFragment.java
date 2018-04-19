package net.performance.assessment.view.fragment;


import net.performance.assessment.R;
import net.performance.assessment.cache.CommonSwitchCache;
import net.performance.assessment.common.Config;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.FunctionIconInfo;
import net.performance.assessment.entity.MenuItemInfo;
import net.performance.assessment.entity.TaskSimpleInfo;
import net.performance.assessment.entity.TaskSimpleInfoListBean;
import net.performance.assessment.interfaces.LoadMenuCallback;
import net.performance.assessment.network.http.CommonAPI;
import net.performance.assessment.network.http.SimpleHttpCallback;
import net.performance.assessment.utils.CustomConfigManager;
import net.performance.assessment.utils.IntentUtils;
import net.performance.assessment.utils.JsonParser;
import net.performance.assessment.utils.LogUtils;
import net.performance.assessment.utils.StringUtil;
import net.performance.assessment.utils.ToastUtil;
import net.performance.assessment.utils.ViewUtils;
import net.performance.assessment.view.activity.CardApprovalListActivity;
import net.performance.assessment.view.activity.DispatchTaskActivity;
import net.performance.assessment.view.activity.DispatchingTaskListActivity;
import net.performance.assessment.view.activity.LoginActivity;
import net.performance.assessment.view.activity.MyReceivedTaskListActivity;
import net.performance.assessment.view.activity.QueryAssessedGrabSingleActivity;
import net.performance.assessment.view.activity.QueryGrabSingleActivity;
import net.performance.assessment.view.activity.QueryMonthPerformanceScoreActivity;
import net.performance.assessment.view.activity.QueryPersonUnsignActivity;
import net.performance.assessment.view.activity.QueryProcessingGrabSingleActivity;
import net.performance.assessment.view.activity.QueryReleaseListActivity;
import net.performance.assessment.view.activity.QueryTaskScoreActivity;
import net.performance.assessment.view.activity.ReleaseListActivity;
import net.performance.assessment.view.adapter.FunctionIconAdapter;
import net.performance.assessment.view.adapter.TaskSimpleInfoAdapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainPageFragment extends BaseDelayLoadFragment {

    private GridView mFunctionGridView;
    private FunctionIconAdapter mFunctionAdapter;
    private List<FunctionIconInfo> mFunctionList;

    private long getTaskFlag;

    private View pbPublishTask;
    private View pbDispatchTask;

    private ListView lvPublishTask;
    private ListView lvDispatchTask;

    private TextView tvPublishTips;
    private TextView tvDispatchTips;

    private List<TaskSimpleInfo> mPublishTaskList;
    private List<TaskSimpleInfo> mDispatchTaskList;

    private TaskSimpleInfoAdapter mPublishTaskAdapter;
    private TaskSimpleInfoAdapter mDispatchTaskAdapter;

    private boolean hasLoadMenu = false;

    @Override
    public void onAttach( Context context )
    {
        super.onAttach( context );
        CustomConfigManager.getInstance().setLoadMenuCallbacks( 1 , mLoadMenuCallback );
    }

    @Override
    protected void createView(LayoutInflater inflater, ViewGroup container) {
        super.createView(inflater, container);
        mRootView = inflater.inflate(R.layout.fragment_common_view_stub, null);
    }

    @Override
    protected void initView() {
        inflateViewStub(R.layout.fragment_main_page);
        //CustomConfigManager.getInstance().setLoadMenuCallbacks( 1 , mLoadMenuCallback );
        super.initView();

        //initFunctionGridView();

        pbPublishTask = ViewUtils.xFindViewById(mRootView, R.id.publish_progress_bar);
        pbDispatchTask = ViewUtils.xFindViewById(mRootView, R.id.dispatch_progress_bar);

        lvPublishTask = ViewUtils.xFindViewById(mRootView, R.id.publish_task_list_view);
        lvDispatchTask = ViewUtils.xFindViewById(mRootView, R.id.dispatch_task_list_view);

        tvPublishTips = ViewUtils.xFindViewById(mRootView, R.id.tv_publish_tips);
        tvDispatchTips = ViewUtils.xFindViewById(mRootView, R.id.tv_dispatch_tips);
    }

    private void initFunctionGridView() {
        mFunctionGridView = ViewUtils.xFindViewById(
                mRootView, R.id.function_grid_view);
        String[] functionDescArray = getResources().getStringArray(
                R.array.main_page_function_arrays);
        int[] iconArray = {
                R.drawable.ic_field_punch, R.drawable.ic_make_up_field_punch, R.drawable.ic_punch_approval, R.drawable.ic_posting_single, R.drawable.ic_grab_single,
                R.drawable.ic_grab_single_approval, R.drawable.ic_grab_completed_progress, R.drawable.ic_grab_assessment_performance, R.drawable.ic_send_single,
                R.drawable.ic_orders, R.drawable.ic_dispatch_complete_progress, R.drawable.ic_order_assessment_performance, R.drawable.ic_staff_performance,
                /*R.drawable.ic_departmental_perfotrmance,*/ R.drawable.ic_performance_inquiries
        };

        mFunctionList = new ArrayList<>();
        for (int i = 0; i < iconArray.length; i++) {
            FunctionIconInfo info = new FunctionIconInfo();
            info.functionIconRes = iconArray[i];
            info.functionDesc = functionDescArray[i];
            mFunctionList.add(info);
        }
        mFunctionAdapter = new FunctionIconAdapter(mFunctionList);
        mFunctionGridView.setAdapter(mFunctionAdapter);

        mFunctionGridView.setOnItemClickListener(mFunctionListener);
    }

    private AdapterView.OnItemClickListener mFunctionListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (StringUtil.isEmptyOrNullStr(Config.sToken)) {
                //请先登录
                IntentUtils.startActivity(mContext, LoginActivity.class);
                return;
            }
            FunctionIconInfo info = mFunctionList.get(position);
            Bundle bundle = new Bundle();
            String functionId = info.functionId;
            if (functionId.equals(getString(R.string.id_fieldwork_punching))) {
                //外勤打卡
                bundle.putInt("sign_flag", 0);
                IntentUtils.startActivity(mContext, QueryPersonUnsignActivity.class, bundle);
            } else if (functionId.equals(getString(R.string.id_correct_fieldwork_punching))) {
                //补外勤打卡
                bundle.putInt("sign_flag", 1);
                IntentUtils.startActivity(mContext, QueryPersonUnsignActivity.class, bundle);
            } else if (functionId.equals(getString(R.string.id_punch_approval))) {
                // 补卡审核
                IntentUtils.startActivity(mContext, CardApprovalListActivity.class);
            } else if (functionId.equals(getString(R.string.id_publish_task))) {
                //发布单
                IntentUtils.startActivity(mContext, ReleaseListActivity.class);
            } else if (functionId.equals(getString(R.string.id_grab_task))) {
                //抢单
                IntentUtils.startActivity(mContext, QueryReleaseListActivity.class);
            } else if (functionId.equals(getString(R.string.id_approve_for_grabbed_task))) {
                //抢单审批
                IntentUtils.startActivity(mContext, QueryGrabSingleActivity.class);
            } else if (functionId.equals(getString(R.string.id_grabbed_task_finished_progress))) {
                //抢单完成进度
                IntentUtils.startActivity(mContext, QueryProcessingGrabSingleActivity.class);
            } else if (functionId.equals(getString(R.string.id_assess_performance_for_grabbed_task))) {
                //抢单考核绩效
                IntentUtils.startActivity(mContext, QueryAssessedGrabSingleActivity.class);
            } else if (functionId.equals(getString(R.string.id_dispatch_task))) {
                //派单
                IntentUtils.startActivity(mContext, DispatchTaskActivity.class);
            } else if (functionId.equals(getString(R.string.id_receive_task))) {
                //接单
                IntentUtils.startActivity(mContext, DispatchingTaskListActivity.class );
            }else if (functionId.equals(getString(R.string.id_dispatched_task_finished_progress))) {
                //派单完成进度
                IntentUtils.startActivity(mContext, MyReceivedTaskListActivity.class );
            } else if (functionId.equals(getString(R.string.id_assess_performance_for_received_task))) {
                //派单考核绩效
                IntentUtils.startActivity(mContext, MyReceivedTaskListActivity.class );
            } else if (functionId.equals(getString(R.string.id_query_task_score))) {
                //任务积分查询
                IntentUtils.startActivity(mContext, QueryTaskScoreActivity.class);
            } else if (functionId.equals(getString(R.string.id_query_month_performance_score))) {
                //月度积分查询
                IntentUtils.startActivity(mContext, QueryMonthPerformanceScoreActivity.class );
            }
        }
    };

    @Override
    protected void loadData() {
        super.loadData();
        if (!StringUtil.isEmptyOrNullStr(Config.sToken) && ! CommonSwitchCache.hasGetData) {
            if ( mPublishTaskList != null )
            {
                mPublishTaskList.clear( );
            }
            if ( mDispatchTaskList != null )
            {
                mDispatchTaskList.clear( );
            }

            showLoadingDialog();
            getTaskFlag = CommonAPI.queryUnfinishedTask(mHttpCallback);
        }
        if ( CustomConfigManager.getInstance().isHasData( ) && !hasLoadMenu )
        {
            hasLoadMenu = true;
            fillList( );
            loadFunctionGridView( );
        }
    }

    private void showLoadingDialog() {
        pbPublishTask.setVisibility(View.VISIBLE);
        pbDispatchTask.setVisibility(View.VISIBLE);

        tvPublishTips.setVisibility(View.VISIBLE);
        tvDispatchTips.setVisibility(View.VISIBLE);
        tvPublishTips.setText(getString(R.string.tips_loading));
        tvDispatchTips.setText(getString(R.string.tips_loading));
    }

    private void hideLoadingDialog() {
        pbPublishTask.setVisibility(View.INVISIBLE);
        pbDispatchTask.setVisibility(View.INVISIBLE);
    }

    private void showEmptyTips() {
        tvPublishTips.setVisibility(View.VISIBLE);
        tvDispatchTips.setVisibility(View.VISIBLE);
        tvPublishTips.setText(getString(R.string.tips_empty));
        tvDispatchTips.setText(getString(R.string.tips_empty));

        if ( mDispatchTaskAdapter != null )
        {
            mDispatchTaskAdapter.notifyDataSetChanged();
        }
        if ( mPublishTaskAdapter != null )
        {
            mPublishTaskAdapter.notifyDataSetChanged();
        }
    }

    private void showFailureTips() {
        tvPublishTips.setVisibility(View.VISIBLE);
        tvDispatchTips.setVisibility(View.VISIBLE);
        tvPublishTips.setText(getString(R.string.tips_load_failure));
        tvDispatchTips.setText(getString(R.string.tips_load_failure));

        if ( mDispatchTaskAdapter != null )
        {
            mDispatchTaskAdapter.notifyDataSetChanged();
        }
        if ( mPublishTaskAdapter != null )
        {
            mPublishTaskAdapter.notifyDataSetChanged();
        }
    }

    protected SimpleHttpCallback mHttpCallback = new SimpleHttpCallback() {
        @Override
        public void onHttpResponseSuccess(String result, long flag) {
            LogUtils.v("onHttpResponseSuccess");
            hideLoadingDialog();
            CommonSwitchCache.hasGetData = true;
            BaseResultBean detectedBean = JsonParser.getInstance().getBeanFromJsonString(result, BaseResultBean.class);
            if (detectedBean.isSuccess()) {
                if (getTaskFlag == flag) {
                    TaskSimpleInfoListBean bean = JsonParser.getInstance().getBeanFromJsonString(result, TaskSimpleInfoListBean.class);
                    if (bean.data != null && bean.data.size() > 0) {
                        if ( mPublishTaskList == null )
                        {
                            mPublishTaskList = new ArrayList<>();
                        }
                        else {
                            mPublishTaskList.clear( );
                        }

                        if ( mDispatchTaskList == null )
                        {
                            mDispatchTaskList = new ArrayList<>();
                        }
                        else {
                            mDispatchTaskList.clear( );
                        }

                        for (TaskSimpleInfo info : bean.data) {
                            if ("QD".equalsIgnoreCase(info.sheetType)) {
                                mPublishTaskList.add(info);
                            }
                            if ("PD".equalsIgnoreCase(info.sheetType)) {
                                mDispatchTaskList.add(info);
                            }
                        }

                        if (mPublishTaskList.size() <= 0) {
                            tvPublishTips.setVisibility(View.VISIBLE);
                            tvPublishTips.setText(getString(R.string.tips_empty));
                        } else {
                            tvPublishTips.setVisibility(View.GONE);

                            mPublishTaskAdapter = new TaskSimpleInfoAdapter();
                            mPublishTaskAdapter.setDataList(mPublishTaskList);
                            lvPublishTask.setAdapter(mPublishTaskAdapter);
                            lvPublishTask.setOnItemClickListener(mPublishTaskClickListener);
                            mPublishTaskAdapter.notifyDataSetChanged();
                        }
                        if (mDispatchTaskList.size() <= 0) {
                            tvDispatchTips.setVisibility(View.VISIBLE);
                            tvDispatchTips.setText(getString(R.string.tips_empty));
                        } else {
                            tvDispatchTips.setVisibility(View.GONE);

                            mDispatchTaskAdapter = new TaskSimpleInfoAdapter();
                            mDispatchTaskAdapter.setDataList(mDispatchTaskList);
                            lvDispatchTask.setAdapter(mDispatchTaskAdapter);
                            lvDispatchTask.setOnItemClickListener(mDispatchTaskListener);
                            mDispatchTaskAdapter.notifyDataSetChanged();
                        }
                    } else {
                        showEmptyTips();
                    }
                }
            } else {
                showFailureTips();
                ToastUtil.showErrorMessage(mContext, detectedBean.message, detectedBean.errorCode);
            }
        }

        @Override
        public void onHttpResponseFailure(int error, long flag) {
            hideLoadingDialog();
            LogUtils.v("onHttpResponseFailure");
            showFailureTips();
        }

        @Override
        public void onHttpResponseTimeout(long flag) {
            LogUtils.v("onHttpResponseTimeout");
            hideLoadingDialog();
            showFailureTips();
        }
    };

    private AdapterView.OnItemClickListener mPublishTaskClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (mPublishTaskList != null) {
                TaskSimpleInfo info = mPublishTaskList.get(position);
                if (info != null) {
                    IntentUtils.startActivity(mContext, QueryReleaseListActivity.class);
                }
            }
        }
    };

    private AdapterView.OnItemClickListener mDispatchTaskListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (mDispatchTaskList != null) {
                TaskSimpleInfo info = mDispatchTaskList.get(position);
                if (info != null) {
                    IntentUtils.startActivity(mContext, DispatchingTaskListActivity.class );
                }
            }
        }
    };

    private void fillList( ){
        if ( mFunctionList == null )
        {
            mFunctionList = new ArrayList<>(  );
        }
        else
        {
            mFunctionList.clear( );
        }
        List<MenuItemInfo > wholeList = CustomConfigManager.getInstance( ).getMenuItemInfos( );
        for ( MenuItemInfo menuInfo : wholeList )
        {
            String menuId = menuInfo.getId( );
            String menuDesc = menuInfo.getName( );

            FunctionIconInfo iconInfo = new FunctionIconInfo( );
            iconInfo.functionDesc = menuDesc;
            iconInfo.functionId = menuId;
            if ( getString( R.string.id_fieldwork_punching ).equals( menuId ) )
            {
                //外勤打卡
                iconInfo.functionIconRes = R.drawable.ic_field_punch;
            }
            else if ( getString( R.string.id_correct_fieldwork_punching ).equals( menuId ) )
            {
                //补外勤打卡
                iconInfo.functionIconRes = R.drawable.ic_make_up_field_punch;
            }
            else if ( getString( R.string.id_punch_approval ).equals( menuId ) ) {
                // 补卡审核
                iconInfo.functionIconRes = R.drawable.ic_punch_approval;
            }
            else if ( getString( R.string.id_publish_task ).equals( menuId ) )
            {
                //发布单
                iconInfo.functionIconRes = R.drawable.ic_posting_single;
            }
            else if ( getString( R.string.id_grab_task ).equals( menuId ) )
            {
                //抢单
                iconInfo.functionIconRes = R.drawable.ic_grab_single;
            }
            else if ( getString( R.string.id_approve_for_grabbed_task ).equals( menuId ) )
            {
                //抢单审批
                iconInfo.functionIconRes = R.drawable.ic_grab_single_approval;
            }
            else if ( getString( R.string.id_grabbed_task_finished_progress ).equals( menuId ) )
            {
                //抢单完成进度
                iconInfo.functionIconRes = R.drawable.ic_grab_completed_progress;
            }
            else if ( getString( R.string.id_assess_performance_for_grabbed_task ).equals( menuId ) )
            {
                //抢单考核绩效
                iconInfo.functionIconRes = R.drawable.ic_grab_assessment_performance;
            }
            else if ( getString( R.string.id_dispatch_task ).equals( menuId ) )
            {
                //派单
                iconInfo.functionIconRes = R.drawable.ic_send_single;
            }
            else if ( getString( R.string.id_receive_task ).equals( menuId ) )
            {
                //接单
                iconInfo.functionIconRes = R.drawable.ic_orders;
            }
            else if ( getString( R.string.id_dispatched_task_finished_progress ).equals( menuId ) )
            {
                //派单完成进度
                iconInfo.functionIconRes = R.drawable.ic_dispatch_complete_progress;
            }
            else if ( getString( R.string.id_assess_performance_for_received_task ).equals( menuId ) )
            {
                //派单考核绩效
                iconInfo.functionIconRes = R.drawable.ic_order_assessment_performance;
            }
            else if ( getString( R.string.id_query_task_score ).equals( menuId ) )
            {
                //任务积分查询
                iconInfo.functionIconRes = R.drawable.ic_staff_performance;
            }
            else if ( getString( R.string.id_query_month_performance_score ).equals( menuId ) )
            {
                //月度积分查询
                iconInfo.functionIconRes = R.drawable.ic_performance_inquiries;
            }

            mFunctionList.add( iconInfo );
        }
    }

    private void loadFunctionGridView( ) {
        mFunctionGridView = ViewUtils.xFindViewById(
                mRootView, R.id.function_grid_view);
        mFunctionAdapter = new FunctionIconAdapter(mFunctionList);
        mFunctionGridView.setAdapter(mFunctionAdapter);

        mFunctionGridView.setOnItemClickListener(mFunctionListener);
    }

    private LoadMenuCallback mLoadMenuCallback = new LoadMenuCallback( )
    {
        @Override
        public void onLoadMenuSuccess( )
        {
            LogUtils.v( "onLoadMenuSuccess" );
            fillList( );
            loadFunctionGridView();
        }

        @Override
        public void onLoadMenuFailed( )
        {
            LogUtils.v( "onLoadMenuFailed" );
            ToastUtil.showCenterTip( mContext , getString( R.string.toast_get_menu_failure ) );
        }
    };
}
