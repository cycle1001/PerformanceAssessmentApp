package net.performance.assessment.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import net.performance.assessment.R;
import net.performance.assessment.cache.LoginInfoCache;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.DateInfo;
import net.performance.assessment.entity.LoginInfo;
import net.performance.assessment.entity.PersonUnSignBean;
import net.performance.assessment.entity.type.PictureFormat;
import net.performance.assessment.network.http.AttendanceTaskAPI;
import net.performance.assessment.utils.Base64Utils;
import net.performance.assessment.utils.CommonUtils;
import net.performance.assessment.utils.FileUtils;
import net.performance.assessment.utils.JsonParser;
import net.performance.assessment.utils.TimeUtils;
import net.performance.assessment.utils.ToastUtil;
import net.performance.assessment.utils.ViewUtils;
import net.performance.assessment.view.widget.CustomDatePicker;
import net.performance.assessment.view.widget.LoadingDialog;

import java.util.ArrayList;

/**
 * 补卡申请
 */
public class MakeUpCardApplicationActivity extends BaseActivity {

    private TextView mTvOrderNum;
    private TextView mTvMakeUpTime;
    private TextView mTvCheckInTime;
    private TextView mTvPunchPeople;
    private EditText mEtMissingCardReason;
    private ImageView mIvImage;

    private ArrayList<ImageItem> images = null;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    private CustomDatePicker mCompleteDatePicker;
    private DateInfo mCompleteTime;

    private long mFlag;
    private PersonUnSignBean mPersonUnSign;

    @Override
    protected void setContentView() {
        super.setContentView();
        setContentView(R.layout.activity_make_up_card_application);
    }

    @Override
    protected void initView() {
        super.initView();

        mTvOrderNum = findViewById(R.id.tv_make_up_order_num);
        mTvMakeUpTime = ViewUtils.xFindViewById(this, R.id.tv_make_up_time);
        mTvCheckInTime = ViewUtils.xFindViewById(this, R.id.tv_m_check_in_time);
        mTvPunchPeople = ViewUtils.xFindViewById(this, R.id.tv_m_punch_people);
        mEtMissingCardReason = ViewUtils.xFindViewById(this, R.id.et_missing_card_reason);
        mIvImage = ViewUtils.xFindViewById(this, R.id.iv_m_image);

        TextView labelMissingCardReason = ViewUtils.xFindViewById(this, R.id.label_missing_card_reason);
        labelMissingCardReason.setText(Html.fromHtml(getString(R.string.missing_card_reason)));
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
        mTvCheckInTime.setText(TimeUtils.getNowTimeString());
    }

    @Override
    protected void setListeners() {
        super.setListeners();
        mIvImage.setOnClickListener(this);
        mTvMakeUpTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_m_image:
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, 100);
                break;

            case R.id.tv_make_up_time:
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
                                mTvMakeUpTime.setText(dateStr + " " + timeStr);
                            }
                        }
                    });
                }
                mCompleteDatePicker.show(mTvMakeUpTime);
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
                    ImagePicker.getInstance().getImageLoader().displayImage((Activity) mContext, images.get(0).path, mIvImage, 0, 0);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    ImagePicker.getInstance().getImageLoader().displayImage((Activity) mContext, images.get(0).path, mIvImage, 0, 0);
                }
            }
        }
    }

    public void submitMakeUpFieldPunch(View view) {
        String punchReasonStr = mEtMissingCardReason.getText().toString().trim();
        if (TextUtils.isEmpty(punchReasonStr)) {
            ToastUtil.showCenterTip(this, "请输入缺卡原因");
            return;
        }
        String extraPunchTime = mTvMakeUpTime.getText().toString().trim();
        if (TextUtils.isEmpty(extraPunchTime)) {
            ToastUtil.showCenterTip(this, "请选择补卡时间");
            return;
        }
        if (images == null) {
            ToastUtil.showCenterTip(this, "请选择或拍摄工作图片");
            return;
        }
        ImageItem imageItem = images.get( 0 );
        byte[ ] temp = FileUtils.readFile( imageItem.path );
        String base64Str = Base64Utils.encode( temp );
        String pictureFormat = PictureFormat.getFormat( imageItem.mimeType );

        mLoadingDialog = new LoadingDialog(this);
        mLoadingDialog.show();
        mFlag = AttendanceTaskAPI.punchCardSpecial(mTvOrderNum.getText().toString().trim(),
                extraPunchTime, punchReasonStr,
                base64Str, mTvCheckInTime.getText().toString().trim(),
                pictureFormat, mHttpCallback);
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
        ImagePicker.getInstance().clear();
    }
}
