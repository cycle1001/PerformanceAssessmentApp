package net.performance.assessment.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 *
 */
public abstract class CommonAdapter< T > extends BaseAdapter
{

    protected List< T > mDataList;

    public CommonAdapter( ){

    }

    public CommonAdapter( List< T > data ) {
        mDataList = data;
    }

    @Override
    public int getCount( ) {
        return mDataList == null ? 0 : mDataList.size( );
    }

    @Override
    public T getItem( int position ) {
        return mDataList.get( position );
    }

    @Override
    public long getItemId( int position ) {
        return position;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
        ViewHolder holder = ViewHolder.newsInstance(
                convertView, parent.getContext( ), getLayoutId( ) );

        setConvertView( holder, position, parent.getContext( ) );

        return holder.getConvertView( );

    }

    protected abstract void setConvertView( ViewHolder holder, int position, Context context );

    protected abstract int getLayoutId( );

    public void setDataList( List< T > dataList ) {
        mDataList = dataList;
    }

    public List< T > getDataList( )
    {
        return mDataList;
    }
}
