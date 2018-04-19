package net.performance.assessment.view.activity;

import net.performance.assessment.utils.KeepLiveManager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 *
 */

public class KeepLiveActivity extends AppCompatActivity
{
    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );

        Window window = getWindow( );
        window.setGravity( Gravity.LEFT | Gravity.TOP );

        WindowManager.LayoutParams params = window.getAttributes( );
        params.x = 0;
        params.y = 0;
        params.width = 1;
        params.height = 1;
        window.setAttributes( params );

        KeepLiveManager.getInstance( ).setKeepLiveActivity( this );
    }
}
