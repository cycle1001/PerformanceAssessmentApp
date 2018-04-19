package net.performance.assessment.receiver;

import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import net.performance.assessment.utils.LogUtils;

import android.content.Context;

/**
 *
 */

public class CustomMessageReceiver extends XGPushBaseReceiver
{
    @Override
    public void onRegisterResult( Context context, int i, XGPushRegisterResult xgPushRegisterResult )
    {

    }

    @Override
    public void onUnregisterResult( Context context, int i )
    {

    }

    @Override
    public void onSetTagResult( Context context, int i, String s )
    {

    }

    @Override
    public void onDeleteTagResult( Context context, int i, String s )
    {

    }

    @Override
    public void onTextMessage( Context context, XGPushTextMessage xgPushTextMessage )
    {

    }

    @Override
    public void onNotifactionClickedResult( Context context, XGPushClickedResult xgPushClickedResult )
    {
        LogUtils.v( "Message Click===" + xgPushClickedResult.toString( ) );
    }

    @Override
    public void onNotifactionShowedResult( Context context, XGPushShowedResult xgPushShowedResult )
    {
        if (context == null || xgPushShowedResult == null) {
            return;
        }

        LogUtils.v( "Message Show===" + xgPushShowedResult.toString( ) );
    }
}
