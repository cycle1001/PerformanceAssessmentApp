package net.performance.assessment.view.activity;

import net.performance.assessment.R;
import net.performance.assessment.cache.LoginInfoCache;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.GrabSingleBean;
import net.performance.assessment.entity.LoginInfo;
import net.performance.assessment.network.http.OrderSheetTaskAPI;
import net.performance.assessment.utils.JsonParser;
import net.performance.assessment.utils.TimeUtils;
import net.performance.assessment.utils.ToastUtil;
import net.performance.assessment.utils.ViewUtils;
import net.performance.assessment.view.widget.LoadingDialog;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

/**
 * 抢单审批
 */
public class GrabSingleApprovalActivity extends BaseActivity {

    private TextView mTvGsaPostSingleNum;
    private TextView mTvGsaWorkItemType;
    private TextView mTvGsaPublishContent;
    private TextView mTvGsaReleaseTime;
    private TextView mTvGsaPublisher;
    private TextView mTvGsaStatus;
    private TextView mTvGsaGrabSingleTime;
    private TextView mTvGsaGrabSinlePerson;
    private TextView mTvGsaGrabSinglePosition;
    private TextView mTvGsaProcessingTime;
    private TextView mTvGsaApprover;

    private GrabSingleBean mGrabBean;
    private LoginInfo mLoginInfo;

    private long commintFlag;

    @Override
    protected void setContentView() {
        super.setContentView();
        setContentView(R.layout.activity_grab_single_approval);
    }

    @Override
    protected void initView() {
        super.initView();

        mTvGsaPostSingleNum = ViewUtils.xFindViewById(this, R.id.tv_gsa_post_single_num);
        mTvGsaWorkItemType = ViewUtils.xFindViewById(this, R.id.tv_gsa_work_item_type);
        mTvGsaPublishContent = ViewUtils.xFindViewById(this, R.id.tv_gsa_publish_content);
        mTvGsaReleaseTime = ViewUtils.xFindViewById(this, R.id.tv_gsa_release_time);
        mTvGsaPublisher = ViewUtils.xFindViewById(this, R.id.tv_gsa_publisher);
        mTvGsaStatus = ViewUtils.xFindViewById(this, R.id.tv_gsa_status);
        mTvGsaGrabSingleTime = ViewUtils.xFindViewById(this, R.id.tv_gsa_grab_single_time);
        mTvGsaGrabSinlePerson = ViewUtils.xFindViewById(this, R.id.tv_gsa_grab_single_person);
        mTvGsaGrabSinglePosition = ViewUtils.xFindViewById(this, R.id.tv_gsa_grab_single_position);
        mTvGsaProcessingTime = ViewUtils.xFindViewById(this, R.id.tv_processing_time);
        mTvGsaApprover = ViewUtils.xFindViewById(this, R.id.tv_gsa_approver);
    }

    @Override
    protected void initData() {
        super.initData();
        boolean hasLoginInfo = false;
        boolean hasGradInfo = false;

        mGrabBean = (GrabSingleBean) getIntent().getSerializableExtra("grab_single");

        mTvGsaProcessingTime.setText(TimeUtils.getNowTimeString());
        mLoginInfo = LoginInfoCache.getInstance().getLoginInfo();
        if (mLoginInfo != null) {
            mTvGsaApprover.setText(mLoginInfo.name);
            hasLoginInfo = true;
        }

        if (mGrabBean != null) {
            String oddNum = mGrabBean.oddNum;
            mTvGsaPostSingleNum.setText(oddNum);
            String workItemType = mGrabBean.workItemTypeName;
            if (!TextUtils.isEmpty(workItemType)) {
                workItemType = workItemType.replaceAll("\\|", "-");
            }
            mTvGsaWorkItemType.setText(workItemType);
            mTvGsaPublishContent.setText(mGrabBean.taskContent);
            String releaseTime = "";
            if (!TextUtils.isEmpty(oddNum) && oddNum.length() > 10) {
                long millisTime = Long.valueOf(oddNum.substring(oddNum.length() - 10, oddNum.length())) * 1000;
                releaseTime = TimeUtils.millis2String(millisTime);
            }
            mTvGsaReleaseTime.setText(releaseTime);
            mTvGsaPublisher.setText(mGrabBean.releasePersonName);
            mTvGsaStatus.setText( mGrabBean.orderStatusName );
            mTvGsaGrabSingleTime.setText(mGrabBean.receiveTime);
            mTvGsaGrabSinlePerson.setText(mGrabBean.receivePersonName);
            mTvGsaGrabSinglePosition.setText(mGrabBean.receiveLocation);

            hasGradInfo = true;
        }

        if ( hasLoginInfo && hasGradInfo )
        {
            if ( !mGrabBean.releasePersonId.equalsIgnoreCase( mLoginInfo.id ) )
            {
                findViewById( R.id. btn_operation_group ).setVisibility( View.GONE );
            }
        }
    }

    public void agreeCommit(View view) {
        approveOrderSheet("0");
    }

    public void disagreeCommit(View view) {
        approveOrderSheet("1");
    }

    private void approveOrderSheet(String auditOpinion) {
        mLoadingDialog = new LoadingDialog(this);
        mLoadingDialog.show();

        commintFlag = OrderSheetTaskAPI.approveOrderSheet(mGrabBean.orderNum, mTvGsaProcessingTime.getText().toString().trim(),
                auditOpinion, mHttpCallback);
    }

    @Override
    protected void handleResponseSuccess(String result, long flag) {
        super.handleResponseSuccess(result, flag);
        BaseResultBean detectedBean = JsonParser.getInstance( ).getBeanFromJsonString( result , BaseResultBean.class );
        if (detectedBean.isSuccess()) {
            if (commintFlag == flag) {
                ToastUtil.showCenterTip(this, "审批成功！");
                this.setResult(RESULT_OK);
                this.finish();
            }
        } else {
            ToastUtil.showCenterTip(this, detectedBean.message);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGrabBean = null;
    }
}
