package net.performance.assessment.view.widget;

import net.performance.assessment.R;
import net.performance.assessment.listener.CustomItemClickListener;
import net.performance.assessment.utils.ViewUtils;
import net.performance.assessment.view.adapter.BaseOptionPickerAdapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.List;

/**
 *
 */
public class BaseOptionPicker<T>
{
    protected Context mContext;
    protected PopupWindow mPopupWindow;
    protected View mLoadingProgress;

    protected ListView mListView;
    protected BaseOptionPickerAdapter< T > mAdapter;

    public BaseOptionPicker( Context context , BaseOptionPickerAdapter<T> adapter ){
        mContext = context;
        if ( mPopupWindow == null )
        {
            View contentView = LayoutInflater.from( mContext ).inflate(
                    R.layout.widget_option_picker, null );
            mLoadingProgress = ViewUtils.xFindViewById( contentView , R.id.loading_progress_bar );
            //int height = ( int ) ( CommonUtil.getScreenPixHeight( mContext ) * 0.5 );
            mPopupWindow = new PopupWindow( contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT );

            mListView = ViewUtils.xFindViewById( contentView , R.id.listview );

            mListView.setAdapter( adapter );
            mAdapter = adapter;

            View blankSpace = ViewUtils.xFindViewById( contentView , R.id.blank_space );
            blankSpace.setOnClickListener( new View.OnClickListener( )
            {
                @Override
                public void onClick( View v )
                {
                    hide( );
                }
            } );
        }
    }

    public void show( View anchorView )
    {
        if ( ! mPopupWindow.isShowing( ) )
        {
            mPopupWindow.setTouchable( true );
            mPopupWindow.setFocusable( true );
            mPopupWindow.setBackgroundDrawable( new BitmapDrawable( ) );
            mPopupWindow.setOutsideTouchable( true );
            mPopupWindow.setFocusable( true );
            mPopupWindow.showAtLocation( anchorView, Gravity.BOTTOM, 0, 0 );
        }
    }

    public void hide( ){
        if ( mPopupWindow!= null )
        {
            mPopupWindow.dismiss( );
        }
    }

    public void setItemClickListener( final CustomItemClickListener clickListener ){
        if ( mAdapter != null )
        {
            mListView.setOnItemClickListener( new AdapterView.OnItemClickListener( )
            {
                @Override
                public void onItemClick( AdapterView< ? > parent, View view, int position, long id )
                {
                    if ( clickListener != null )
                    {
                        List<T> list = mAdapter.getDataList( );
                        if ( list != null )
                        {
                            T data = list.get( position );
                            clickListener.onItemClick( position , data );
                        }
                    }
                }
            } );
        }
    }
}
