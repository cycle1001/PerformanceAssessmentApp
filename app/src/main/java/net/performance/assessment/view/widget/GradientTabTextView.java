package net.performance.assessment.view.widget;


import net.performance.assessment.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 *具有渐变效果选项TextView
 */
public class GradientTabTextView extends TextView
{
    private int mSelectedColor;
    private int mSelectedAlpha;

    private Paint mPaint;

    public GradientTabTextView( Context context )
    {
        super( context );
    }

    public GradientTabTextView( Context context, AttributeSet attrs )
    {
        this( context, attrs , 0 );
    }

    public GradientTabTextView( Context context, AttributeSet attrs, int defStyleAttr )
    {
        super( context, attrs, defStyleAttr );
        init( attrs );
    }

    private void init(  AttributeSet attrs )
    {
        TypedArray typedArray = getContext( ).obtainStyledAttributes( attrs,
                R.styleable.GradientTabImageView );
        mSelectedColor = typedArray.getColor( R.styleable.GradientTabTextView_selected_color , 0xFF0043A3 );
        typedArray.recycle( );

        mPaint = new Paint(  );
        mPaint.setAntiAlias( true );
        mPaint.setDither( true );
        mPaint.setTextSize( getTextSize( ) );
        mPaint.setColor( mSelectedColor );
        mPaint.setTextAlign( Paint.Align.LEFT );
    }

    public void setTabOffSet( float offSet )
    {
        mSelectedAlpha = ( int ) ( 255 - offSet * 255 );
        invalidate( );
    }

    private void updateAlpha( Canvas canvas )
    {
        mPaint.setAlpha( mSelectedAlpha );
        canvas.drawText( getText( ).toString( ) , 0 , getBaseline() , mPaint );
    }

    @Override
    protected void onDraw( Canvas canvas )
    {
        super.onDraw( canvas );
        updateAlpha( canvas );
    }
}
