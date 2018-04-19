package net.performance.assessment.view.widget;

import net.performance.assessment.R;

import android.content.Context;
import android.util.AttributeSet;

/**
 *
 */

public class AddPersonnelIconItem extends BasePersonnelIconItem
{
    public AddPersonnelIconItem( Context context )
    {
        this( context , null );
    }

    public AddPersonnelIconItem( Context context, AttributeSet attrs )
    {
        this( context, attrs , 0 );
    }

    public AddPersonnelIconItem( Context context, AttributeSet attrs, int defStyleAttr )
    {
        super( context, attrs, defStyleAttr );
        inflate( R.layout.item_add_personnel );
        addContent( );
    }

}
