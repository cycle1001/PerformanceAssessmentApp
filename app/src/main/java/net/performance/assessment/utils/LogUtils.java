package net.performance.assessment.utils;


import net.performance.assessment.common.Config;

import android.util.Log;


/**
 * @Description: Log统一管理类
 */
public class LogUtils
{

    private LogUtils( ) {
		/* cannot be instantiated */
		throw new UnsupportedOperationException( "cannot be instantiated" );
    }

    private static final String TAG = "performance";

    // 下面四个是默认tag的函数
    public static void i( String msg ) {
		if ( Config.isDebug ) {
			Log.i( TAG, msg );
		}
    }

    public static void d( String msg ) {
		if ( Config.isDebug ) {
			Log.d( TAG, msg );
		}

    }

    public static void e( String msg ) {
		if ( Config.isDebug ) {
			Log.e( TAG, msg );
		}
    }

    public static void v( String msg ) {
		if ( Config.isDebug ) {
			Log.v( TAG, msg );
		}
    }

    public static void w( String msg ) {
		if ( Config.isDebug ) {
			Log.w( TAG, msg );
		}
    }

    // 下面是传入自定义tag的函数
    public static void i( String tag, String msg ) {
		if ( Config.isDebug ) {
			Log.i( tag, msg );
		}
    }

    public static void d( String tag, String msg ) {
		if ( Config.isDebug ) {
			Log.d( tag, msg );
		}
    }

    public static void e( String tag, String msg ) {
		if ( Config.isDebug ) {
			Log.e( tag, msg );
		}
    }

    public static void v( String tag, String msg ) {
		if ( Config.isDebug ) {
			Log.v( tag, msg );
		}
    }

    public static void w( String tag, String msg ) {
		if ( Config.isDebug ) {
			Log.w( tag, msg );
		}
    }

}
