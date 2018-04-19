package net.performance.assessment.view.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.performance.assessment.R;
import net.performance.assessment.cache.LoginInfoCache;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.LoginInfo;
import net.performance.assessment.entity.TreeWorkingBean;
import net.performance.assessment.network.http.ReleaseListTaskAPI;
import net.performance.assessment.utils.JsonParser;
import net.performance.assessment.utils.LogUtils;
import net.performance.assessment.utils.TimeUtils;
import net.performance.assessment.utils.ToastUtil;
import net.performance.assessment.utils.ViewUtils;
import net.performance.assessment.view.widget.LoadingDialog;

/**
 * 发布单
 * @author _Smile
 */
public class ReleaseListActivity extends BaseActivity {

    private TextView mTvPostSingleNum;
    private TextView mTvReleaseTime;
    private TextView mTvPublisher;
    private TextView mTvSelectWorkItemType;
    private EditText mTvPublishContent;
    private EditText mEtIntegralStandard;
    private TextView mTvApplicableTeam;

    private String taskReleaseTime;
    private long submitFlag;

    private LoginInfo loginInfo;

    private String mSelectWorkItemType = "";
    private String mGroupIds;

    @Override
    protected void setContentView() {
        super.setContentView();
        setContentView(R.layout.activity_release_list);
    }

    @Override
    protected void initView() {
        super.initView();

        mTvPostSingleNum = ViewUtils.xFindViewById(this, R.id.tv_post_single_num);
        mTvReleaseTime = ViewUtils.xFindViewById(this, R.id.tv_release_time);
        mTvPublisher = ViewUtils.xFindViewById(this, R.id.tv_publisher);
        mTvSelectWorkItemType = ViewUtils.xFindViewById(this, R.id.tv_work_item_type);
        mTvPublishContent = ViewUtils.xFindViewById(this, R.id.tv_publish_content);
        mEtIntegralStandard = ViewUtils.xFindViewById(this, R.id.et_integral_standard);
        mTvApplicableTeam = ViewUtils.xFindViewById(this, R.id.tv_applicable_team);
    }

    @Override
    protected void initData() {
        super.initData();

        mTvReleaseTime.setText(TimeUtils.getNowTimeString());
        mTvPostSingleNum.setText(generateReleaseNum());

        if (loginInfo != null) {
            mTvPublisher.setText(loginInfo.name);
        }
    }

    /**
     * 生成发布单号
     * @return 单号
     */
    private String generateReleaseNum() {
        taskReleaseTime = ViewUtils.getEditViewContent(mTvReleaseTime);
        String numMillis = String.valueOf(TimeUtils.string2Millis(taskReleaseTime) / 1000);
        loginInfo = LoginInfoCache.getInstance().getLoginInfo();
        String userCode = "";
        if (loginInfo != null) {
            userCode = loginInfo.seriesno;
        }
        return "F" + userCode + numMillis;
    }

    public void btnReleaseList(View view) {
        String oddNum = ViewUtils.getEditViewContent(mTvPostSingleNum);
        String workItemType = ViewUtils.getEditViewContent(mTvSelectWorkItemType);
        if (TextUtils.isEmpty(workItemType)) {
            ToastUtil.showCenterTip(this, R.string.tip_select_work_item);
            return;
        }
        String taskContent = ViewUtils.getEditViewContent(mTvPublishContent);
        if (TextUtils.isEmpty(taskContent)) {
            ToastUtil.showCenterTip(this, R.string.tip_input_work_content);
            return;
        }
        String integralStandard = ViewUtils.getEditViewContent(mEtIntegralStandard);
        if (TextUtils.isEmpty(integralStandard)) {
            ToastUtil.showCenterTip(this, R.string.tip_input_score);
            return;
        }

        mLoadingDialog = new LoadingDialog(this);
        mLoadingDialog.show();
        submitFlag = ReleaseListTaskAPI.submitReleaseList(oddNum, mSelectWorkItemType, taskContent, taskReleaseTime, mGroupIds, integralStandard, mHttpCallback);
    }

    public void selectWorkItemType(View view) {
        startActivityForResult(new Intent(this, SelectWorkItemTypeActivity.class), 100);
    }

    @Override
    protected void handleResponseSuccess(String result, long flag) {
        super.handleResponseSuccess(result, flag);
        BaseResultBean bean = JsonParser.getInstance().getBeanFromJsonString(result, BaseResultBean.class);
        if (bean.isSuccess()) {
            if (submitFlag == flag) {
                ToastUtil.showTip(mContext, "提交成功");
                finish();
            }
        } else {
            ToastUtil.showErrorMessage(mContext, bean.message, bean.errorCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            TreeWorkingBean workingBean = (TreeWorkingBean) data.getSerializableExtra("select_item");
            loadWorkItemType(workingBean);
        }
    }

    private void loadWorkItemType(TreeWorkingBean workingBean) {
        if (workingBean != null) {
            StringBuffer itemType = new StringBuffer();
            itemType.append(workingBean.firstName);
            itemType.append("-");
            itemType.append(workingBean.secondName);
            itemType.append("-");
            itemType.append(workingBean.thirdName);
            mTvSelectWorkItemType.setText(itemType.toString());
            mSelectWorkItemType = workingBean.firstId + "|" + workingBean.secondId + "|" + workingBean.thirdId;

            mTvPublishContent.setText(workingBean.name);
            mEtIntegralStandard.setText(workingBean.standardScore);
            mTvApplicableTeam.setText(workingBean.groups);
            mGroupIds = workingBean.groupIds;
        }
    }
}
