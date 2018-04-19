package net.performance.assessment.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

/**
 *
 */

public class KeepLiveService extends Service
{
    private final static int KEEPLIVE_SERVICE_ID = 1001;

    @Override
    public IBinder onBind( Intent intent )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand( Intent intent, int flags, int startId )
    {
        if ( Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2 )
        {
            startForeground( KEEPLIVE_SERVICE_ID, new Notification( ) );
        }
        else
        {
            Intent secondIntent = new Intent( this, SecondService.class );
            startService( secondIntent );
            startForeground( KEEPLIVE_SERVICE_ID, new Notification( ) );
        }

        return super.onStartCommand( intent, flags, startId );
    }

}
