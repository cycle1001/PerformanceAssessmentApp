package net.performance.assessment.view.activity;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.performance.assessment.R;
import net.performance.assessment.cache.LoginInfoCache;
import net.performance.assessment.common.Constant;
import net.performance.assessment.common.CustomActivityManager;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.DispatchTaskInfo;
import net.performance.assessment.entity.LoginInfo;
import net.performance.assessment.entity.type.DispatchStatus;
import net.performance.assessment.network.http.DispatchTaskAPI;
import net.performance.assessment.network.image.GlideApp;
import net.performance.assessment.utils.Arith;
import net.performance.assessment.utils.JsonParser;
import net.performance.assessment.utils.StringUtil;
import net.performance.assessment.utils.ToastUtil;
import net.performance.assessment.utils.ViewUtils;
import net.performance.assessment.view.adapter.DifficultCoefficientAdapter;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * 派单绩效考核页面
 */
public class DispatchPerformanceAssessmentActivity extends BaseActivity {
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
    private EditText tvQualityAssessment;
    private Spinner tvAdditionalScore;
    private TextView tvFinalScore;
    private TextView tvOverview;
    private Button btnSubmit;

    private long assessFlag;
    private DispatchTaskInfo mDispatchTaskInfo;

    private String extraScore = "0.6";
    private String finishScore;

    private String mPicPath;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_dispatch_performance_assessment);
    }

    @Override
    protected void initView() {
        super.initView();

        tvTaskId = ViewUtils.xFindViewById(this, R.id.tv_task_id);
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
        btnSubmit = ViewUtils.xFindViewById(this, R.id.btn_submit);
    }

    @Override
    protected void setListeners() {
        super.setListeners();
        btnSubmit.setOnClickListener(this);

        ivTaskImage.setOnClickListener(this);

        String[] mStringArray=getResources().getStringArray(R.array.difficult_coefficient);
        //使用自定义的ArrayAdapter
        DifficultCoefficientAdapter mAdapter = new DifficultCoefficientAdapter(this,mStringArray);

        tvAdditionalScore.setAdapter(mAdapter);
        //监听Item选中事件
        tvAdditionalScore.setOnItemSelectedListener(new ItemSelectedListenerImpl());

        tvQualityAssessment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                finishScore = s.toString();
                if (TextUtils.isEmpty(finishScore)) {
                    return;
                }
                double score01 = Double.valueOf(finishScore);
                if (score01 > 0 && score01 <= 10) {
                    calculateScore();
                    return;
                } else {
                    finishScore = "0.0";
                    ToastUtil.showCenterTip(DispatchPerformanceAssessmentActivity.this, "请输入质量考核计分,10分制");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private class ItemSelectedListenerImpl implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position,long arg3) {
            String[] d = getResources().getStringArray(R.array.difficult_coefficient);
            extraScore = d[position];

            calculateScore();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}

    }

    private void calculateScore() {
        if (TextUtils.isEmpty(extraScore)) {
            extraScore = "0.6";
        }

        String scoreStandard = mDispatchTaskInfo.integralStandard;
        if (TextUtils.isEmpty(scoreStandard)) {
            scoreStandard = "0.0";
        }

        if (TextUtils.isEmpty(finishScore)) {
            finishScore = "0.0";
        }

        try {
            double div = Arith.div(Double.valueOf(finishScore), 10.0, 1);
            double mul01 = Arith.mul(div, Double.valueOf(scoreStandard));
            double mul02 = Arith.mul(mul01, Double.valueOf(extraScore));

            DecimalFormat df = new DecimalFormat("######0.0");

            tvFinalScore.setText(String.valueOf(df.format(mul02)));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void initData() {
        super.initData();
        boolean hasLoginInfo = false;
        boolean hasDispatchInfo = false;

        LoginInfo loginInfo = LoginInfoCache.getInstance().getLoginInfo();
        if (loginInfo != null) {
            tvReceiver.setText(loginInfo.name);
            tvReceiver.setTag(loginInfo.id);
            hasLoginInfo = true;
        }

        Intent intent = getIntent();
        mDispatchTaskInfo = (DispatchTaskInfo) intent.getSerializableExtra(Constant.DISPATCH_TASK_INFO);

        if (mDispatchTaskInfo != null) {
            String[] projectTypeStr = mDispatchTaskInfo.workItemTypeName.split("\\|");
            int length = projectTypeStr.length;

            tvTaskId.setText(mDispatchTaskInfo.dispatchNum);
            tvProjectType.setText(length > 0 ? projectTypeStr[0] : "");
            tvTaskContent.setText(mDispatchTaskInfo.dispatchContent);
            tvDispatchTime.setText(mDispatchTaskInfo.dispatchTime);
            tvDispatcher.setText(mDispatchTaskInfo.dispatchPersonName);
            tvTaskStatus.setText(DispatchStatus.getDesc(mContext, mDispatchTaskInfo.dispatchStatus));

            tvReceiver.setText(mDispatchTaskInfo.receivePersonName);

            tvReceivedTime.setText(mDispatchTaskInfo.createDate);
            tvFinishedTime.setText(mDispatchTaskInfo.finishTime);
            tvStandardPoints.setText(mDispatchTaskInfo.integralStandard);
            tvProgress.setText(String.valueOf(mDispatchTaskInfo.finishPercent));

            mPicPath = mDispatchTaskInfo.finishPictures;
            if (!TextUtils.isEmpty(mPicPath)) {
                mPicPath = mPicPath.replace("\\\\", "//");
                GlideApp.with(this).load(mPicPath)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .error(R.drawable.ic_default_pic)
                        .placeholder(R.drawable.ic_default_pic)
                        .into(ivTaskImage);
            }

            hasDispatchInfo = true;
        }

        if ( hasLoginInfo && hasDispatchInfo )
        {
            if ( !mDispatchTaskInfo.dispatchPerson.equalsIgnoreCase( loginInfo.id ) )
            {
                findViewById( R.id. operation_group_view ).setVisibility( View.GONE );
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnSubmit)) {
            String taskIdStr = ViewUtils.getEditViewContent(tvTaskId);

            finishScore = ViewUtils.getEditViewContent(tvQualityAssessment);
            if (TextUtils.isEmpty(finishScore)) {
                ToastUtil.showCenterTip(this, "请输入质量考核计分");
                return;
            }
            double score01 = Double.valueOf(finishScore);
            if (score01 < 0 || score01 > 10) {
                ToastUtil.showCenterTip(this, "请输入质量考核计分,10分制");
                return;
            }

            String finalScoreStr = ViewUtils.getEditViewContent(tvFinalScore);

            String overviewStr = ViewUtils.getEditViewContent(tvOverview).trim();
            if (StringUtil.isEmptyOrNullStr(overviewStr)) {
                ToastUtil.showTip(mContext, getString(R.string.toast_input_overview));
                return;
            }

            String dispatchStatusStr = DispatchStatus.FINISHED;

            showProgressDialog("");
            assessFlag = DispatchTaskAPI.submitDispatchedTaskAssessment(taskIdStr, finishScore,
                    extraScore, finalScoreStr, overviewStr, dispatchStatusStr, mHttpCallback);
        } else if (v.equals(ivTaskImage)) {
            if (!TextUtils.isEmpty(mPicPath)) {
                int[] location = new int[2];
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Rect frame = new Rect();
                    this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                    int statusBarHeight = frame.top;
                    v.getLocationOnScreen(location);
                    location[1] += statusBarHeight;
                } else {
                    v.getLocationOnScreen(location);
                }
                v.invalidate();
                int width = v.getWidth();
                int height = v.getHeight();

                Intent intent = new Intent(this, ShowPicActivity.class);
                intent.putExtra(ShowPicActivity.PHOTO_SOURCE_ID, mPicPath);
                intent.putExtra(ShowPicActivity.PHOTO_SELECT_X_TAG, location[0]);
                intent.putExtra(ShowPicActivity.PHOTO_SELECT_Y_TAG, location[1]);
                intent.putExtra(ShowPicActivity.PHOTO_SELECT_W_TAG, width);
                intent.putExtra(ShowPicActivity.PHOTO_SELECT_H_TAG, height);
                this.startActivity(intent);
                this.overridePendingTransition(0, 0);
            }
        }
    }

    @Override
    protected void handleResponseSuccess(String result, long flag) {
        super.handleResponseSuccess(result, flag);
        if (assessFlag == flag) {
            BaseResultBean bean = JsonParser.getInstance().getBeanFromJsonString(result, BaseResultBean.class);
            if (bean.isSuccess()) {
                ToastUtil.showTip(mContext, getString(R.string.toast_submit_success));
                //finish( );
                CustomActivityManager.getInstance().backToMainActivity();
            } else {
                ToastUtil.showErrorMessage(mContext, bean.message, bean.errorCode);
            }
        }
    }
}
