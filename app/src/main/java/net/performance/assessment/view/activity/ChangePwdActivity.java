package net.performance.assessment.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import net.performance.assessment.R;
import net.performance.assessment.cache.LoginInfoCache;
import net.performance.assessment.common.Config;
import net.performance.assessment.common.Constant;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.LoginSimpleInfo;
import net.performance.assessment.network.http.CommonAPI;
import net.performance.assessment.utils.AESUtils;
import net.performance.assessment.utils.IntentUtils;
import net.performance.assessment.utils.JsonParser;
import net.performance.assessment.utils.SharedPreferenceUtils;
import net.performance.assessment.utils.ToastUtil;

public class ChangePwdActivity extends BaseActivity {

    private EditText mEtOldPwd;
    private EditText mEtNewPwd;
    private EditText mEtConfirmPwd;

    private long mFlag;

    @Override
    protected void setContentView() {
        super.setContentView();
        setContentView(R.layout.activity_change_pwd);
    }

    @Override
    protected void initView() {
        super.initView();

        mEtOldPwd = findViewById(R.id.old_password_edit_view);
        mEtNewPwd = findViewById(R.id.new_password_edit_view);
        mEtConfirmPwd = findViewById(R.id.confirm_password_edit_view);
    }

    public void changePwd(View view) {
        String oldPwd = mEtOldPwd.getText().toString().trim();
        String newPwd = mEtNewPwd.getText().toString().trim();
        String confirmPwd = mEtConfirmPwd.getText().toString().trim();

        if (TextUtils.isEmpty(oldPwd)) {
            ToastUtil.showCenterTip(this, "请输入原始密码");
            return;
        }

        if (TextUtils.isEmpty(newPwd)) {
            ToastUtil.showCenterTip(this, "请输入新密码");
            return;
        }

        if (TextUtils.isEmpty(confirmPwd)) {
            ToastUtil.showCenterTip(this, "请输入确认密码");
            return;
        }

        if (!newPwd.equalsIgnoreCase(confirmPwd)) {
            ToastUtil.showCenterTip(this, "两次密码输入不同");
            mEtNewPwd.setText("");
            mEtConfirmPwd.setText("");
            mEtNewPwd.setFocusable(true);
            mEtNewPwd.requestFocus();
            mEtNewPwd.setFocusableInTouchMode(true);
            return;
        }

        showProgressDialog("");
        mFlag = CommonAPI.modifyPassword(oldPwd, newPwd, mHttpCallback);
    }

    @Override
    protected void handleResponseSuccess(String result, long flag) {
        super.handleResponseSuccess(result, flag);
        BaseResultBean detectedBean = JsonParser.getInstance().getBeanFromJsonString(result, BaseResultBean.class);
        if (detectedBean.isSuccess()) {
            if (mFlag == flag) {
                ToastUtil.showCenterTip(this, "修改成功！");
                handler.sendEmptyMessage(0);
            }
        } else {
            ToastUtil.showCenterTip(this, detectedBean.message);
        }
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Config.sToken = "";
            String loginInfoStr = SharedPreferenceUtils.getInstance(mContext).getString(Constant.LOGIN_INFO);
            if (!TextUtils.isEmpty(loginInfoStr)) {
                LoginSimpleInfo info = JsonParser.getInstance().getBeanFromJsonString(
                        loginInfoStr, LoginSimpleInfo.class);

                LoginSimpleInfo loginSimpleInfo = new LoginSimpleInfo.Builder().account(
                        info.account).password("").build();
                String loginInfoStr2 = JsonParser.getInstance().getJsonStringFromObject(loginSimpleInfo);

                SharedPreferenceUtils.getInstance(ChangePwdActivity.this).putString(Constant.LOGIN_INFO, loginInfoStr2);
            }
            IntentUtils.startActivity(ChangePwdActivity.this, LoginActivity.class);
            ChangePwdActivity.this.finish();
            return true;
        }
    });
}
