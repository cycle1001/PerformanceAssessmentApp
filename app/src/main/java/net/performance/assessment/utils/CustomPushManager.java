package net.performance.assessment.utils;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import net.performance.assessment.common.Config;

import android.content.Context;

/**
 *
 */

public class CustomPushManager
{
    private static final String TAG = "CustomPushManager";

    private static final CustomPushManager ourInstance = new CustomPushManager( );

    public static CustomPushManager getInstance( )
    {
        return ourInstance;
    }

    private CustomPushManager( )
    {
    }

    public void enableDebug( Context context , boolean enable ){
        XGPushConfig.enableDebug( context , enable );
    }

    public void register( Context context ){
        XGPushManager.registerPush( context, new XGIOperateCallback( )
        {
            @Override
            public void onSuccess( Object data, int flag )
            {
                //token在设备卸载重装的时候有可能会变
                LogUtils.v( TAG, "注册成功，设备token为：" + data );
                Config.sXinGeToken = String.valueOf( data );
            }

            @Override
            public void onFail( Object data, int errCode, String msg )
            {
                LogUtils.v( TAG, "注册失败，错误码：" + errCode + ",错误信息：" + msg );
            }
        } );
    }

    public void unregister( Context context ){
        XGPushManager.unregisterPush( context, new XGIOperateCallback( )
        {
            @Override
            public void onSuccess( Object data, int flag )
            {
                LogUtils.v( TAG, "data为：" + data );
            }

            @Override
            public void onFail( Object data, int errCode, String msg )
            {
                LogUtils.v( TAG, "注册失败，错误码：" + errCode + ",错误信息：" + msg );
            }
        } );
    }

    public void setTag( Context context , String tag ){
        XGPushManager.setTag( context , tag );
    }
}
