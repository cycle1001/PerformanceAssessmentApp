package net.performance.assessment.view.adapter;

import net.performance.assessment.R;
import net.performance.assessment.entity.TaskSimpleInfo;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

/**
 *
 */

public class TaskSimpleInfoAdapter extends CommonAdapter<TaskSimpleInfo>
{
    @Override
    protected void setConvertView( ViewHolder holder, int position, Context context )
    {
        TaskSimpleInfo info = getItem( position );

        TextView tvTaskContent = holder.getItemView( R.id.tv_task_content );
        tvTaskContent.setText( info.taskContent );

        TextView tvProjectType = holder.getItemView( R.id.tv_project_type );
        if (!TextUtils.isEmpty(info.workItemTypeName)) {
            String[] projectTypeStr = info.workItemTypeName.split("\\|");
            tvProjectType.setText(projectTypeStr.length > 0 ? projectTypeStr[0] : "");
        }

        TextView tvTaskStatus = holder.getItemView( R.id.tv_task_status );
        tvTaskStatus.setText( info.orderStatusName );
    }

    @Override
    protected int getLayoutId( )
    {
        return R.layout.item_task_simple_info;
    }
}
