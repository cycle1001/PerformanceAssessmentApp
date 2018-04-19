package net.performance.assessment.view.widget;

import net.performance.assessment.utils.LogUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 *
 */

public class BasePersonnelIconItem extends RelativeLayout
{
    protected View mContentView;

    public BasePersonnelIconItem( Context context )
    {
        this( context , null );
    }

    public BasePersonnelIconItem( Context context, AttributeSet attrs )
    {
        this( context, attrs , 0 );
    }

    public BasePersonnelIconItem( Context context, AttributeSet attrs, int defStyleAttr )
    {
        super( context, attrs, defStyleAttr );
        init( );
    }

    public void inflate( int layoutRes ){
        mContentView = LayoutInflater.from( getContext() ).inflate( layoutRes , null );
    }

    private void init( ){
        setLayoutParams( new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT ,
                ViewGroup.LayoutParams.WRAP_CONTENT  , 1.0f ) );
    }

    public void addContent(  ){
        if ( mContentView == null ){
            LogUtils.v( "mContentView null" );
            return;
        }
        addView( mContentView );
    }

    public void hideContent( ){
        this.setVisibility( INVISIBLE );
    }
}
