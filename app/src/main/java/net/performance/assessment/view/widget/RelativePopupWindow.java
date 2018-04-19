package net.performance.assessment.view.widget;

import net.performance.assessment.entity.type.HorizontalPosition;
import net.performance.assessment.entity.type.VerticalPosition;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.PopupWindow;

/**
 *
 */

public class RelativePopupWindow extends PopupWindow
{
    public RelativePopupWindow( Context context )
    {
        super( context );
    }

    public RelativePopupWindow( Context context , AttributeSet attrs )
    {
        super( context , attrs );
    }

    public RelativePopupWindow( Context context , AttributeSet attrs , int defStyleAttr )
    {
        super( context , attrs , defStyleAttr );
    }

    @TargetApi( Build.VERSION_CODES.HONEYCOMB )
    public RelativePopupWindow( Context context , AttributeSet attrs , int defStyleAttr ,
            int defStyleRes )
    {
        super( context , attrs , defStyleAttr , defStyleRes );
    }

    public RelativePopupWindow( )
    {
        super( );
    }

    public RelativePopupWindow( View contentView )
    {
        super( contentView );
    }

    public RelativePopupWindow( int width , int height )
    {
        super( width , height );
    }

    public RelativePopupWindow( View contentView , int width , int height )
    {
        super( contentView , width , height );
    }

    public RelativePopupWindow( View contentView , int width , int height , boolean focusable )
    {
        super( contentView , width , height , focusable );
    }

    /**
     * setCancelFromOutside
     * @Description: 设置点击弹窗以外的区域是否隐藏
     * @param enable
     */
    public void setCancelFromOutside( boolean enable )
    {
        this.setTouchable( enable );
        this.setFocusable( enable );
        this.setBackgroundDrawable( new BitmapDrawable( ) );
        this.setOutsideTouchable( enable );
    }

    /**
     * Show at relative position to anchor View.
     *
     * @param anchor
     *            Anchor View
     * @param vertPos
     *            Vertical Position Flag
     * @param horizPos
     *            Horizontal Position Flag
     */
    public void showOnAnchor( View anchor , int vertPos , int horizPos )
    {
        showOnAnchor( anchor , vertPos , horizPos , 0 , 0 );
    }

    /**
     * Show at relative position to anchor View with translation.
     *
     * @param anchor
     *            Anchor View
     * @param vertPos
     *            Vertical Position Flag
     * @param horizPos
     *            Horizontal Position Flag
     * @param x
     *            Translation X
     * @param y
     *            Translation Y
     */
    public void showOnAnchor( View anchor , int vertPos , int horizPos , int x , int y )
    {
        View contentView = getContentView( );

        contentView.measure( View.MeasureSpec.UNSPECIFIED , View.MeasureSpec.UNSPECIFIED );
        final int measuredW = contentView.getMeasuredWidth( );
        final int measuredH = contentView.getMeasuredHeight( );
        switch ( vertPos )
        {
            case VerticalPosition.ABOVE :
                y -= measuredH + anchor.getHeight( );
                break;
            case VerticalPosition.ALIGN_BOTTOM :
                y -= measuredH;
                break;
            case VerticalPosition.CENTER :
                y -= anchor.getHeight( ) / 2 + measuredH / 2;
                break;
            case VerticalPosition.ALIGN_TOP :
                y -= anchor.getHeight( );
                break;
            case VerticalPosition.BELOW :
                // Default position.
                break;
        }
        switch ( horizPos )
        {
            case HorizontalPosition.LEFT :
                x -= measuredW;
                break;
            case HorizontalPosition.ALIGN_RIGHT :
                x -= measuredW - anchor.getWidth( );
                break;
            case HorizontalPosition.CENTER :
                x += anchor.getWidth( ) / 2 - measuredW / 2;
                break;
            case HorizontalPosition.ALIGN_LEFT :
                // Default position.
                break;
            case HorizontalPosition.RIGHT :
                x += anchor.getWidth( );
                break;
            default:
                x -= measuredW;
                break;
        }
        showAsDropDown( anchor , x , y );
    }
}
