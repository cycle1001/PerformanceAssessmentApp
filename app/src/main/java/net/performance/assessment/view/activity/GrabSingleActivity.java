package net.performance.assessment.view.activity;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import net.performance.assessment.R;
import net.performance.assessment.cache.LoginInfoCache;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.LocationResultBean;
import net.performance.assessment.entity.LoginInfo;
import net.performance.assessment.entity.ReleaseListBean;
import net.performance.assessment.interfaces.LocationCallBackListener;
import net.performance.assessment.listener.LocationResultListener;
import net.performance.assessment.network.http.OrderSheetTaskAPI;
import net.performance.assessment.utils.JsonParser;
import net.performance.assessment.utils.TimeUtils;
import net.performance.assessment.utils.ToastUtil;
import net.performance.assessment.utils.ViewUtils;
import net.performance.assessment.utils.location.LocationUtils;
import net.performance.assessment.view.widget.LoadingDialog;

/**
 * 抢单
 */
public class GrabSingleActivity extends BaseActivity implements LocationCallBackListener {

    private TextView mTvGsPostSingleNum;
    private TextView mTvGsWorkItemType;
    private TextView mTvGsPublishContent;
    private TextView mTvGsReleaseTime;
    private TextView mTvGsPublisher;
    private TextView mTvGsTime;
    private TextView mTvGsPerson;
    private TextView mTvGsPosition;
    private TextView mTvAgainLocation;

    private LocationResultListener locationResultListener;

    private LoginInfo mLoginInfo;
    private int isLocationOver = 0;
    private ReleaseListBean mReleaseBean;
    private long commintFlag;

    @Override
    protected void setContentView() {
        super.setContentView();
        setContentView(R.layout.activity_grab_single);

    }

    @Override
    protected void onResume() {
        super.onResume();
        locationResultListener = new LocationResultListener(this);
        LocationUtils.startLocation(this, locationResultListener);
    }

    @Override
    protected void initView() {
        super.initView();

        mTvGsPostSingleNum = ViewUtils.xFindViewById(this, R.id.tv_gs_post_single_num);
        mTvGsWorkItemType = ViewUtils.xFindViewById(this, R.id.tv_gs_work_item_type);
        mTvGsPublishContent = ViewUtils.xFindViewById(this, R.id.tv_gs_publish_content);
        mTvGsReleaseTime = ViewUtils.xFindViewById(this, R.id.tv_gs_release_time);
        mTvGsPublisher = ViewUtils.xFindViewById(this, R.id.tv_gs_publisher);
        mTvGsTime = ViewUtils.xFindViewById(this, R.id.tv_gs_grab_single_time);
        mTvGsPerson = ViewUtils.xFindViewById(this, R.id.tv_gs_grab_single_person);
        mTvGsPosition = ViewUtils.xFindViewById(this, R.id.tv_gs_grab_single_position);
        mTvAgainLocation = findViewById(R.id.tv_again_location);
    }

    @Override
    protected void initData() {
        super.initData();
        mReleaseBean = (ReleaseListBean) getIntent().getSerializableExtra("release_list");

        mTvGsTime.setText(TimeUtils.getNowTimeString());
        mLoginInfo = LoginInfoCache.getInstance().getLoginInfo();
        if (mLoginInfo != null) {
            mTvGsPerson.setText(mLoginInfo.name);
        }

        if (mReleaseBean != null) {
            mTvGsPostSingleNum.setText(mReleaseBean.oddNum);
            String workItemTypeName = mReleaseBean.workItemTypeName;
            if (!TextUtils.isEmpty(workItemTypeName)) {
                workItemTypeName = workItemTypeName.replaceAll("\\|", "-");
            }
            mTvGsWorkItemType.setText(workItemTypeName);
            mTvGsPublishContent.setText(mReleaseBean.taskContent);
            mTvGsReleaseTime.setText(mReleaseBean.taskReleaseTime);
            mTvGsPublisher.setText(mReleaseBean.oddUserName);
        }
    }

    @Override
    protected void setListeners() {
        super.setListeners();
    }

    public void commitOrder(View view) {
        if (isLocationOver == 0) {
            ToastUtil.showCenterTip(this, "正在定位中，请稍后……");
            return;
        } else if (isLocationOver == -1) {
            ToastUtil.showCenterTip(this, "定位失败，请重试");
            return;
        }
        mLoadingDialog = new LoadingDialog(this);
        mLoadingDialog.show();
        commintFlag = OrderSheetTaskAPI.orderSheetCommit(mReleaseBean.oddNum, mTvGsTime.getText().toString().trim(),
                mTvGsPosition.getText().toString().trim(), mHttpCallback);
    }

    public void againLocation(View view) {
        mTvGsPosition.setTextColor(getResources().getColor(R.color.c_1e1e1e));
        mTvGsPosition.setText(R.string.tip_location_loading);

        LocationUtils.stopLocation(locationResultListener);
        locationResultListener = null;
        locationResultListener = new LocationResultListener(this);
        LocationUtils.startLocation(this, locationResultListener);

        mTvAgainLocation.setVisibility(View.GONE);

    }

    @Override
    public void onLocationResult(LocationResultBean location) {
        if (location == null) {
            isLocationOver = -1;
            mTvAgainLocation.setVisibility(View.VISIBLE);
            LocationUtils.stopLocation(locationResultListener);
            mTvGsPosition.setText(R.string.tip_location_failed);
            mTvGsPosition.setTextColor(Color.RED);
            return;
        }
        if (TextUtils.isEmpty(location.locationErrDescribe)) {
            isLocationOver = 200;
            mTvGsPosition.setText(location.locationAddr);
            mTvGsPosition.setTextColor(getResources().getColor(R.color.c_1e1e1e));
        } else {
            isLocationOver = -1;
            mTvAgainLocation.setVisibility(View.VISIBLE);
            mTvGsPosition.setText(location.locationErrDescribe);
            mTvGsPosition.setTextColor(Color.RED);
        }
    }

    @Override
    protected void handleResponseSuccess(String result, long flag) {
        super.handleResponseSuccess(result, flag);
        BaseResultBean detectedBean = JsonParser.getInstance( ).getBeanFromJsonString( result , BaseResultBean.class );
        if (detectedBean.isSuccess()) {
            if (commintFlag == flag) {
                ToastUtil.showCenterTip(this, "抢单成功！");
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
        mReleaseBean = null;
        mLoginInfo = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocationUtils.stopLocation(locationResultListener);
        locationResultListener = null;
    }
}
