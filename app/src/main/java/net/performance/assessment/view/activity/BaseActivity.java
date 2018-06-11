package net.performance.assessment.view.activity;

import net.performance.assessment.R;
import net.performance.assessment.common.BaseApplication;
import net.performance.assessment.common.CustomActivityManager;
import net.performance.assessment.network.http.SimpleHttpCallback;
import net.performance.assessment.utils.HttpUtils;
import net.performance.assessment.utils.LogUtils;
import net.performance.assessment.utils.StringUtil;
import net.performance.assessment.utils.ToastUtil;
import net.performance.assessment.utils.ViewUtils;
import net.performance.assessment.view.widget.CustomTitleBar;
import net.performance.assessment.view.widget.LoadingDialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 *
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected final String TAG = getClass().getName();

    protected Context mContext;

    protected CustomTitleBar mCustomTitleBar;

    protected LoadingDialog mLoadingDialog;

    private BaseApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        setTheme( );
        super.onCreate(savedInstanceState);
        if (application == null) {
            // 得到Application对象
            application = (BaseApplication) getApplication();
        }
        addActivity();// 调用添加方法

        mContext = this;
        CustomActivityManager.getInstance().addActivity(this);
        mHttpCallback.setRequestTag(TAG);
        //根据版本设置状态栏效果
        /*if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow( );
            window.setFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS );
        }*/
        setContentView();

        initView();
        initData();
        setListeners();
    }

    protected void setTheme( ){

    }

    protected void setContentView() {

    }

    protected void initView() {
        mCustomTitleBar = ViewUtils.xFindViewById(this, R.id.custom_top_bar);
    }

    private View.OnClickListener mBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    protected void initData() {

    }

    protected void setListeners() {
        if (mCustomTitleBar != null) {
            mCustomTitleBar.setLeftClickListener(mBackClickListener);
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        cancelHttpRequest();
        destroyProgressDialog();
        CustomActivityManager.getInstance().removeActivity(this);
        super.onDestroy();
    }

    protected void cancelHttpRequest() {
        mHttpCallback = null;
        HttpUtils.getInstance().cancelRequest(TAG);
    }

    @Override
    public void onClick(View v) {

    }

    protected void showProgressDialog(String loadingTips) {
        try {
            String loadingText = getString(R.string.label_default_loading);
            if (!StringUtil.isEmptyOrNullStr(loadingTips)) {
                loadingText = loadingTips;
            }
            if (mLoadingDialog == null) {
                mLoadingDialog = new LoadingDialog(mContext, loadingText);
            } else {
                mLoadingDialog.setLoadingTipContent(loadingText);
            }
            mLoadingDialog.show();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    protected void hideProgressDialog() {
        try {
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.hide();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    protected void destroyProgressDialog() {
        try {
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
            mLoadingDialog = null;
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * 做请求成功后的业务处理
     *
     * @param result
     * @param flag
     */
    protected void handleResponseSuccess(String result, long flag) {
        hideProgressDialog();
    }

    /**
     * 做请求失败后的业务处理
     *
     * @param error
     * @param flag
     */
    protected void handleResponseFailure(int error, long flag) {
        hideProgressDialog();
        ToastUtil.showTip(mContext, getString(R.string.toast_request_failure));
    }

    /**
     * 做请求超时后的业务处理
     *
     * @param flag
     */
    protected void handleResponseTimeout(long flag) {
        hideProgressDialog();
        ToastUtil.showTip(mContext, getString(R.string.toast_request_timeout));
    }

    protected SimpleHttpCallback mHttpCallback = new SimpleHttpCallback() {
        @Override
        public void onHttpResponseSuccess(String result, long flag) {
            LogUtils.v("onHttpResponseSuccess");
            handleResponseSuccess(result, flag);
        }

        @Override
        public void onHttpResponseFailure(int error, long flag) {
            LogUtils.v("onHttpResponseFailure");
            handleResponseFailure(error, flag);
        }

        @Override
        public void onHttpResponseTimeout(long flag) {
            LogUtils.v("onHttpResponseTimeout");
            handleResponseTimeout(flag);
        }
    };

    // 添加Activity方法
    public void addActivity() {
        application.addActivity(this);// 调用myApplication的添加Activity方法
    }
    //销毁当个Activity方法
    public void removeActivity() {
        application.removeActivity(this);// 调用myApplication的销毁单个Activity方法
    }
    //销毁所有Activity方法
    public void removeALLActivity() {
        application.removeALLActivity();// 调用myApplication的销毁所有Activity方法
    }
}
