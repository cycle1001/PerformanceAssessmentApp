package net.performance.assessment.utils;

import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.MenuItemInfo;
import net.performance.assessment.entity.MenuItemListBean;
import net.performance.assessment.interfaces.LoadMenuCallback;
import net.performance.assessment.network.http.CommonAPI;
import net.performance.assessment.network.http.SimpleHttpCallback;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class CustomConfigManager
{
    private static final CustomConfigManager ourInstance = new CustomConfigManager( );

    private List<LoadMenuCallback> mLoadMenuCallbacks;

    private long getUserMenuFlag;

    private List<MenuItemInfo> mMenuItemInfos;
    private int mErrorCode;

    private boolean hasData = false;

    public static CustomConfigManager getInstance( )
    {
        return ourInstance;
    }

    private CustomConfigManager( )
    {
        if ( mLoadMenuCallbacks == null )
        {
            mLoadMenuCallbacks = new ArrayList<>(  );
        }
    }

    public boolean isHasData( )
    {
        return hasData;
    }

    public List< MenuItemInfo > getMenuItemInfos( )
    {
        return mMenuItemInfos;
    }

    public void setLoadMenuCallbacks( int key , LoadMenuCallback callback ){
        mLoadMenuCallbacks.add( callback );
    }

    public void getUserMenu( ){
        getUserMenuFlag = CommonAPI.getUserMenu( mHttpCallback );
    }

    private SimpleHttpCallback mHttpCallback = new SimpleHttpCallback(){
        @Override
        public void onHttpResponseSuccess(String result, long flag) {
            LogUtils.v("onHttpResponseSuccess");
            BaseResultBean detectedBean = JsonParser.getInstance( ).getBeanFromJsonString(result, BaseResultBean.class );
            if ( detectedBean.isSuccess( ) ) {
                MenuItemListBean bean = JsonParser.getInstance( ).getBeanFromJsonString(result, MenuItemListBean.class );
                if (bean.data != null && bean.data.size() > 0)
                {
                    hasData = true;
                    if ( mMenuItemInfos == null )
                    {
                        mMenuItemInfos = new ArrayList<>(  );
                    }
                    else {
                        mMenuItemInfos.clear( );
                    }
                    mMenuItemInfos.addAll( bean.data );
                    callbackSuccess( );
                }
                else {
                    callbackFailure( );
                }
            }
            else {
                callbackFailure( );
            }
        }

        @Override
        public void onHttpResponseFailure(int error, long flag) {
            LogUtils.v("onHttpResponseFailure");
            callbackFailure( );
        }

        @Override
        public void onHttpResponseTimeout(long flag) {
            LogUtils.v("onHttpResponseTimeout");
            callbackFailure( );
        }
    };

    private void callbackSuccess( ){
        for ( LoadMenuCallback callback : mLoadMenuCallbacks )
        {
            callback.onLoadMenuSuccess( );
        }
    }

    private void callbackFailure(  ){
        for ( LoadMenuCallback callback : mLoadMenuCallbacks )
        {
            callback.onLoadMenuFailed( );
        }
    }
}
