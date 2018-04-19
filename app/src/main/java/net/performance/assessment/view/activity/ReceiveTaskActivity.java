package net.performance.assessment.view.activity;

import net.performance.assessment.R;
import net.performance.assessment.cache.LoginInfoCache;
import net.performance.assessment.common.Constant;
import net.performance.assessment.common.CustomActivityManager;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.DispatchTaskInfo;
import net.performance.assessment.entity.LoginInfo;
import net.performance.assessment.entity.type.DispatchStatus;
import net.performance.assessment.network.http.DispatchTaskAPI;
import net.performance.assessment.utils.JsonParser;
import net.performance.assessment.utils.TimeUtils;
import net.performance.assessment.utils.ToastUtil;
import net.performance.assessment.utils.ViewUtils;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 *  接单页面
 */
public class ReceiveTaskActivity extends BaseActivity
{
    private TextView tvTaskId;
    private TextView tvProjectType;
    private TextView tvScoreStandard;
    private TextView tvTaskContent;
    private TextView tvDispatchTime;
    private TextView tvDispatcher;

    private TextView tvTaskStatus;
    private TextView tvReceiver;
    private TextView tvReceivedTime;

    private Button btnSubmit;

    private long receiveFlag;

    private DispatchTaskInfo mDispatchTaskInfo;

    @Override
    protected void setContentView( )
    {
        setContentView( R.layout.activity_receive_task );
    }

    @Override
    protected void initView( )
    {
        super.initView( );

        tvTaskId = ViewUtils.xFindViewById( this , R.id.tv_task_id );
        tvProjectType = ViewUtils.xFindViewById( this , R.id.tv_project_type );
        tvTaskContent = ViewUtils.xFindViewById( this , R.id.tv_task_content );
        tvScoreStandard = ViewUtils.xFindViewById( this , R.id.tv_score_standard );
        tvDispatchTime = ViewUtils.xFindViewById( this , R.id.tv_dispatch_time );
        tvDispatcher = ViewUtils.xFindViewById( this , R.id.tv_dispatcher );

        tvTaskStatus = ViewUtils.xFindViewById( this , R.id.tv_task_status );
        tvReceivedTime = ViewUtils.xFindViewById( this , R.id.tv_received_time );
        tvReceiver = ViewUtils.xFindViewById( this , R.id.tv_receiver );

        btnSubmit = ViewUtils.xFindViewById( this , R.id.btn_submit );
    }

    @Override
    protected void initData( )
    {
        super.initData( );

        tvTaskStatus.setText( getString( R.string.label_status_dispatched ) );

        String currentTime = TimeUtils.getNowTimeString( );
        tvReceivedTime.setText( currentTime );

        LoginInfo loginInfo = LoginInfoCache.getInstance( ).getLoginInfo( );
        if ( loginInfo != null )
        {
            tvReceiver.setText( loginInfo.name );
            tvReceiver.setTag( loginInfo.id );
        }

        Intent intent = getIntent();
        mDispatchTaskInfo = ( DispatchTaskInfo ) intent.getSerializableExtra( Constant.DISPATCH_TASK_INFO );

        if ( mDispatchTaskInfo != null )
        {
            String[ ] projectTypeStr = mDispatchTaskInfo.workItemTypeName.split( "\\|" );
            int length = projectTypeStr.length;

            tvTaskId.setText( mDispatchTaskInfo.dispatchNum );
            tvProjectType.setText( length > 0 ? projectTypeStr[ 0 ] : "" );
            tvTaskContent.setText( mDispatchTaskInfo.dispatchContent );
            tvDispatchTime.setText( mDispatchTaskInfo.dispatchTime );
            tvDispatcher.setText( mDispatchTaskInfo.dispatchPersonName );
            tvTaskStatus.setText( DispatchStatus.getDesc( mContext , mDispatchTaskInfo.dispatchStatus ) );

            tvScoreStandard.setText( mDispatchTaskInfo.integralStandard );
        }
    }

    @Override
    protected void setListeners( )
    {
        super.setListeners( );
        btnSubmit.setOnClickListener( this );
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( btnSubmit ) )
        {
            String taskIdStr = ViewUtils.getEditViewContent( tvTaskId );
            String receiverStr = String.valueOf( tvReceiver.getTag( ) );
            String statusStr = DispatchStatus.DISPATCHED;

            showProgressDialog( "" );
            receiveFlag = DispatchTaskAPI.receiveTask( taskIdStr , receiverStr , statusStr , mHttpCallback );
        }
    }

    @Override
    protected void handleResponseSuccess( String result, long flag )
    {
        super.handleResponseSuccess( result, flag );
        BaseResultBean bean = JsonParser.getInstance( ).getBeanFromJsonString( result , BaseResultBean.class );
        if ( bean.isSuccess( ) )
        {
            if ( receiveFlag == flag )
            {
                ToastUtil.showTip( mContext  , getString( R.string.toast_submit_success ) );
                //finish( );
                CustomActivityManager.getInstance( ).backToMainActivity( );
            }
        }
        else {
            ToastUtil.showErrorMessage( mContext , bean.message , bean.errorCode );
        }
    }
}
