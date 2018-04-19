package net.performance.assessment.view.fragment;

import net.performance.assessment.R;
import net.performance.assessment.common.Config;
import net.performance.assessment.entity.FunctionIconInfo;
import net.performance.assessment.entity.MenuItemInfo;
import net.performance.assessment.interfaces.LoadMenuCallback;
import net.performance.assessment.utils.CustomConfigManager;
import net.performance.assessment.utils.IntentUtils;
import net.performance.assessment.utils.LogUtils;
import net.performance.assessment.utils.StringUtil;
import net.performance.assessment.utils.ToastUtil;
import net.performance.assessment.utils.ViewUtils;
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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class TaskFragment extends BaseDelayLoadFragment {
    private GridView mAttendanceGridView;
    private FunctionIconAdapter mAttendanceAdapter;
    private List<FunctionIconInfo> mAttendanceList;

    private GridView mTaskGridView;
    private FunctionIconAdapter mTaskAdapter;
    private List<FunctionIconInfo> mTaskList;

    private GridView mPerformanceGridView;
    private FunctionIconAdapter mPerformanceAdapter;
    private List<FunctionIconInfo> mPerformanceList;

    private boolean hasLoadMenu = false;

    @Override
    protected void createView(LayoutInflater inflater, ViewGroup container) {
        super.createView(inflater, container);
        mRootView = inflater.inflate(R.layout.fragment_common_view_stub, null);
    }

    @Override
    protected void initView() {
        inflateViewStub(R.layout.fragment_task);
        CustomConfigManager.getInstance().setLoadMenuCallbacks( 0 , mLoadMenuCallback );
        super.initView();

        //initAttendanceGridView();
        //initTaskGridView();
        //initPerformanceGridView();
    }

    private void initAttendanceGridView() {
        mAttendanceGridView = ViewUtils.xFindViewById(
                mRootView, R.id.attendance_grid_view);
        String[] functionDescArray = getResources().getStringArray(
                R.array.attendance_function_arrays);
        int[] iconArray = {
                R.drawable.ic_field_punch, R.drawable.ic_make_up_field_punch
        };

        mAttendanceList = new ArrayList<>();
        for (int i = 0; i < iconArray.length; i++) {
            FunctionIconInfo info = new FunctionIconInfo();
            info.functionIconRes = iconArray[i];
            info.functionDesc = functionDescArray[i];
            mAttendanceList.add(info);
        }
        mAttendanceAdapter = new FunctionIconAdapter(mAttendanceList);
        mAttendanceGridView.setAdapter(mAttendanceAdapter);

        mAttendanceGridView.setOnItemClickListener(mAttendanceListener);
    }

    private AdapterView.OnItemClickListener mAttendanceListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            FunctionIconInfo info = mAttendanceList.get(position);
            Bundle bundle = new Bundle();
            if (info.functionId.equals(getString(R.string.id_fieldwork_punching))) {
                //外勤打卡
                bundle.putInt("sign_flag", 0);
            } else if (info.functionId.equals(getString(R.string.id_correct_fieldwork_punching))) {
                //补外勤打卡
                bundle.putInt("sign_flag", 1);
            }
            IntentUtils.startActivity(mContext, QueryPersonUnsignActivity.class, bundle);
        }
    };

    private void initTaskGridView() {
        mTaskGridView = ViewUtils.xFindViewById(
                mRootView, R.id.task_grid_view);
        String[] functionDescArray = getResources().getStringArray(
                R.array.task_function_arrays);
        int[] iconArray = {
                R.drawable.ic_posting_single, R.drawable.ic_grab_single,
                R.drawable.ic_grab_single_approval, R.drawable.ic_grab_assessment_performance,
                R.drawable.ic_send_single, R.drawable.ic_orders,
                R.drawable.ic_order_assessment_performance
        };

        mTaskList = new ArrayList<>();
        for (int i = 0; i < iconArray.length; i++) {
            FunctionIconInfo info = new FunctionIconInfo();
            info.functionIconRes = iconArray[i];
            info.functionDesc = functionDescArray[i];
            mTaskList.add(info);
        }
        mTaskAdapter = new FunctionIconAdapter(mTaskList);
        mTaskGridView.setAdapter(mTaskAdapter);

        mTaskGridView.setOnItemClickListener(mTaskListener);
    }

    private AdapterView.OnItemClickListener mTaskListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            FunctionIconInfo info = mTaskList.get(position);
            LogUtils.v("functionDesc===" + info.functionDesc);
            if ( StringUtil.isEmptyOrNullStr( Config.sToken ) )
            {
                //请先登录
                IntentUtils.startActivity( mContext , LoginActivity.class );
                return;
            }
            if (info.functionId.equals(getString(R.string.id_publish_task))) {
                //发布单
                IntentUtils.startActivity(mContext, ReleaseListActivity.class);
            } else if (info.functionId.equals(getString(R.string.id_grab_task))) {
                //抢单
                IntentUtils.startActivity(mContext, QueryReleaseListActivity.class);
            } else if (info.functionId.equals(getString(R.string.id_approve_for_grabbed_task))) {
                //抢单审批
                IntentUtils.startActivity(mContext, QueryGrabSingleActivity.class);
            }
            else if (info.functionId.equals(getString(R.string.id_grabbed_task_finished_progress))) {
                //抢单完成进度
                IntentUtils.startActivity(mContext, QueryProcessingGrabSingleActivity.class );
            }else if (info.functionId.equals(getString(R.string.id_assess_performance_for_grabbed_task))) {
                //抢单考核绩效
                IntentUtils.startActivity( mContext , QueryAssessedGrabSingleActivity.class );
            } else if (info.functionId.equals(getString(R.string.id_dispatch_task))) {
                //派单
                IntentUtils.startActivity(mContext, DispatchTaskActivity.class );
            } else if (info.functionId.equals(getString(R.string.id_receive_task))) {
                //接单
                IntentUtils.startActivity(mContext, DispatchingTaskListActivity.class );
                //IntentUtils.startActivity( mContext , ReceiveTaskActivity.class );
            } else if (info.functionId.equals(getString(R.string.id_dispatched_task_finished_progress))) {
                //派单完成进度
                //IntentUtils.startActivity(mContext, ReceiveTaskActivity.class);
                IntentUtils.startActivity(mContext, MyReceivedTaskListActivity.class );
            }else if (info.functionId.equals(getString(R.string.id_assess_performance_for_received_task))) {
                //派单考核绩效
                //IntentUtils.startActivity( mContext , DispatchPerformanceAssessmentActivity.class );
                IntentUtils.startActivity(mContext, MyReceivedTaskListActivity.class );
            }
        }
    };

    private void initPerformanceGridView() {
        mPerformanceGridView = ViewUtils.xFindViewById(
                mRootView, R.id.performance_grid_view);
        String[] functionDescArray = getResources().getStringArray(
                R.array.performance_function_arrays);
        int[] iconArray = {
                R.drawable.ic_staff_performance,
                /*R.drawable.ic_departmental_perfotrmance, */R.drawable.ic_performance_inquiries
        };

        mPerformanceList = new ArrayList<>();
        for (int i = 0; i < iconArray.length; i++) {
            FunctionIconInfo info = new FunctionIconInfo();
            info.functionIconRes = iconArray[i];
            info.functionDesc = functionDescArray[i];
            mPerformanceList.add(info);
        }
        mPerformanceAdapter = new FunctionIconAdapter(mPerformanceList);
        mPerformanceGridView.setAdapter(mPerformanceAdapter);

        mPerformanceGridView.setOnItemClickListener(mPerformanceListener);
    }

    private AdapterView.OnItemClickListener mPerformanceListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            FunctionIconInfo info = mPerformanceList.get(position);
            LogUtils.v("functionDesc===" + info.functionDesc);
            if (info.functionId.equals(getString(R.string.id_query_task_score))) {
                //任务积分查询
                //IntentUtils.startActivity( mContext , EmployeePerformanceAssessmentActivity.class );
                IntentUtils.startActivity(mContext, QueryTaskScoreActivity.class );
            } else if (info.functionId.equals(getString(R.string.label_departmental_performance))) {
                //部门绩效
                //IntentUtils.startActivity( mContext , DepartmentalPerformanceAssessmentActivity.class );
                IntentUtils.startActivity(mContext, QueryMonthPerformanceScoreActivity.class );
            } else if (info.functionId.equals(getString(R.string.id_query_month_performance_score))) {
                //月度积分查询
                //IntentUtils.startActivity( mContext , QueryPerformanceActivity.class );
                IntentUtils.startActivity(mContext, QueryMonthPerformanceScoreActivity.class );
            }
        }
    };

    @Override
    protected void loadData( )
    {
        super.loadData( );

        if ( CustomConfigManager.getInstance().isHasData( ) && !hasLoadMenu )
        {
            hasLoadMenu = true;
            fillList( );
            loadAttendanceGridView( );
            loadTaskGridView( );
            loadPerformanceGridView( );
        }
    }

    private LoadMenuCallback mLoadMenuCallback = new LoadMenuCallback( )
    {
        @Override
        public void onLoadMenuSuccess( )
        {
            LogUtils.v( "onLoadMenuSuccess" );
            fillList( );
            loadAttendanceGridView( );
            loadTaskGridView( );
            loadPerformanceGridView( );
        }

        @Override
        public void onLoadMenuFailed( )
        {
            LogUtils.v( "onLoadMenuFailed" );
            ToastUtil.showCenterTip( mContext , getString( R.string.toast_get_menu_failure ) );
        }
    };

    private void fillList( ){
        if ( mAttendanceList == null )
        {
            mAttendanceList = new ArrayList<>();
        }
        else {
            mAttendanceList.clear( );
        }
        if ( mTaskList == null )
        {
            mTaskList = new ArrayList<>();
        }
        else {
            mTaskList.clear( );
        }
        if ( mPerformanceList == null )
        {
            mPerformanceList = new ArrayList<>();
        }
        else {
            mPerformanceList.clear( );
        }

        List<MenuItemInfo> wholeList = CustomConfigManager.getInstance().getMenuItemInfos( );
        for ( MenuItemInfo menuInfo : wholeList )
        {
            String menuTypeStr = menuInfo.getMenuType( );
            String menuId = menuInfo.getId( );
            String menuDesc = menuInfo.getName( );

            FunctionIconInfo iconInfo = new FunctionIconInfo( );
            iconInfo.functionDesc = menuDesc;
            iconInfo.functionId = menuId;
            if ( "003".equals( menuTypeStr ) )
            {
                //积分查询
                if ( getString( R.string.id_query_task_score ).equals( menuId ) )
                {
                    //任务积分查询
                    iconInfo.functionIconRes = R.drawable.ic_staff_performance;
                }
                else if ( getString( R.string.id_query_month_performance_score ).equals( menuId ) )
                {
                    //月度积分查询
                    iconInfo.functionIconRes = R.drawable.ic_performance_inquiries;
                }
                mPerformanceList.add( iconInfo );
            }
            if ( "002".equals( menuTypeStr ) )
            {
                //工单管理
                if ( getString( R.string.id_publish_task ).equals( menuId ) )
                {
                    //发布工单
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
                    //抢单考核
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
                    //接单考核
                    iconInfo.functionIconRes = R.drawable.ic_order_assessment_performance;
                }
                else if ( getString( R.string.id_punch_approval ).equals( menuId ) )
                {
                    //补卡审批
                    iconInfo.functionIconRes = R.drawable.ic_punch_approval;
                }
                mTaskList.add( iconInfo );
            }
            if ( "001".equals( menuTypeStr ) )
            {
                //考勤管理
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
                mAttendanceList.add( iconInfo );
            }
        }
    }

    private void loadAttendanceGridView() {
        TextView tvTitle = ViewUtils.xFindViewById( mRootView , R.id.title_attendance_management );
        mAttendanceGridView = ViewUtils.xFindViewById(
                mRootView, R.id.attendance_grid_view);
        if ( mAttendanceList.size() > 0 )
        {
            tvTitle.setVisibility( View.VISIBLE );
            mAttendanceGridView.setVisibility( View.VISIBLE );
        }
        else {
            tvTitle.setVisibility( View.GONE );
            mAttendanceGridView.setVisibility( View.GONE );
        }

        mAttendanceAdapter = new FunctionIconAdapter(mAttendanceList);
        mAttendanceGridView.setAdapter(mAttendanceAdapter);

        mAttendanceGridView.setOnItemClickListener(mAttendanceListener);
    }

    private void loadTaskGridView( ) {
        TextView tvTitle = ViewUtils.xFindViewById( mRootView , R.id.title_task_management );
        mTaskGridView = ViewUtils.xFindViewById(
                mRootView, R.id.task_grid_view);
        if ( mTaskList.size( ) > 0 )
        {
            tvTitle.setVisibility( View.VISIBLE );
            mTaskGridView.setVisibility( View.VISIBLE );
            mTaskAdapter = new FunctionIconAdapter(mTaskList);
            mTaskGridView.setAdapter(mTaskAdapter);

            mTaskGridView.setOnItemClickListener(mTaskListener);
        }
        else {
            tvTitle.setVisibility( View.GONE );
            mTaskGridView.setVisibility( View.GONE );
        }
    }

    private void loadPerformanceGridView( ) {
        TextView tvTitle = ViewUtils.xFindViewById( mRootView , R.id.title_performance_management );
        mPerformanceGridView = ViewUtils.xFindViewById(
                mRootView, R.id.performance_grid_view);
        if ( mPerformanceList.size() > 0 )
        {
            tvTitle.setVisibility( View.VISIBLE );
            mPerformanceGridView.setVisibility( View.VISIBLE );
        }
        else {
            tvTitle.setVisibility( View.GONE );
            mPerformanceGridView.setVisibility( View.GONE );
        }

        mPerformanceAdapter = new FunctionIconAdapter(mPerformanceList);
        mPerformanceGridView.setAdapter(mPerformanceAdapter);

        mPerformanceGridView.setOnItemClickListener(mPerformanceListener);
    }
}
