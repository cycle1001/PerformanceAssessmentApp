package net.performance.assessment.utils;

import android.content.Context;

/**
 * 错误码转换辅助类
 */
public class ErrorCodeConverter
{

    private final static String DEFAULT_PREFIX = "error_";
    /**
     * getErrorMessage
     * @Description: 获取错误信息
     * @param context
     * @param errorCode
     * @return
     */
    public static String getErrorMessage( Context context , String errorCode )
    {
        String errorMsg = "";

        StringBuilder builder = new StringBuilder( DEFAULT_PREFIX );
        builder.append( errorCode );

        int stringRes = ResourceUtils.getStringId( context , builder.toString( ) );

        if ( stringRes > 0 )
        {
            errorMsg = context.getString( stringRes );
        }
        return errorMsg;
    }
}
