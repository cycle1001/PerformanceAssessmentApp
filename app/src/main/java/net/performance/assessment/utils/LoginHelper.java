package net.performance.assessment.utils;

import net.performance.assessment.cache.LoginInfoCache;
import net.performance.assessment.common.Config;
import net.performance.assessment.common.Constant;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.LoginInfo;
import net.performance.assessment.entity.LoginResponseBean;
import net.performance.assessment.entity.LoginSimpleInfo;
import net.performance.assessment.network.http.CommonAPI;
import net.performance.assessment.network.http.SimpleHttpCallback;
import net.performance.assessment.view.activity.LoginActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */

public class LoginHelper
{

    private static final LoginHelper ourInstance = new LoginHelper( );
    private static final int MSG_LOGIN = 101;

    private long loginFlag;

    private String account;
    private String password;

    private Context mContext;

    private ExecutorService mExecutors;

    private Handler mHandler;

    public static LoginHelper getInstance( )
    {
        return ourInstance;
    }

    private LoginHelper( )
    {
    }

    public void autoLogin( Context context ){
        mContext = context;
        mHandler = new Handler( mCallback );
        //获取缓存的登录信息
        String loginDetailInfoStr = SharedPreferenceUtils.getInstance( context ).getString(
                Constant.LOGIN_DETAIL_INFO );
        LogUtils.v( "loginDetailInfoStr===" + loginDetailInfoStr );
        if ( !StringUtil.isEmptyOrNullStr( loginDetailInfoStr ) )
        {
            String loginInfoStr = SharedPreferenceUtils.getInstance(mContext).getString(
                    Constant.LOGIN_INFO);
            if ( !TextUtils.isEmpty(loginInfoStr )) {
                LoginSimpleInfo info = JsonParser.getInstance( ).getBeanFromJsonString(
                        loginInfoStr, LoginSimpleInfo.class );

                account = info.getAccount();
                password = AESUtils.decode(info.getPassword(), Constant.LOGIN_ENCODE_KEY);

                LoginInfo loginInfo = JsonParser.getInstance( ).getBeanFromJsonString(
                        loginInfoStr, LoginInfo.class );
                Config.sXinGeToken = loginInfo.xingeToken;

                loginFlag = CommonAPI.login(account, password, Config.sXinGeToken, mHttpCallback );
            }
            else {
                redirectTo( context );
            }
        }
        else
        {
            if ( StringUtil.isEmptyOrNullStr( Config.sToken ) )
            {
                redirectTo( context );
            }
        }
    }

    private void redirectTo( Context context )
    {
        Intent intent = new Intent( context, LoginActivity.class );
        context.startActivity( intent );
    }

    private Runnable mRunnable = new Runnable( )
    {
        @Override
        public void run( )
        {
            while( true ){
                    if ( !TextUtils.isEmpty( Config.sXinGeToken ) )
                    {
                        LogUtils.v( "sXinGeToken not Empty" );
                        mHandler.sendEmptyMessage( MSG_LOGIN );
                        break;
                    }
            }
        }
    };

    private void doLogin( ){
        if ( mExecutors == null )
        {
            mExecutors = Executors.newSingleThreadExecutor( );
        }
        mExecutors.execute( mRunnable );
    }

    protected SimpleHttpCallback mHttpCallback = new SimpleHttpCallback() {
        @Override
        public void onHttpResponseSuccess(String result, long flag) {
            LogUtils.v("onHttpResponseSuccess");
            BaseResultBean detectedBean = JsonParser.getInstance( ).getBeanFromJsonString(result, BaseResultBean.class );
            if (detectedBean.isSuccess()) {
                if (loginFlag == flag) {
                    LoginResponseBean bean = JsonParser.getInstance( ).getBeanFromJsonString(result, LoginResponseBean.class );
                    if (bean.data != null && bean.data.size() > 0) {
                        //保存登录信息
                        LoginInfo info = bean.data.get(0 );

                        Config.setToken(info.appToken);

                        LoginInfoCache.getInstance( ).save(info );

                        String loginDetailInfoStr = JsonParser.getInstance().getJsonStringFromObject(info);
                        SharedPreferenceUtils.getInstance( mContext ).putString( Constant.LOGIN_DETAIL_INFO , loginDetailInfoStr );
                        //获取菜单配置
                        CustomConfigManager.getInstance().getUserMenu( );
                    } /*else {
                        ToastUtil.showTip(mContext, R.string.toast_get_login_data_failure );
                    }*/
                }
            } else {
                if ( "110900200".equals( detectedBean.errorCode ) )
                {
                    CustomPushManager.getInstance( ).register( mContext );

                   doLogin( );
                }
            }
        }

        @Override
        public void onHttpResponseFailure(int error, long flag) {
            LogUtils.v("onHttpResponseFailure");

        }

        @Override
        public void onHttpResponseTimeout(long flag) {
            LogUtils.v("onHttpResponseTimeout");

        }
    };

    public void exit( ){
        mContext = null;
    }

    private Handler.Callback mCallback = new android.os.Handler.Callback( )
    {
        @Override
        public boolean handleMessage( Message message )
        {
            switch ( message.what )
            {
                case MSG_LOGIN:
                    loginFlag = CommonAPI.login(account, password, Config.sXinGeToken, mHttpCallback );
                    break;
            }
            return false;
        }
    };
}
