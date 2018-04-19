package net.performance.assessment.view.activity;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import net.performance.assessment.R;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.DateInfo;
import net.performance.assessment.entity.GrabSingleBean;
import net.performance.assessment.entity.type.PictureFormat;
import net.performance.assessment.network.http.OrderSheetTaskAPI;
import net.performance.assessment.utils.Base64Utils;
import net.performance.assessment.utils.CommonUtils;
import net.performance.assessment.utils.FileUtils;
import net.performance.assessment.utils.JsonParser;
import net.performance.assessment.utils.TimeUtils;
import net.performance.assessment.utils.ToastUtil;
import net.performance.assessment.utils.ViewUtils;
import net.performance.assessment.view.widget.CustomDatePicker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 抢单实施进度填写
 */
public class GrabSingleProgressActivity extends BaseActivity {

    private TextView mTvGspPostSingleNum;
    private TextView mTvGspWorkItemType;
    private TextView mTvGspPublishContent;
    private TextView mTvGspReleaseTime;
    private TextView mTvGspPublisher;
    private TextView mTvGspTime;
    private TextView mTvGspPerson;
    private TextView mTvGspPosition;
    private TextView mTvGspParticipant;
    private EditText mEtGspCompletePercentage;
    private TextView mEtGspCompleteTime;
    private ImageView mIvGspFinishPic;

    private ArrayList<ImageItem> images = null;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    private CustomDatePicker mCompleteDatePicker;
    private DateInfo mCompleteTime;
    private GrabSingleBean mGrabSingleBean;

    private long mFlag;
    String receiveUnionPerson = "";
    private double progress = 0;

    @Override
    protected void setContentView() {
        super.setContentView();
        setContentView(R.layout.activity_grab_single_progress);
    }

    @Override
    protected void initView() {
        super.initView();

        mTvGspPostSingleNum = ViewUtils.xFindViewById(this, R.id.tv_gsp_post_single_num);
        mTvGspWorkItemType = ViewUtils.xFindViewById(this, R.id.tv_gsp_work_item_type);
        mTvGspPublishContent = ViewUtils.xFindViewById(this, R.id.tv_gsp_publish_content);
        mTvGspReleaseTime = ViewUtils.xFindViewById(this, R.id.tv_gsp_release_time);
        mTvGspPublisher = ViewUtils.xFindViewById(this, R.id.tv_gsp_publisher);
        mTvGspTime = ViewUtils.xFindViewById(this, R.id.tv_grab_single_time);
        mTvGspPerson = ViewUtils.xFindViewById(this, R.id.tv_grab_single_person);
        mTvGspPosition = ViewUtils.xFindViewById(this, R.id.tv_grab_single_position);
        mTvGspParticipant = ViewUtils.xFindViewById(this, R.id.et_gsp_participant);
        mEtGspCompletePercentage = ViewUtils.xFindViewById(this, R.id.et_gsp_complete_percentage);
        mEtGspCompleteTime = ViewUtils.xFindViewById(this, R.id.et_gsp_complete_time);
        mIvGspFinishPic = ViewUtils.xFindViewById(this, R.id.iv_gsp_finish_working_pic);
    }

    @Override
    protected void initData() {
        super.initData();
        mGrabSingleBean = (GrabSingleBean) getIntent().getSerializableExtra("grab_single");

        String oddNum = mGrabSingleBean.oddNum;
        mTvGspPostSingleNum.setText(oddNum);
        String workItemType = mGrabSingleBean.workItemTypeName;
        if (!TextUtils.isEmpty(workItemType)) {
            workItemType = workItemType.replaceAll("\\|", "-");
        }
        mTvGspWorkItemType.setText(workItemType);
        mTvGspPublishContent.setText(mGrabSingleBean.taskContent);
        String releaseTime = "";
        if (!TextUtils.isEmpty(oddNum) && oddNum.length() > 10) {
            long millisTime = Long.valueOf(oddNum.substring(oddNum.length() - 10, oddNum.length())) * 1000;
            releaseTime = TimeUtils.millis2String(millisTime);
        }
        mTvGspReleaseTime.setText(releaseTime);
        mTvGspPublisher.setText(mGrabSingleBean.releasePersonName);
        mTvGspTime.setText(mGrabSingleBean.receiveTime);
        mTvGspPerson.setText(mGrabSingleBean.receivePersonName);
        mTvGspPosition.setText(mGrabSingleBean.receiveLocation);
    }

    @Override
    protected void setListeners() {
        super.setListeners();
        mIvGspFinishPic.setOnClickListener(this);

        Button btnGspSubmit = ViewUtils.xFindViewById(this, R.id.btn_gsp_submit);
        btnGspSubmit.setOnClickListener(this);

        mEtGspCompleteTime.setOnClickListener(this);
        mTvGspParticipant.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_gsp_finish_working_pic:
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.et_gsp_complete_time:
                if (mCompleteDatePicker == null) {
                    mCompleteTime = CommonUtils.createCurrentDateInfo();
                    mCompleteDatePicker = new CustomDatePicker(this, mCompleteTime.year,
                            mCompleteTime.month, mCompleteTime.day, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCompleteTime = (DateInfo) v.getTag();
                            if (mCompleteTime != null) {
                                String timeStr = mCompleteTime.getTimeFormatStr(":");
                                String dateStr = mCompleteTime.getDateFormatStr("-");
                                mEtGspCompleteTime.setText(dateStr + " " + timeStr);
                            }
                        }
                    });
                }
                mCompleteDatePicker.show(mEtGspCompleteTime);
                break;
            case R.id.btn_gsp_submit:
                submitProgress();
                break;

            case R.id.et_gsp_participant:
                Intent intent1 = new Intent(this, ClassPersonSelectActivity.class);
                startActivityForResult(intent1, 100);
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
                    ImagePicker.getInstance().getImageLoader().displayImage((Activity) mContext, images.get(0).path, mIvGspFinishPic, 0, 0);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    ImagePicker.getInstance().getImageLoader().displayImage((Activity) mContext, images.get(0).path, mIvGspFinishPic, 0, 0);
                }
            }
        } else if (resultCode == RESULT_OK) {
            receiveUnionPerson = "";
            receiveUnionPerson = data.getStringExtra("select_person_ids");
            String selectNames = data.getStringExtra("select_person_names");
            mTvGspParticipant.setText(selectNames);
        }
    }

    private void submitProgress() {
        String finishPercent = mEtGspCompletePercentage.getText().toString().trim();
        if (TextUtils.isEmpty(finishPercent)) {
            ToastUtil.showCenterTip(this, "请填写完成进度（1-100）");
            return;
        }
        progress = Double.parseDouble(finishPercent);
        if (progress < 0 || progress > 100) {
            ToastUtil.showTip(mContext, getString(R.string.toast_percent_limitation));
            return;
        }

        String finishTime = mEtGspCompleteTime.getText().toString().trim();
        if (TextUtils.isEmpty(finishTime)) {
            ToastUtil.showCenterTip(this, "请选择完成时间");
            return;
        }
        if (images == null) {
            ToastUtil.showCenterTip(this, "请选择或拍摄工作图片");
            return;
        }
        ImageItem imageItem = images.get(0);
        byte[] temp = FileUtils.readFile(imageItem.path);
        String base64Str = Base64Utils.encode(temp);
        String pictureFormat = PictureFormat.getFormat(imageItem.mimeType);

        showProgressDialog("");
        mFlag = OrderSheetTaskAPI.submitOrderProgress(mGrabSingleBean.orderNum, receiveUnionPerson, finishPercent,
                finishTime, base64Str, pictureFormat, mHttpCallback);
    }

    @Override
    protected void handleResponseSuccess(String result, long flag) {
        super.handleResponseSuccess(result, flag);
        if (mFlag == flag) {
            BaseResultBean bean = JsonParser.getInstance().getBeanFromJsonString(result, BaseResultBean.class);
            if (bean.isSuccess()) {
                ToastUtil.showTip(mContext, getString(R.string.toast_submit_success));
                if (progress == 100) {
                    setResult(RESULT_OK);
                }
                finish();
            } else {
                ToastUtil.showErrorMessage(mContext, bean.message, bean.errorCode);
            }
        }
    }
}
