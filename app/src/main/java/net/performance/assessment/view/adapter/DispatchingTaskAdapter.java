package net.performance.assessment.view.adapter;

import net.performance.assessment.R;
import net.performance.assessment.entity.DispatchTaskInfo;
import net.performance.assessment.entity.type.DispatchStatus;

import android.content.Context;
import android.widget.TextView;

/**
 *
 */

public class DispatchingTaskAdapter extends CommonAdapter<DispatchTaskInfo >
{
    @Override
    protected void setConvertView( ViewHolder holder, int position, Context context )
    {
        DispatchTaskInfo info = getItem( position );

        TextView tvCreateDate = holder.getItemView( R.id.tv_create_date );
        tvCreateDate.setText( info.createDate );

        TextView tvContent = holder.getItemView( R.id.tv_task_content );
        tvContent.setText( info.dispatchContent );

        TextView tvScore = holder.getItemView( R.id.tv_task_score );
        //tvScore.setText( String.valueOf( info.taskscore ) );

        TextView tvStatus = holder.getItemView( R.id.tv_task_status );
        tvStatus.setText( DispatchStatus.getDesc( context , info.dispatchStatus ) );
    }

    @Override
    protected int getLayoutId( )
    {
        return R.layout.item_dispatching_task;
    }
}
