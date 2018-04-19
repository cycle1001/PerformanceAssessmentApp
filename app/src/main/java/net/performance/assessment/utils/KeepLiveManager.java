package net.performance.assessment.utils;

import net.performance.assessment.view.activity.KeepLiveActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 *
 */

public class KeepLiveManager
{
    private static final KeepLiveManager ourInstance = new KeepLiveManager( );

    private Activity mKeepLiveActivity;

    public static KeepLiveManager getInstance( )
    {
        return ourInstance;
    }

    private KeepLiveManager( )
    {

    }

    public void startKeepLiveActivity( Context context )
    {
        Intent intent = new Intent( context, KeepLiveActivity.class );
        context.startActivity( intent );
    }

    public void setKeepLiveActivity( Activity activity )
    {
        mKeepLiveActivity = activity;
    }

    public void finishKeepLiveActivity( )
    {
        if ( mKeepLiveActivity != null )
        {
            mKeepLiveActivity.finish( );
        }
    }
}
