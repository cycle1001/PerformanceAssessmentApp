package net.performance.assessment.view.activity;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.performance.assessment.R;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.GrabSingleBean;
import net.performance.assessment.network.http.OrderSheetTaskAPI;
import net.performance.assessment.network.image.GlideApp;
import net.performance.assessment.utils.Arith;
import net.performance.assessment.utils.JsonParser;
import net.performance.assessment.utils.TimeUtils;
import net.performance.assessment.utils.ToastUtil;
import net.performance.assessment.utils.ViewUtils;
import net.performance.assessment.view.adapter.DifficultCoefficientAdapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 抢单绩效考核
 */
public class GrabSinglePerformanceAppraisalActivity extends BaseActivity {

    private TextView mTvGspaPostSingleNum;
    private TextView mTvGspaWorkItemType;
    private TextView mTvGspaPublishContent;
    private TextView mTvGspaReleaseTime;
    private TextView mTvGspaPublisher;
    private TextView mTvGspaTime;
    private TextView mTvGspaPerson;
    private TextView mTvGspaPosition;
    private TextView mTvGspaParticipant;
    private TextView mTvGspaCompletePercentage;
    private TextView mTvGspaCompleteTime;
    private ImageView mIvGspaFinishPic;
    private EditText mEtGspaQualityAssessment;
    private Spinner mEtGspaAdditionalScore;
    private TextView mTvGspaLastScore;
    private EditText mEtGspaOverview;
    private TextView mTvStandardPoints;

    private ArrayList<ImageItem> images = null;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    private GrabSingleBean mGrabSingleBean;
    private long mFlag;
    private String finishPicUrl;

    private String extraScore;
    private String finishScore;

    @Override
    protected void setContentView() {
        super.setContentView();
        setContentView(R.layout.activity_grab_single_performance_appraisal);
    }

    @Override
    protected void initView() {
        super.initView();

        mTvGspaPostSingleNum = ViewUtils.xFindViewById(this, R.id.tv_gspa_post_single_num);
        mTvGspaWorkItemType = ViewUtils.xFindViewById(this, R.id.tv_gspa_work_item_type);
        mTvGspaPublishContent = ViewUtils.xFindViewById(this, R.id.tv_gspa_publish_content);
        mTvGspaReleaseTime = ViewUtils.xFindViewById(this, R.id.tv_gspa_release_time);
        mTvGspaPublisher = ViewUtils.xFindViewById(this, R.id.tv_gspa_publisher);
        mTvGspaTime = ViewUtils.xFindViewById(this, R.id.tv_gspa_grab_single_time);
        mTvGspaPerson = ViewUtils.xFindViewById(this, R.id.tv_gspa_grab_single_person);
        mTvGspaPosition = ViewUtils.xFindViewById(this, R.id.tv_gspa_grab_single_position);
        mTvGspaParticipant = ViewUtils.xFindViewById(this, R.id.et_gspa_participant);
        mTvGspaCompletePercentage = ViewUtils.xFindViewById(this, R.id.et_gspa_complete_percentage);
        mTvGspaCompleteTime = ViewUtils.xFindViewById(this, R.id.et_gspa_complete_time);
        mIvGspaFinishPic = ViewUtils.xFindViewById(this, R.id.iv_gspa_finish_working_pic);
        mEtGspaQualityAssessment = ViewUtils.xFindViewById(this, R.id.et_gspa_quality_assessment);
        mEtGspaAdditionalScore = ViewUtils.xFindViewById(this, R.id.et_gspa_additional_score);
        mTvGspaLastScore = ViewUtils.xFindViewById(this, R.id.et_gspa_last_score);
        mEtGspaOverview = ViewUtils.xFindViewById(this, R.id.et_gspa_overview);
        mTvStandardPoints = findViewById(R.id.et_gspa_standard_points);
    }

    @Override
    protected void initData() {
        super.initData();
        mGrabSingleBean = (GrabSingleBean) getIntent().getSerializableExtra("grab_single");

        if (mGrabSingleBean != null) {
            String oddNum = mGrabSingleBean.oddNum;
            mTvGspaPostSingleNum.setText(oddNum);
            String workItemType = mGrabSingleBean.workItemTypeName;
            if (!TextUtils.isEmpty(workItemType)) {
                workItemType = workItemType.replaceAll("\\|", "-");
            }
            mTvGspaWorkItemType.setText(workItemType);
            mTvGspaPublishContent.setText(mGrabSingleBean.taskContent);
            String releaseTime = "";
            if (!TextUtils.isEmpty(oddNum) && oddNum.length() > 10) {
                long millisTime = Long.valueOf(oddNum.substring(oddNum.length() - 10, oddNum.length())) * 1000;
                releaseTime = TimeUtils.millis2String(millisTime);
            }
            mTvGspaReleaseTime.setText(releaseTime);
            mTvGspaPublisher.setText(mGrabSingleBean.releasePersonName);
            String unionPersonName = mGrabSingleBean.receiveUnionPersonName;
            if (TextUtils.isEmpty(unionPersonName)) {
                unionPersonName = "无";
            }
            mTvGspaParticipant.setText(unionPersonName);
            mTvGspaTime.setText(mGrabSingleBean.receiveTime);
            mTvGspaPerson.setText(mGrabSingleBean.receivePersonName);
            mTvGspaPosition.setText(mGrabSingleBean.receiveLocation);
            mTvGspaCompletePercentage.setText(mGrabSingleBean.finishPercent);
            mTvGspaCompleteTime.setText(mGrabSingleBean.finishTime);
            mTvStandardPoints.setText(mGrabSingleBean.scoreStandard);
            mTvGspaLastScore.setText("0");

            finishPicUrl = mGrabSingleBean.finishPictures;
            if (!TextUtils.isEmpty(finishPicUrl)) {
                finishPicUrl = finishPicUrl.replaceAll("\\\\", "///");
                Log.e("url", finishPicUrl);
//                GlideApp.with(this).load(finishPicUrl)
//                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                        .error(R.drawable.ic_default_pic)
//                        .placeholder(R.drawable.ic_default_pic)
//                        .into(mIvGspaFinishPic);
                ImageLoader.getInstance().displayImage(finishPicUrl, mIvGspaFinishPic, getImageOptions());
            }
        }
    }

    @Override
    protected void setListeners() {
        super.setListeners();
        Button btnGspaSubmit = ViewUtils.xFindViewById(this, R.id.btn_gspa_submit);
        btnGspaSubmit.setOnClickListener(this);

        mIvGspaFinishPic.setOnClickListener(this);

        String[] mStringArray=getResources().getStringArray(R.array.difficult_coefficient);
        //使用自定义的ArrayAdapter
        DifficultCoefficientAdapter mAdapter = new DifficultCoefficientAdapter(this,mStringArray);

        mEtGspaAdditionalScore.setAdapter(mAdapter);
        //监听Item选中事件
        mEtGspaAdditionalScore.setOnItemSelectedListener(new ItemSelectedListenerImpl());

        mEtGspaQualityAssessment.addTextChangedListener(new TextWatcher() {
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
                    ToastUtil.showCenterTip(GrabSinglePerformanceAppraisalActivity.this, "请输入质量考核计分,10分制");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void calculateScore() {
        if (TextUtils.isEmpty(extraScore)) {
            extraScore = "0.6";
        }

        String scoreStandard = mGrabSingleBean.scoreStandard;
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

            mTvGspaLastScore.setText(String.valueOf(df.format(mul02)));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_gspa_submit:
                submitAppraisal();
                break;

            case R.id.iv_gspa_finish_working_pic:
                if (!TextUtils.isEmpty(finishPicUrl)) {
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
                    intent.putExtra(ShowPicActivity.PHOTO_SOURCE_ID, finishPicUrl);
                    intent.putExtra(ShowPicActivity.PHOTO_SELECT_X_TAG, location[0]);
                    intent.putExtra(ShowPicActivity.PHOTO_SELECT_Y_TAG, location[1]);
                    intent.putExtra(ShowPicActivity.PHOTO_SELECT_W_TAG, width);
                    intent.putExtra(ShowPicActivity.PHOTO_SELECT_H_TAG, height);
                    this.startActivity(intent);
                    this.overridePendingTransition(0, 0);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    ImagePicker.getInstance().getImageLoader().displayImage((Activity) mContext, images.get(0).path, mIvGspaFinishPic, 0, 0);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    ImagePicker.getInstance().getImageLoader().displayImage((Activity) mContext, images.get(0).path, mIvGspaFinishPic, 0, 0);
                }
            }
        }
    }

    private void submitAppraisal() {
        finishScore = mEtGspaQualityAssessment.getText().toString().trim();
        if (TextUtils.isEmpty(finishScore)) {
            ToastUtil.showCenterTip(this, "请输入质量考核计分");
            return;
        }
        double score01 = Double.valueOf(finishScore);
        if (score01 < 0 || score01 > 10) {
            ToastUtil.showCenterTip(this, "请输入质量考核计分,10分制");
            return;
        }

        String finalScore = mTvGspaLastScore.getText().toString().trim();

        String overallMerit = mEtGspaOverview.getText().toString().trim();
        if (TextUtils.isEmpty(overallMerit)) {
            ToastUtil.showCenterTip(this, "请输入评价内容");
            return;
        }

        showProgressDialog("");
        mFlag = OrderSheetTaskAPI.orderAssessment(mGrabSingleBean.orderNum, finishScore, extraScore,
                finalScore, overallMerit, mHttpCallback);
    }

    @Override
    protected void handleResponseSuccess(String result, long flag) {
        super.handleResponseSuccess(result, flag);
        if (mFlag == flag) {
            BaseResultBean bean = JsonParser.getInstance().getBeanFromJsonString(result, BaseResultBean.class);
            if (bean.isSuccess()) {
                ToastUtil.showTip(mContext, getString(R.string.toast_submit_success));
                setResult(RESULT_OK);
                finish();
            } else {
                ToastUtil.showErrorMessage(mContext, bean.message, bean.errorCode);
            }
        }
    }
}
