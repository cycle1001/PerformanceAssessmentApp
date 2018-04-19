package net.performance.assessment.view.widget;

import net.performance.assessment.entity.PersonnelIconItemInfo;
import net.performance.assessment.utils.CommonUtils;
import net.performance.assessment.utils.LogUtils;
import net.performance.assessment.view.widget.loading.ShowPersonnelIconItem;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class DynamicItemManager
{

    private static volatile DynamicItemManager instance;

    private final static int ROW_SIZE = 4;

    private LinearLayout mRootGroup;

    private List<LinearLayout> mRowGroups;

    private List<PersonnelIconItemInfo> mItemData;

    public static DynamicItemManager getInstance( ) {
        if (instance == null) {
            synchronized (DynamicItemManager.class) {
                if (instance == null) {
                    instance = new DynamicItemManager();
                }
            }
        }
        return instance;
    }

    public DynamicItemManager( ){
        mRowGroups = new ArrayList<>(  );
        mItemData = new ArrayList<>(  );
    }

    public void init( Context context , LinearLayout rootGroup ){
        if ( rootGroup == null ){
            LogUtils.v( "rootGroup null" );
            return;
        }
        mRootGroup = rootGroup;

        if ( mRootGroup.getChildCount() > 0 )
        {
            mRootGroup.removeAllViews( );
        }

        int size = mItemData.size( );
        int rowNum = size / ROW_SIZE + 1;
        int mod = size % ROW_SIZE;

        LinearLayout row = new LinearLayout( context );
        row.setOrientation( LinearLayout.HORIZONTAL );
        int height = CommonUtils.dipToPx( context , 80 );
        row.setLayoutParams( new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT , height ) );

        mRootGroup.addView( row );

        mRowGroups.add( row );
    }

    public void updateItemGroup( ){
        if ( mRootGroup.getChildCount() > 0 )
        {
            mRootGroup.removeAllViews( );
        }

        int size = mItemData.size( );
        int rowNum = size / ROW_SIZE + 1;
        int mod = size % ROW_SIZE;


    }

    private void generateRow( Context context , int rowNum , int mod ){
        int height = CommonUtils.dipToPx( context , 80 );
        for ( int i = 0; i < rowNum; i++ )
        {
            LinearLayout row = new LinearLayout( context );
            row.setOrientation( LinearLayout.HORIZONTAL );
            row.setLayoutParams( new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT , height ) );

            if ( i == rowNum - 1 )
            {

            }
            else {

            }

            mRootGroup.addView( row );
        }

    }

    private void fillRow( Context context , LinearLayout row , int mod ){
        BasePersonnelIconItem item;
        for ( int i = 0; i < ROW_SIZE; i++ )
        {
            if ( i >= mod )
            {
                item = new AddPersonnelIconItem( context );
                if ( i > mod )
                {
                    item.hideContent( );
                }
            }
            else {
                item = new ShowPersonnelIconItem( context );
            }
            row.addView( item );
        }
    }

    public void release( ){
        mRowGroups.clear();
        mRootGroup = null;
        mItemData.clear( );
    }
}
