package net.performance.assessment.entity.type;

import net.performance.assessment.R;

import android.content.Context;

/**
 * 派单状态
 */

public class DispatchStatus
{
 /*   @StringDef({ DISPATCHED , EXECUTING , FINISHED , ASSESSED })
    @Retention( RetentionPolicy.SOURCE )
    public @interface Status { }*/

    /**已派单*/
    public static final String DISPATCHED = "001";

    /**派单执行中*/
    public static final String EXECUTING = "002";

    /**派单执行完成*/
    public static final String FINISHED = "003";

    /**派单考核完成*/
    public static final String ASSESSED = "004";

    public static String getDesc( Context context , String status ){
        String desc = "";
        if ( DISPATCHED.equals( status ) )
        {
            desc = context.getString( R.string.dispatch_status_dispatched );
        }
        else if ( EXECUTING.equals( status ) )
        {
            desc = context.getString( R.string.dispatch_status_executing );
        }
        else if ( FINISHED.equals( status ) )
        {
            desc = context.getString( R.string.dispatch_status_finished );
        }
        else if ( ASSESSED.equals( status ) )
        {
            desc = context.getString( R.string.dispatch_status_assessed );
        }
        return desc;
    }
}
