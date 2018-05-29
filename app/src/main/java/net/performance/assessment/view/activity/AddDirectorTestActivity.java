package net.performance.assessment.view.activity;

import net.performance.assessment.network.http.CommonAPI;

/**
 *
 */

public class AddDirectorTestActivity extends BaseActivity
{

    @Override
    protected void initData( )
    {
        super.initData( );
        CommonAPI.findUsers( "" , "" , mHttpCallback );

    }

    @Override
    public void finish( )
    {
        /*Intent intent = new Intent(  );
        long currentTime = System.currentTimeMillis( );
        PersonnelIconItemInfo itemInfo = new PersonnelIconItemInfo.Builder( ).id(
                "xyz" + currentTime ).name( "abc" + currentTime ).build( );
        intent.putExtra( Constant.SELECT_DIRECTOR, itemInfo );
        setResult( RESULT_OK , intent );
        super.finish( );*/
    }
}
