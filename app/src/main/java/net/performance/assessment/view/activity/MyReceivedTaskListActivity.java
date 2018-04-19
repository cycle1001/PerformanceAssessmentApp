package net.performance.assessment.view.activity;

import net.performance.assessment.R;
import net.performance.assessment.common.Constant;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.DispatchTaskInfo;
import net.performance.assessment.entity.DispatchingTaskListBean;
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
 *  我接到的任务单
 */

public class MyReceivedTaskListActivity extends BaseActivity
{
    private ListView mListView;
    private DispatchingTaskAdapter mAdapter;
    private List<DispatchTaskInfo > mTaskList;

    private long getTaskListFlag;

    @Override
    protected void setContentView( )
    {
        super.setContentView( );
        setContentView( R.layout.activity_dispatching_task_list );
    }

    @Override
    protected void initView( )
    {
        super.initView( );
        mListView = ViewUtils.xFindViewById( this , R.id.task_listview );
        mCustomTitleBar.setTitle( getString( R.string.label_my_received_task ) );

        mAdapter = new DispatchingTaskAdapter( );
        mListView.setAdapter( mAdapter );
        mListView.setOnItemClickListener( mOnItemClickListener );
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener( )
    {
        @Override
        public void onItemClick( AdapterView< ? > parent, View view, int position, long id )
        {
            DispatchTaskInfo info = mTaskList.get( position );
            if ( info != null )
            {
                if ( DispatchStatus.EXECUTING.equals( info.dispatchStatus ) )
                {
                    //跳到任务实施进度页面
                    IntentUtils.startActivity( mContext, UpdateDispatchedTaskProgressActivity.class,
                            Constant.DISPATCH_TASK_INFO, info );
                }
                else if ( DispatchStatus.FINISHED.equals( info.dispatchStatus ) )
                {
                    //跳到任务单绩效考核页面
                    IntentUtils.startActivity( mContext, DispatchPerformanceAssessmentActivity.class,
                            Constant.DISPATCH_TASK_INFO, info );
                }
            }
        }
    };

    @Override
    protected void initData( )
    {
        super.initData( );
        showProgressDialog( "" );
        getTaskListFlag = DispatchTaskAPI.queryDispatchingTask( "", "", "", mHttpCallback );
    }

    @Override
    protected void handleResponseSuccess( String result, long flag )
    {
        super.handleResponseSuccess( result, flag );
        BaseResultBean detectedBean = JsonParser.getInstance( ).getBeanFromJsonString( result , BaseResultBean.class );
        if ( detectedBean.isSuccess( ) )
        {
            if ( getTaskListFlag == flag )
            {
                DispatchingTaskListBean bean = JsonParser.getInstance( ).getBeanFromJsonString( result , DispatchingTaskListBean.class );
                if ( bean.data != null  && bean.data.size() > 0 )
                {
                    if ( mTaskList == null )
                    {
                        mTaskList = new ArrayList<>(  );
                        mAdapter.setDataList( mTaskList );
                    }

                    mTaskList.addAll( bean.data );
                    mAdapter.notifyDataSetChanged( );
                }
                else {
                    ToastUtil.showTip( mContext , getString( R.string.tips_empty ) );
                }
            }
        }
        else {
            ToastUtil.showErrorMessage( mContext , detectedBean.message , detectedBean.errorCode );
        }
    }
}
