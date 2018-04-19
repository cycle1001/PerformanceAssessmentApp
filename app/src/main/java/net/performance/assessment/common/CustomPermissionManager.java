package net.performance.assessment.common;

import net.performance.assessment.interfaces.CheckPermissionCallback;
import net.performance.assessment.utils.LogUtils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import didikee.com.permissionshelper.PermissionsHelper;

/**
 *
 */

public class CustomPermissionManager
{
    private static volatile CustomPermissionManager instance = null;

    private PermissionsHelper mPermissionsHelper;

    public static final String[] PERMISSIONS = {
            Manifest.permission.READ_PHONE_STATE , Manifest.permission.CAMERA ,
            Manifest.permission.ACCESS_COARSE_LOCATION , Manifest.permission.ACCESS_FINE_LOCATION ,
            Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private int mDeniedCount;
    private boolean isFirst = false;

    public static CustomPermissionManager getInstance( )
    {
        if ( instance == null )
        {
            synchronized ( CustomPermissionManager.class )
            {
                if ( instance == null )
                {
                    instance = new CustomPermissionManager( );
                }
            }
        }
        return instance;
    }

    public CustomPermissionManager( )
    {

    }

    public void init( Activity context )
    {
        mPermissionsHelper = new PermissionsHelper( context, PERMISSIONS, true );
    }

    public void request( final CheckPermissionCallback callback )
    {

        if ( mPermissionsHelper.checkAllPermissions( PERMISSIONS ) )
        {
            mPermissionsHelper.onDestroy( );
            if ( callback != null )
            {
                callback.onPermissionAllGranted( );
            }
        }
        else
        {
            //申请权限
            mPermissionsHelper.startRequestNeedPermissions( );
        }

        mPermissionsHelper.setonAllNeedPermissionsGrantedListener(
                new PermissionsHelper.onAllNeedPermissionsGrantedListener( )
                {


                    @Override
                    public void onAllNeedPermissionsGranted( )
                    {
                        LogUtils.v( "onAllNeedPermissionsGranted" );
                        if ( callback != null )
                        {
                            callback.onPermissionAllGranted( );
                        }
                    }

                    @Override
                    public void onPermissionsDenied( )
                    {
                        //LogUtils.v("onPermissionsDenied" );
                        mDeniedCount += 1;
                        if ( mDeniedCount > PERMISSIONS.length && !isFirst )
                        {
                            if ( callback != null )
                            {
                                isFirst = true;
                                callback.onPermissionDeniedPermantly( );
                            }
                        }
                        else {
                            if ( callback != null )
                            {
                                callback.onPermissionDeniedTemporarily( );
                            }
                        }
                    }

                    @Override
                    public void hasLockForever( )
                    {
                        LogUtils.v("hasLockForever" );
                    }

                    @Override
                    public void onBeforeRequestFinalPermissions( PermissionsHelper helper )
                    {
                       /* if ( callback != null )
                        {
                            callback.onPermissionDenied( );
                        }*/
                    }
                } );
    }

    public void onRequestPermissionsResult( int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults ){
        if ( mPermissionsHelper != null ){
            mPermissionsHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void onActivityResult( int requestCode, int resultCode, Intent data ){
        if ( mPermissionsHelper != null ){
            mPermissionsHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void release( ){
        if ( mPermissionsHelper != null )
        {
            mPermissionsHelper.onDestroy();
        }
    }
}
