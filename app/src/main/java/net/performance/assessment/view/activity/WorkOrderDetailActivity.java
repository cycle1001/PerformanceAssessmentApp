package net.performance.assessment.view.activity;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.performance.assessment.R;
import net.performance.assessment.common.Constant;
import net.performance.assessment.entity.WorkOrderInfo;
import net.performance.assessment.network.image.GlideApp;
import net.performance.assessment.utils.ViewUtils;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 工单详情页面
 */

public class WorkOrderDetailActivity extends BaseActivity
{
    private TextView tvTaskId;
    private TextView tvProjectType;
    private TextView tvDispatcher;
    private TextView tvTaskContent;
    private TextView tvTaskStatus;
    private TextView tvDispatchTime;
    private TextView tvReceiver;
    private TextView tvReceivedTime;
    private TextView tvProgress;
    private TextView tvFinishedTime;
    private TextView tvStandardPoints;
    private ImageView ivTaskImage;
    private TextView tvQualityAssessment;
    private TextView tvAdditionalScore;
    private TextView tvFinalScore;
    private TextView tvOverview;

    private WorkOrderInfo mWorkOrderInfo;

    @Override
    protected void setContentView( )
    {
        super.setContentView( );
        setContentView( R.layout.activity_work_order_detail );
    }

    @Override
    protected void initView( )
    {
        super.initView( );
        tvTaskId = ViewUtils.xFindViewById(this, R.id.tv_task_id );
        tvProjectType = ViewUtils.xFindViewById(this, R.id.tv_project_type);
        tvDispatcher = ViewUtils.xFindViewById(this, R.id.tv_dispatcher);
        tvTaskContent = ViewUtils.xFindViewById(this, R.id.tv_task_content);
        tvDispatchTime = ViewUtils.xFindViewById(this, R.id.tv_dispatch_time);
        tvReceiver = ViewUtils.xFindViewById(this, R.id.tv_receiver);
        tvReceivedTime = ViewUtils.xFindViewById(this, R.id.tv_received_time);
        tvTaskStatus = ViewUtils.xFindViewById(this, R.id.tv_task_status);

        tvProgress = ViewUtils.xFindViewById(this, R.id.tv_progress);
        tvFinishedTime = ViewUtils.xFindViewById(this, R.id.tv_finished_time);
        tvStandardPoints = findViewById(R.id.tv_standard_points);
        ivTaskImage = ViewUtils.xFindViewById(this, R.id.iv_task_image);
        tvQualityAssessment = ViewUtils.xFindViewById(this, R.id.tv_quality_assessment);
        tvAdditionalScore = ViewUtils.xFindViewById(this, R.id.tv_additional_score);
        tvFinalScore = ViewUtils.xFindViewById(this, R.id.tv_final_score);
        tvOverview = ViewUtils.xFindViewById(this, R.id.tv_overview);
    }

    @Override
    protected void initData( )
    {
        super.initData( );

        Intent intent = getIntent( );
        mWorkOrderInfo = ( WorkOrderInfo ) intent.getSerializableExtra( Constant.WORK_ORDER_INFO );

        if (mWorkOrderInfo != null) {
            tvTaskId.setText(mWorkOrderInfo.dispatchNum);

            String projectTypeStr = mWorkOrderInfo.workItemTypeName.replace("|" , "-" );
            tvProjectType.setText(projectTypeStr);
            tvTaskContent.setText(mWorkOrderInfo.dispatchContent);
            tvDispatchTime.setText(mWorkOrderInfo.dispatchTime);
            tvDispatcher.setText(mWorkOrderInfo.dispatchPersonName);
            tvTaskStatus.setText(
                    mWorkOrderInfo.dispatchStatusName );

            tvReceiver.setText(mWorkOrderInfo.receivePersonName);

            tvReceivedTime.setText(mWorkOrderInfo.receiveTime);
            tvFinishedTime.setText(mWorkOrderInfo.finishTime);
            tvStandardPoints.setText(mWorkOrderInfo.integralStandard );
            tvProgress.setText( String.valueOf(mWorkOrderInfo.finishPercent) );

            tvQualityAssessment.setText( String.valueOf( mWorkOrderInfo.finishScore ) );
            tvFinalScore.setText( String.valueOf( mWorkOrderInfo.finalScore ) );
            tvAdditionalScore.setText( String.valueOf( mWorkOrderInfo.extraScore ) );
            tvOverview.setText( mWorkOrderInfo.overallMerit );

            if ( !TextUtils.isEmpty(mWorkOrderInfo.finishPictures )) {
                String picturePath = mWorkOrderInfo.finishPictures.replace("\\", "//");
//                GlideApp.with(this ).load(picturePath )
//                        .diskCacheStrategy( DiskCacheStrategy.RESOURCE )
//                        .error(R.drawable.ic_default_pic)
//                        .placeholder(R.drawable.ic_default_pic)
//                        .into(ivTaskImage);
                ImageLoader.getInstance().displayImage(picturePath, ivTaskImage, getImageOptions());
            }
            else {
                ViewUtils.xFindViewById(this, R.id.label_task_img).setVisibility( View.GONE );
                ivTaskImage.setVisibility( View.GONE );
            }
        }
    }
}
