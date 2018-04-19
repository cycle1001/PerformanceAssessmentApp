package net.performance.assessment.view.adapter;

import net.performance.assessment.R;
import net.performance.assessment.entity.PersonnelSimpleInfo;
import net.performance.assessment.utils.ViewUtils;

import android.content.Context;
import android.widget.TextView;

/**
 *
 */

public class DepartmentInfoAdapter extends CommonAdapter<PersonnelSimpleInfo>
{
    @Override
    protected void setConvertView( ViewHolder holder, int position, Context context )
    {
        PersonnelSimpleInfo info = mDataList.get( position );

        TextView name = ViewUtils.xFindViewById( holder.getConvertView( ) , R.id.tv_name );
        name.setText( info.name );
    }

    @Override
    protected int getLayoutId( )
    {
        return R.layout.item_peronnel_simple_info;
    }
}
