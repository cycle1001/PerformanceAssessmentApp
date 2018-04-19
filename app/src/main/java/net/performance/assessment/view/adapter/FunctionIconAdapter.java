package net.performance.assessment.view.adapter;


import net.performance.assessment.R;
import net.performance.assessment.entity.FunctionIconInfo;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2016/11/20.
 */
public class FunctionIconAdapter extends CommonAdapter<FunctionIconInfo >
{
    private int width = 0;

    public FunctionIconAdapter( List< FunctionIconInfo > data )
    {
        super( data );
    }

    @Override
    protected void setConvertView( ViewHolder holder, int position, Context context ) {
        FunctionIconInfo info = getItem( position );

        ImageView functionIcon = holder.getItemView( R.id.function_icon );
        functionIcon.setImageResource( info.functionIconRes );

        TextView functionDesc = holder.getItemView( R.id.function_desc );
        functionDesc.setText( info.functionDesc );


    }

    @Override
    protected int getLayoutId( )
    {
        return R.layout.item_function_icon;
    }

    public void setWidth( int width )
    {
        this.width = width;
    }
}
