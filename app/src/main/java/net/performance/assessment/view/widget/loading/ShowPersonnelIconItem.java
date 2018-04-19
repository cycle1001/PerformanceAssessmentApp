package net.performance.assessment.view.widget.loading;

import net.performance.assessment.R;
import net.performance.assessment.view.widget.BasePersonnelIconItem;

import android.content.Context;
import android.util.AttributeSet;

/**
 *
 */

public class ShowPersonnelIconItem extends BasePersonnelIconItem
{
    public ShowPersonnelIconItem( Context context )
    {
        this( context , null );
    }

    public ShowPersonnelIconItem( Context context, AttributeSet attrs )
    {
        this( context, attrs , 0 );
    }

    public ShowPersonnelIconItem( Context context, AttributeSet attrs, int defStyleAttr )
    {
        super( context, attrs, defStyleAttr );
        inflate( R.layout.item_show_personnel );
        addContent( );
    }
}
