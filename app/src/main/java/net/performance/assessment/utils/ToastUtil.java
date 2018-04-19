package net.performance.assessment.utils;

import net.performance.assessment.R;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 *
 */
public class ToastUtil
{

    /**
     * 显示中部提示
     *
     * @param ctx   上下文
     * @param msgId 信息ID
     */
    public static void showCenterTip( Context ctx, int msgId ) {
        showCenterTip( ctx, ctx.getText( msgId ).toString( ) );
    }

    /**
     * 显示中部提示
     *
     * @param ctx 上下文
     * @param msg 提示信息
     */
    public static void showCenterTip( Context ctx, String msg ) {
        Toast t = Toast.makeText( ctx, msg, Toast.LENGTH_SHORT );
        t.setGravity( Gravity.CENTER, 0, 0 );
        t.show( );
    }

    /**
     * 显示底部提示
     *
     * @param ctx   上下文
     * @param msgId 信息ID
     */
    public static void showTip( Context ctx, int msgId ) {
        showTip( ctx, ctx.getText( msgId ).toString( ) );
    }

    public static void showTip( Context ctx, int msgId, int duration ) {
        showTip( ctx, ctx.getText( msgId ).toString( ), duration );
    }

    /**
     * 显示底部提示
     *
     * @param ctx 上下文
     * @param msg 提示信息
     */
    public static void showTip( Context ctx, String msg ) {
        showTip( ctx, msg, Toast.LENGTH_SHORT );
    }

    public static void showTip( Context ctx, String msg, int duration ) {
        Toast t = Toast.makeText( ctx.getApplicationContext( ) , msg, duration );
        t.setText( msg );
        t.show( );
    }

    public static void showErrorMessage( Context context , String defaultErrorMessage , String errorCode ){
        String customErrorMessage = ErrorCodeConverter.getErrorMessage( context , errorCode );
        String toastContent = context.getString( R.string.error_unknown );
        if ( !StringUtil.isEmptyOrNullStr( customErrorMessage ) )
        {
            toastContent = customErrorMessage;
        }
        else {
            if ( !StringUtil.isEmptyOrNullStr( defaultErrorMessage ) )
            {
                toastContent = defaultErrorMessage;
            }
        }
        showTip( context , toastContent );
    }
}
