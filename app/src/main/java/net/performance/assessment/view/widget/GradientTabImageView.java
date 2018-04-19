package net.performance.assessment.view.widget;


import net.performance.assessment.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 具有渐变效果选项ImageView
 */
public class GradientTabImageView extends ImageView
{
    private Drawable mSelectedDrawable;
    private int mWidth;
    private int mHeight;

    private int mSelectedAlpha;

    public GradientTabImageView( Context context )
    {
        super( context );
    }

    public GradientTabImageView( Context context, AttributeSet attrs )
    {
        this( context, attrs , 0 );
    }

    public GradientTabImageView( Context context, AttributeSet attrs, int defStyleAttr )
    {
        super( context, attrs, defStyleAttr );
        init( attrs );
    }

    private void init(  AttributeSet attrs )
    {
        TypedArray typedArray = getContext( ).obtainStyledAttributes( attrs,
                R.styleable.GradientTabImageView );
        mSelectedDrawable = typedArray.getDrawable( R.styleable.GradientTabImageView_selected_drawable );
        typedArray.recycle( );
    }

    public void setTabOffSet( float offSet )
    {
        mSelectedAlpha = ( int ) ( 255 - offSet * 255 );
        invalidate( );
    }

    private void updateAlpha( Canvas canvas )
    {
        if ( mSelectedDrawable == null )
        {
            return;
        }
        mSelectedDrawable.setBounds( 0, 0, mWidth, mHeight );
        mSelectedDrawable.setAlpha( mSelectedAlpha );
        mSelectedDrawable.draw( canvas );
    }

    @Override
    protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec )
    {
        super.onMeasure( widthMeasureSpec, heightMeasureSpec );
        mWidth = getMeasuredWidth( );
        mHeight = getMeasuredHeight( );
    }

    @Override
    protected void onDraw( Canvas canvas )
    {
        super.onDraw( canvas );
        updateAlpha( canvas );
    }
}
