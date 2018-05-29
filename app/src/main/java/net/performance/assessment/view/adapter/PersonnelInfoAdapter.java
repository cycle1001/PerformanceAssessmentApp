package net.performance.assessment.view.adapter;

import net.performance.assessment.R;
import net.performance.assessment.entity.PersonnelSimpleInfo;
import net.performance.assessment.utils.ViewUtils;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.HashMap;

/**
 *
 */

public class PersonnelInfoAdapter extends CommonAdapter< PersonnelSimpleInfo >
{
    private HashMap< String, Boolean > mSelectionMap;

    public void initData( )
    {
        if ( mSelectionMap == null )
        {
            mSelectionMap = new HashMap<>(  );
        }
        for ( PersonnelSimpleInfo info : mDataList )
        {
            mSelectionMap.put( info.id, false );
        }
    }

    @Override
    protected void setConvertView( ViewHolder holder, int position, Context context )
    {
        PersonnelSimpleInfo info = mDataList.get( position );

        if ( info != null )
        {
            TextView name = ViewUtils.xFindViewById( holder.getConvertView( ), R.id.tv_class_person_name );
            name.setText( info.name );

            // 根据isSelected来设置checkbox的选中状况
            CheckBox cbIsSelect = ViewUtils.xFindViewById( holder.getConvertView( ), R.id.cb_select );
            cbIsSelect.setChecked( mSelectionMap.get( info.id ) );
        }
    }

    @Override
    protected int getLayoutId( )
    {
        return R.layout.item_class_person;
    }

    public HashMap< String, Boolean > getSelectionMap( )
    {
        return mSelectionMap;
    }
}
