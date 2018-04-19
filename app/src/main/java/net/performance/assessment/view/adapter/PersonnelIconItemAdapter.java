package net.performance.assessment.view.adapter;

import net.performance.assessment.R;
import net.performance.assessment.entity.PersonnelIconItemInfo;
import net.performance.assessment.utils.ViewUtils;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 *
 */

public class PersonnelIconItemAdapter extends CommonAdapter<PersonnelIconItemInfo>
{
    @Override
    protected void setConvertView( ViewHolder holder, int position, Context context )
    {
        PersonnelIconItemInfo info = getItem( position );
        View contentView = holder.getConvertView( );
        View addView = ViewUtils.xFindViewById( contentView , R.id.add_view );
        View infoView = ViewUtils.xFindViewById( contentView , R.id.info_view );
        if ( position == getCount( ) - 1 )
        {
            addView.setVisibility( View.VISIBLE );
            infoView.setVisibility( View.INVISIBLE );
        }
        else {
            addView.setVisibility( View.INVISIBLE );
            infoView.setVisibility( View.VISIBLE );
            TextView name = ViewUtils.xFindViewById( infoView , R.id.tv_user_name );
            name.setText( info.name );
        }
    }

    @Override
    protected int getLayoutId( )
    {
        return R.layout.item_show_personnel;
    }
}
