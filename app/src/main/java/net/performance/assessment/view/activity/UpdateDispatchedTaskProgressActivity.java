package net.performance.assessment.view.activity;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import net.performance.assessment.R;
import net.performance.assessment.common.Constant;
import net.performance.assessment.common.CustomActivityManager;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.DateInfo;
import net.performance.assessment.entity.DispatchTaskInfo;
import net.performance.assessment.entity.type.DispatchStatus;
import net.performance.assessment.entity.type.PictureFormat;
import net.performance.assessment.network.http.DispatchTaskAPI;
import net.performance.assessment.utils.Base64Utils;
import net.performance.assessment.utils.CommonUtils;
import net.performance.assessment.utils.FileUtils;
import net.performance.assessment.utils.JsonParser;
import net.performance.assessment.utils.LogUtils;
import net.performance.assessment.utils.StringUtil;
import net.performance.assessment.utils.ToastUtil;
import net.performance.assessment.utils.ViewUtils;
import net.performance.assessment.view.widget.CustomDatePicker;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 填写任务单实施进度
 */

public class UpdateDispatchedTaskProgressActivity extends BaseActivity
{
    private final static int REQ_SELECT_IMG = 100;
    private final static int REQ_PREVIEW_IMG = 101;

    private TextView tvTaskId;
    private TextView tvProjectType;
    private TextView tvDispatcher;
    private TextView tvTaskStatus;
    private TextView tvTaskContent;
    private TextView tvDispatchTime;
    private TextView tvReceiver;
    private TextView tvReceivedTime;
    private TextView tvProgress;
    private TextView tvFinishedTime;
    private ImageView ivTaskImage;
    private Button btnSubmit;

    private CustomDatePicker mFinishedDatePicker;
    private DateInfo mFinishedDate;

    private long updateProgressFlag;
    private ArrayList< ImageItem > images = null;

    private DispatchTaskInfo mDispatchTaskInfo;

    @Override
    protected void setContentView( )
    {
        super.setContentView( );
        setContentView( R.layout.activity_update_dispatched_task_progress );
    }

    @Override
    protected void initView( )
    {
        super.initView( );

        tvTaskId = ViewUtils.xFindViewById( this, R.id.tv_task_id );
        tvProjectType = ViewUtils.xFindViewById( this, R.id.tv_project_type );
        tvDispatcher = ViewUtils.xFindViewById( this, R.id.tv_dispatcher );
        tvTaskContent = ViewUtils.xFindViewById( this, R.id.tv_task_content );
        tvDispatchTime = ViewUtils.xFindViewById( this, R.id.tv_dispatch_time );
        tvReceiver = ViewUtils.xFindViewById( this, R.id.tv_receiver );

        tvTaskStatus = ViewUtils.xFindViewById( this , R.id.tv_task_status );
        tvReceivedTime = ViewUtils.xFindViewById( this, R.id.tv_received_time );
        tvProgress = ViewUtils.xFindViewById( this, R.id.tv_progress );
        tvFinishedTime = ViewUtils.xFindViewById( this, R.id.tv_finished_time );
        ivTaskImage = ViewUtils.xFindViewById( this, R.id.iv_task_image );
        btnSubmit = ViewUtils.xFindViewById( this, R.id.btn_submit );
    }

    @Override
    protected void initData( )
    {
        super.initData( );

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

            tvReceiver.setText( mDispatchTaskInfo.receivePersonName );

            tvReceivedTime.setText( mDispatchTaskInfo.createDate );

            tvFinishedTime.setHint( mDispatchTaskInfo.finishTime );

            if ( mDispatchTaskInfo.finishPercent != 0 )
            {
                tvProgress.setHint( String.valueOf( mDispatchTaskInfo.finishPercent ) );
            }
            else {
                tvProgress.setHint( getString( R.string.hint_input_task_progress ) );
            }

        }
    }

    @Override
    protected void setListeners( )
    {
        super.setListeners( );
        tvFinishedTime.setOnClickListener( this );
        ivTaskImage.setOnClickListener( this );
        btnSubmit.setOnClickListener( this );
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( btnSubmit ) )
        {
            String taskIdStr = ViewUtils.getEditViewContent( tvTaskId );

            String progressStr = ViewUtils.getEditViewContent( tvProgress );
            double progress;
            if ( StringUtil.isEmptyOrNullStr( progressStr ) )
            {
                ToastUtil.showTip( mContext, getString( R.string.hint_input_task_progress ) );
                return;
            }
            else
            {
                progress = Double.parseDouble( progressStr );

                LogUtils.v( "progress===" + progress );
                if ( progress < 0 || progress > 100 )
                {
                    ToastUtil.showTip( mContext, getString( R.string.toast_percent_limitation ) );
                    return;
                }
                progressStr = CommonUtils.trimDoubleToString( progress );
                progress = Double.parseDouble( progressStr );
            }

            String finishedTimeStr = ViewUtils.getEditViewContent( tvFinishedTime );
            if ( StringUtil.isEmptyOrNullStr( finishedTimeStr ) )
            {
                ToastUtil.showTip( mContext, getString( R.string.hint_input_finished_time ) );
                return;
            }

            String dispatchStatusStr = DispatchStatus.EXECUTING;
            String base64Str = "";
            String pictureFormat = "";
            if ( images != null && images.size( ) > 0 )
            {
                ImageItem imageItem = images.get( 0 );
//                byte[ ] temp = FileUtils.readFile( imageItem.path );
                base64Str = Base64Utils.encode( imageItem.path );
                pictureFormat = PictureFormat.getFormat( imageItem.mimeType );
            }
            else {
                ToastUtil.showTip( mContext , getString( R.string.toast_add_finished_task_image ) );
                return;
            }

            showProgressDialog( "" );
            updateProgressFlag = DispatchTaskAPI.submitDispatchedTaskProgress( taskIdStr,
                    String.valueOf( progress ), finishedTimeStr, base64Str, dispatchStatusStr,
                    pictureFormat, mHttpCallback );
        }
        else if ( v.equals( tvFinishedTime ) )
        {
            if ( mFinishedDatePicker == null )
            {
                mFinishedDate = CommonUtils.createCurrentDateInfo( );
                mFinishedDatePicker = new CustomDatePicker( this, mFinishedDate.year,
                        mFinishedDate.month, mFinishedDate.day, new View.OnClickListener( )
                {
                    @Override
                    public void onClick( View v )
                    {
                        mFinishedDate = ( DateInfo ) v.getTag( );
                        if ( mFinishedDate != null )
                        {
                            String timeStr = mFinishedDate.getTimeFormatStr( ":" );
                            String dateStr = mFinishedDate.getDateFormatStr( "-" );
                            tvFinishedTime.setText( dateStr + " " + timeStr );
                        }
                    }
                } );
            }
            mFinishedDatePicker.show( tvFinishedTime );
        }
        else if ( v.equals( ivTaskImage ) )
        {
            Intent intent = new Intent( this, ImageGridActivity.class );
            startActivityForResult( intent, REQ_SELECT_IMG );
        }
    }

    @Override
    protected void handleResponseSuccess( String result, long flag )
    {
        super.handleResponseSuccess( result, flag );
        if ( updateProgressFlag == flag )
        {
            BaseResultBean bean = JsonParser.getInstance( ).getBeanFromJsonString( result,
                    BaseResultBean.class );
            if ( bean.isSuccess( ) )
            {
                ToastUtil.showTip( mContext, getString( R.string.toast_submit_success ) );
                //finish( );
                CustomActivityManager.getInstance().backToMainActivity( );
            }
            else
            {
                ToastUtil.showErrorMessage( mContext, bean.message, bean.errorCode );
            }
        }
    }

    @Override
    public void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        super.onActivityResult( requestCode, resultCode, data );
        if ( resultCode == ImagePicker.RESULT_CODE_ITEMS )
        {
            //添加图片返回
            if ( data != null && requestCode == REQ_SELECT_IMG )
            {
                images = ( ArrayList< ImageItem > ) data.getSerializableExtra(
                        ImagePicker.EXTRA_RESULT_ITEMS );
                if ( images != null )
                {
                    ImagePicker.getInstance( ).getImageLoader( ).displayImage(
                            ( Activity ) mContext, images.get( 0 ).path, ivTaskImage, 0, 0 );
                }
            }
        }
        else if ( resultCode == ImagePicker.RESULT_CODE_BACK )
        {
            //预览图片返回
            if ( data != null && requestCode == REQ_PREVIEW_IMG )
            {
                images = ( ArrayList< ImageItem > ) data.getSerializableExtra(
                        ImagePicker.EXTRA_IMAGE_ITEMS );
                if ( images != null )
                {
                    ImagePicker.getInstance( ).getImageLoader( ).displayImage(
                            ( Activity ) mContext, images.get( 0 ).path, ivTaskImage, 0, 0 );
                }
            }
        }
    }
}
