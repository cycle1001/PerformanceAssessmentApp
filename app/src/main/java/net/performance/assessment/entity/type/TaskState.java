package net.performance.assessment.entity.type;

import net.performance.assessment.R;

import android.content.Context;

/**
 *  发布单状态
 */

public class TaskState
{
    /**未发布*/
    public static final String NON_DISTRIBUTED = "001";

    /**发布中*/
    public static final String DISTRIBUTING = "002";

    /**执行中*/
    public static final String EXECUTING = "003";

    /**任务完成*/
    public static final String FINISHED = "004";

    public static String getDesc( Context context , String status ){
        String desc = "";
        if ( NON_DISTRIBUTED.equals( status ) )
        {
            desc = context.getString( R.string.task_state_non_distribute );
        }
        else if ( DISTRIBUTING.equals( status ) )
        {
            desc = context.getString( R.string.task_state_distributing );
        }
        else if ( EXECUTING.equals( status ) )
        {
            desc = context.getString( R.string.task_state_executing );
        }
        else if ( FINISHED.equals( status ) )
        {
            desc = context.getString( R.string.task_state_finished );
        }
        return desc;
    }
}
