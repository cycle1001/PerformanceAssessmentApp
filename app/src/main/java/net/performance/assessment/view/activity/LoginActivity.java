package net.performance.assessment.view.activity;

import net.performance.assessment.R;
import net.performance.assessment.cache.LoginInfoCache;
import net.performance.assessment.common.Config;
import net.performance.assessment.common.Constant;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.LoginInfo;
import net.performance.assessment.entity.LoginResponseBean;
import net.performance.assessment.entity.LoginSimpleInfo;
import net.performance.assessment.network.http.CommonAPI;
import net.performance.assessment.utils.AESUtils;
import net.performance.assessment.utils.CustomConfigManager;
import net.performance.assessment.utils.CustomPushManager;
import net.performance.assessment.utils.JsonParser;
import net.performance.assessment.utils.LogUtils;
import net.performance.assessment.utils.SharedPreferenceUtils;
import net.performance.assessment.utils.ToastUtil;
import net.performance.assessment.utils.ViewUtils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 登录页面
 */

public class LoginActivity extends BaseActivity {

    private EditText etAccount;
    private EditText etPassword;
    private TextView tvRememberPasswordOption;
    private Button btnLogin;

    private long loginFlag;

    private boolean isRememberPassword;

    @Override
    protected void setContentView() {
        super.setContentView();
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initView() {
        super.initView();
        mCustomTitleBar.setLeftBtnVisibility(View.GONE);
        etAccount = ViewUtils.xFindViewById(this, R.id.et_account);
        etPassword = ViewUtils.xFindViewById(this, R.id.et_password);
        tvRememberPasswordOption = ViewUtils.xFindViewById(this, R.id.remember_password_option);
        btnLogin = ViewUtils.xFindViewById(this, R.id.btn_login);
    }

    @Override
    protected void setListeners() {
        super.setListeners();
        btnLogin.setOnClickListener(this);
        tvRememberPasswordOption.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();

        isRememberPassword = SharedPreferenceUtils.getInstance(mContext).getBoolean(
                Constant.IS_REMEMBER_PASSWORD, false);

        changeRememberPasswordOption();

        if (isRememberPassword) {
            String loginInfoStr = SharedPreferenceUtils.getInstance(mContext).getString(
                    Constant.LOGIN_INFO);
            if (!TextUtils.isEmpty(loginInfoStr)) {
                LoginSimpleInfo info = JsonParser.getInstance().getBeanFromJsonString(
                        loginInfoStr, LoginSimpleInfo.class);
                etAccount.setText(info.getAccount());
                etPassword.setText(
                        AESUtils.decode(info.getPassword(), Constant.LOGIN_ENCODE_KEY));
            }
        }
    }

    private void changeRememberPasswordOption() {
        LogUtils.v("changeRememberPasswordOption---" + isRememberPassword);
        int leftDrawable = isRememberPassword ? R.mipmap.remember_password_selected : R.mipmap.remember_password_normal;
        tvRememberPasswordOption.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, 0, 0, 0);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnLogin)) {
            LogUtils.v("login");
            String account = ViewUtils.getEditViewContent(etAccount);
            String password = ViewUtils.getEditViewContent(etPassword);

            showProgressDialog("");
            loginFlag = CommonAPI.login(account, password, Config.sXinGeToken, mHttpCallback);
        } else if (v.equals(tvRememberPasswordOption)) {
            isRememberPassword = !isRememberPassword;
            changeRememberPasswordOption();
        }
    }

    @Override
    protected void handleResponseSuccess(String result, long flag) {
        super.handleResponseSuccess(result, flag);
        BaseResultBean detectedBean = JsonParser.getInstance().getBeanFromJsonString(result, BaseResultBean.class);
        if (detectedBean.isSuccess()) {
            if (loginFlag == flag) {
                LoginResponseBean bean = JsonParser.getInstance().getBeanFromJsonString(result, LoginResponseBean.class);
                if (bean.data != null && bean.data.size() > 0) {
                    //保存登录信息
                    LoginInfo info = bean.data.get(0);
                    Config.setToken(info.appToken);
                    LoginInfoCache.getInstance().save(info);

                    String loginDetailInfoStr = JsonParser.getInstance().getJsonStringFromObject(info);
                    SharedPreferenceUtils.getInstance( mContext ).putString( Constant.LOGIN_DETAIL_INFO , loginDetailInfoStr );
                    //获取菜单配置

                    CustomConfigManager.getInstance().getUserMenu( );
                    toMain();
                } else {
                    ToastUtil.showTip(mContext, R.string.toast_get_login_data_failure);
                }
            }
        } else {
            if ( "110900200".equals( detectedBean.errorCode ) )
            {
                Context context = getApplicationContext( );
                CustomPushManager.getInstance( ).register( context );
            }
            ToastUtil.showErrorMessage(mContext, detectedBean.message, detectedBean.errorCode);
        }
    }

    public void saveLoginInfo(Context context, String account, String password) {
        SharedPreferenceUtils.getInstance(context).putBoolean(Constant.IS_REMEMBER_PASSWORD,
                isRememberPassword);
        if (isRememberPassword) {
            if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password)) {
                String passwordEncodeStr = AESUtils.encode(password, Constant.LOGIN_ENCODE_KEY);

                LoginSimpleInfo loginSimpleInfo = new LoginSimpleInfo.Builder().account(
                        account).password(passwordEncodeStr).build();
                String loginInfoStr = JsonParser.getInstance().getJsonStringFromObject(loginSimpleInfo);

                SharedPreferenceUtils.getInstance(context).putString(Constant.LOGIN_INFO, loginInfoStr);
                LogUtils.v("saveLoginInfo======");
            }
        } else {
            SharedPreferenceUtils.getInstance(context).remove(Constant.LOGIN_INFO);
        }
    }

    @Override
    protected void onDestroy() {
        String accountStr = etAccount.getText().toString();
        String passwordStr = etPassword.getText().toString();
        saveLoginInfo(getApplicationContext(), accountStr, passwordStr);
        super.onDestroy();
    }

    private void toMain() {
        //startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }

    private boolean isQuit = false;

    @Override
    public void onBackPressed() {

        if (!isQuit) {
            ToastUtil.showTip(this, "再按一次退出程序");
            isQuit = true;

            //这段代码意思是,在两秒钟之后isQuit会变成false
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        isQuit = false;
                    }
                }
            }).start();


        } else {
            removeALLActivity();
            System.exit(0);
        }
    }
}
