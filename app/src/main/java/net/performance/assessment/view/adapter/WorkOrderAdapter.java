package net.performance.assessment.view.adapter;

import net.performance.assessment.R;
import net.performance.assessment.entity.WorkOrderInfo;

import android.content.Context;
import android.widget.TextView;

/**
 *
 */

public class WorkOrderAdapter extends CommonAdapter<WorkOrderInfo>
{
    @Override
    protected void setConvertView( ViewHolder holder, int position, Context context )
    {
        WorkOrderInfo info = getItem( position );

        TextView tvTaskId = holder.getItemView( R.id.tv_task_id );
        tvTaskId.setText( info.getDispatchNum( ) );

        TextView tvContent = holder.getItemView( R.id.tv_task_content );
        tvContent.setText( info.getDispatchContent( ) );

        TextView tvReceiver = holder.getItemView( R.id.tv_receiver );
        tvReceiver.setText( info.getReceivePersonName( ) );

        TextView tvStatus = holder.getItemView( R.id.tv_task_status );
        tvStatus.setText(  info.getDispatchStatusName( ) );
    }

    @Override
    protected int getLayoutId( )
    {
        return R.layout.item_work_order_info;
    }
}
