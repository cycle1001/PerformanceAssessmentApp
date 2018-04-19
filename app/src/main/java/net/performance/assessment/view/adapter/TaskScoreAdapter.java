package net.performance.assessment.view.adapter;

import net.performance.assessment.R;
import net.performance.assessment.entity.TaskScoreInfo;

import android.content.Context;
import android.widget.TextView;

/**
 *
 */

public class TaskScoreAdapter extends CommonAdapter<TaskScoreInfo>
{
    @Override
    protected void setConvertView( ViewHolder holder, int position, Context context )
    {
        TaskScoreInfo info = getItem( position );

        TextView tvContent = holder.getItemView( R.id.tv_task_content );
        tvContent.setText( info.taskContent );

        TextView tvFinishedTime = holder.getItemView( R.id.tv_finished_time );
        tvFinishedTime.setText( info.finishTime );

        TextView tvScore = holder.getItemView( R.id.tv_task_score );
        tvScore.setText( String.valueOf( info.finalScore ) );
    }

    @Override
    protected int getLayoutId( )
    {
        return R.layout.item_task_score_info;
    }
}
