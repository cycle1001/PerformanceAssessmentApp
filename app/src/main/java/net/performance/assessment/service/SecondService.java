package net.performance.assessment.service;

import net.performance.assessment.common.BaseApplication;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 *
 */
public class SecondService extends Service
{
    @Nullable
    @Override
    public IBinder onBind( Intent intent )
    {
        return null;
    }

    @Override
    public int onStartCommand( Intent intent, int flags, int startId )
    {
        startForeground( BaseApplication.KEEP_LIVE_SERVICE_ID , new Notification(  ) );
        stopForeground( true );
        stopSelf( );
        return super.onStartCommand( intent, flags, startId );
    }
}
