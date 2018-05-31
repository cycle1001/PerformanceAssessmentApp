package net.performance.assessment.view.adapter;

import net.performance.assessment.R;
import net.performance.assessment.entity.MonthPerformanceScoreInfo;

import android.content.Context;
import android.widget.TextView;

/**
 *
 */

public class MonthPerformanceScoreAdapter extends CommonAdapter<MonthPerformanceScoreInfo >
{
    @Override
    protected void setConvertView( ViewHolder holder, int position, Context context )
    {
        MonthPerformanceScoreInfo info = getItem( position );

        TextView tvScore = holder.getItemView( R.id.tv_this_month_task_score );
        tvScore.setText( String.valueOf( info.countNum ) );

        TextView tvOverviewScore = holder.getItemView( R.id.tv_overview_score );
        tvOverviewScore.setText( String.valueOf( info.emplyeeScore ) );

        TextView tvDepartmentalScore = holder.getItemView( R.id.tv_departmental_score );
        tvDepartmentalScore.setText( String.valueOf( info.departMentScore ) );

        TextView tvFinalScore = holder.getItemView( R.id.tv_final_score );
        tvFinalScore.setText( String.valueOf( info.finalEmpScore ) );
    }

    @Override
    protected int getLayoutId( )
    {
        return R.layout.item_month_performance_score;
    }
}
