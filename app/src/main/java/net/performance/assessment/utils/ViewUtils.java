package net.performance.assessment.utils;

import android.app.Activity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 视图工具类
 */
public class ViewUtils
{

    public static < T extends View > T xFindViewById( View rootView, int resourceId )
    {
        return ( T ) rootView.findViewById( resourceId );
    }

    public static < T extends View > T xFindViewById( Activity activity, int resourceId )
    {
        return ( T ) activity.findViewById( resourceId );
    }

    public static String getEditViewContent( TextView editView )
    {
        return editView.getText().toString();
    }

    public static void hidePopupWindow( PopupWindow popupWindow )
    {
        if ( popupWindow != null && popupWindow.isShowing( ) )
        {
            popupWindow.dismiss( );
        }
    }
}
