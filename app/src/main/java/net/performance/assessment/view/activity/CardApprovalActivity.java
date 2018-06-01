package net.performance.assessment.view.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.performance.assessment.R;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.CardApprovalInfo;
import net.performance.assessment.network.http.AttendanceTaskAPI;
import net.performance.assessment.network.image.GlideApp;
import net.performance.assessment.utils.JsonParser;
import net.performance.assessment.utils.ToastUtil;

/**
 * 补卡审批
 */
public class CardApprovalActivity extends BaseActivity {

    private CardApprovalInfo mInfo;

    private TextView mTvPunchNum;
    private TextView mTvPunchName;
    private TextView mTvPunchTime;
    private TextView mTvPunchReason;
    private ImageView mIvPic;

    private String mPicUrl;

    private long mFlag;

    @Override
    protected void setContentView() {
        super.setContentView();
        setContentView(R.layout.activity_card_approved);
    }

    @Override
    protected void initView() {
        super.initView();

        mTvPunchNum = findViewById(R.id.tv_punch_num);
        mTvPunchName = findViewById(R.id.tv_punch_person);
        mTvPunchTime = findViewById(R.id.tv_punch_time);
        mTvPunchReason = findViewById(R.id.tv_punch_reason);
        mIvPic = findViewById(R.id.iv_working_pic);
    }

    @Override
    protected void setListeners() {
        super.setListeners();

        mIvPic.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mInfo = (CardApprovalInfo) getIntent().getSerializableExtra("card_approval");

        if (mInfo != null) {
            mTvPunchNum.setText(mInfo.punchNum);
            mTvPunchName.setText(mInfo.punchPersonName);
            mTvPunchTime.setText(mInfo.punchTime);
            mTvPunchReason.setText(mInfo.punchReason);

            mPicUrl = mInfo.workPicture;
            if (!TextUtils.isEmpty(mPicUrl)) {
                mPicUrl = mPicUrl.replaceAll("\\\\", "///");
                GlideApp.with(this).load(mPicUrl)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .error(R.drawable.ic_default_pic)
                        .placeholder(R.drawable.ic_default_pic)
                        .into(mIvPic);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_working_pic:
                if (!TextUtils.isEmpty(mPicUrl)) {
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
                    intent.putExtra(ShowPicActivity.PHOTO_SOURCE_ID, mPicUrl);
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

    public void agreeCommit(View view) {
        cardApproval("002");
    }

    public void disagreeCommit(View view) {
        cardApproval("003");
    }

    private void cardApproval(String auditStatus) {
        showProgressDialog("");

        mFlag = AttendanceTaskAPI.punchCardSpecialApprove(mInfo.punchNum, "1",
                auditStatus, mHttpCallback);
    }

    @Override
    protected void handleResponseSuccess(String result, long flag) {
        super.handleResponseSuccess(result, flag);
        BaseResultBean detectedBean = JsonParser.getInstance( ).getBeanFromJsonString( result , BaseResultBean.class );
        if (detectedBean.isSuccess()) {
            if (mFlag == flag) {
                ToastUtil.showCenterTip(this, "操作成功！");
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
}
