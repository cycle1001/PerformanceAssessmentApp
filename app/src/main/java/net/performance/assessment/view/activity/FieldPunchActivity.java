package net.performance.assessment.view.activity;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.performance.assessment.R;
import net.performance.assessment.cache.LoginInfoCache;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.LocationResultBean;
import net.performance.assessment.entity.LoginInfo;
import net.performance.assessment.entity.PersonUnSignBean;
import net.performance.assessment.interfaces.LocationCallBackListener;
import net.performance.assessment.listener.LocationResultListener;
import net.performance.assessment.network.http.AttendanceTaskAPI;
import net.performance.assessment.utils.JsonParser;
import net.performance.assessment.utils.TimeUtils;
import net.performance.assessment.utils.ToastUtil;
import net.performance.assessment.utils.ViewUtils;
import net.performance.assessment.utils.location.LocationUtils;
import net.performance.assessment.view.widget.LoadingDialog;

/**
 * 外勤打卡
 */
public class FieldPunchActivity extends BaseActivity implements LocationCallBackListener {

    private TextView mTvCheckLocation, mTvCheckInTime, mTvPunchPeople, mTvOrderNum;
    private TextView mTvAgainLocation;

    private String mFieldPunchTime;
    private int isLocationOver = 0;
    private PersonUnSignBean mPersonUnSign;

    private LocationResultListener locationResultListener;
    private long mFlag;

    @Override
    protected void setContentView() {
        super.setContentView();
        setContentView(R.layout.activity_field_punch);
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

        mTvOrderNum = findViewById(R.id.tv_order_num);
        mTvCheckLocation = ViewUtils.xFindViewById(this, R.id.tv_check_location);
        mTvCheckInTime = ViewUtils.xFindViewById(this, R.id.tv_check_int_time);
        mTvPunchPeople = ViewUtils.xFindViewById(this, R.id.tv_punch_people);
        mTvAgainLocation = findViewById(R.id.tv_again_location);
    }

    @Override
    protected void initData() {
        super.initData();

        mPersonUnSign = (PersonUnSignBean) getIntent().getSerializableExtra("person_unsign");

        if (mPersonUnSign != null) {
            mTvOrderNum.setText(mPersonUnSign.orderNum);
        }

        LoginInfo loginInfo = LoginInfoCache.getInstance().getLoginInfo();
        if (loginInfo != null) {
            mTvPunchPeople.setText(loginInfo.name);
        }
        mFieldPunchTime = TimeUtils.getNowTimeString();
        mTvCheckInTime.setText(mFieldPunchTime);
    }


    @Override
    public void onLocationResult(LocationResultBean location) {
        if (location == null) {
            isLocationOver = -1;
            mTvAgainLocation.setVisibility(View.VISIBLE);
            LocationUtils.stopLocation(locationResultListener);
            mTvCheckLocation.setText(R.string.tip_location_failed);
            mTvCheckLocation.setTextColor(Color.RED);
            return;
        }
        if (TextUtils.isEmpty(location.locationErrDescribe)) {
            isLocationOver = 200;
            mTvCheckLocation.setText(location.locationAddr);
            mTvCheckLocation.setTextColor(getResources().getColor(R.color.c_1e1e1e));
        } else {
            isLocationOver = -1;
            mTvAgainLocation.setVisibility(View.VISIBLE);
            mTvCheckLocation.setText(location.locationErrDescribe);
            mTvCheckLocation.setTextColor(Color.RED);
        }
    }

    public void againLocation(View view) {
        mTvCheckLocation.setTextColor(getResources().getColor(R.color.c_1e1e1e));
        mTvCheckLocation.setText(R.string.tip_location_loading);

        LocationUtils.stopLocation(locationResultListener);
        locationResultListener = null;
        locationResultListener = new LocationResultListener(this);
        LocationUtils.startLocation(this, locationResultListener);

        mTvAgainLocation.setVisibility(View.GONE);

    }

    public void submitFieldPunch(View view) {
        if (isLocationOver == 0) {
            ToastUtil.showCenterTip(this, "正在定位中，请稍后……");
            return;
        } else if (isLocationOver == -1) {
            ToastUtil.showCenterTip(this, "定位失败，请重试");
            return;
        }

        mLoadingDialog = new LoadingDialog(this);
        mLoadingDialog.show();
        mFlag = AttendanceTaskAPI.punchCardNormal(mPersonUnSign.orderNum, mFieldPunchTime,
                mTvCheckLocation.getText().toString().trim(), mHttpCallback);
    }

    @Override
    protected void handleResponseSuccess(String result, long flag) {
        super.handleResponseSuccess(result, flag);
        BaseResultBean detectedBean = JsonParser.getInstance( ).getBeanFromJsonString( result , BaseResultBean.class );
        if (detectedBean.isSuccess()) {
            if (mFlag == flag) {
                ToastUtil.showCenterTip(this, "打卡成功！");
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocationUtils.stopLocation(locationResultListener);
        locationResultListener = null;
    }
}
