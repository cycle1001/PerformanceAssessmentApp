package net.performance.assessment.view.widget;

import net.performance.assessment.R;
import net.performance.assessment.utils.ViewUtils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 *  自定义顶部标题栏
 */
public class CustomTitleBar extends RelativeLayout
{
    //private Button btnLeft;
    private ImageView btnLeft;
    private Button btnRight;
    private TextView tvTitle;

    private OnClickListener mLeftListener;
    private OnClickListener mRightListener;

    public CustomTitleBar( Context context )
    {
        this( context , null );
    }

    public CustomTitleBar( Context context, AttributeSet attrs )
    {
        this( context, attrs , 0 );
    }

    public CustomTitleBar( Context context, AttributeSet attrs, int defStyleAttr )
    {
        super( context, attrs, defStyleAttr );
        View rootView = LayoutInflater.from(context ).inflate( R.layout.common_title_bar, this );
        initView( rootView );
        initAttribute( context , attrs );
    }

    private void initView( View rootView ){
        btnLeft = ViewUtils.xFindViewById( rootView , R.id.btn_left );
        btnRight = ViewUtils.xFindViewById( rootView , R.id.btn_right );
        tvTitle = ViewUtils.xFindViewById( rootView , R.id.tv_title );
    }

    private void initAttribute( Context context, AttributeSet attrs ){
        TypedArray typedArray = context.obtainStyledAttributes( attrs , R.styleable.CustomTitleBar );
        int leftBtnBackground = typedArray.getResourceId( R.styleable.CustomTitleBar_left_background , 0 );
        int rightBtnBackground = typedArray.getResourceId( R.styleable.CustomTitleBar_right_background , 0 );
        String titleStr = typedArray.getString( R.styleable.CustomTitleBar_title_text );

        int defaultSize = ( int ) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_SP, 16,
                getResources( ).getDisplayMetrics( ) );
        float titleTextSize = typedArray.getDimensionPixelSize(
                R.styleable.CustomTitleBar_title_text_color, 16 );

        int titleTextColor = typedArray.getColor( R.styleable.CustomTitleBar_title_text_color , 0xFFFFFFFF );
        typedArray.recycle( );

        //btnLeft.setBackgroundResource( leftBtnBackground );
        btnLeft.setImageResource( leftBtnBackground );
        btnRight.setBackgroundResource( rightBtnBackground );

        tvTitle.setText( titleStr );
        tvTitle.setTextSize( titleTextSize );
        tvTitle.setTextColor( titleTextColor );
    }

    public CustomTitleBar setLeftBtnVisibility( int visibility ){
        btnLeft.setVisibility( visibility );
        return this;
    }

    public CustomTitleBar setRightBtnVisibility( int visibility ){
        btnRight.setVisibility( visibility );
        return this;
    }

    public CustomTitleBar setTitle( String title ){
        tvTitle.setText( title );
        return this;
    }

    public CustomTitleBar setRightText( String rightText ){
        btnRight.setText( rightText );
        return this;
    }

    public CustomTitleBar setLeftClickListener( OnClickListener leftClickListener ){
        btnLeft.setOnClickListener( leftClickListener );
        return this;
    }

    public CustomTitleBar setRightClickListener( OnClickListener rightClickListener ){
        btnRight.setOnClickListener( rightClickListener );
        return this;
    }
}
