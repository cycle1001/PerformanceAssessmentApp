package net.performance.assessment.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;

/**
 *
 */
public class IntentUtils
{
    public static void startActivity( Context context, Class< ? > clazz )
    {
        context.startActivity( new Intent( context, clazz ) );
    }

    public static void startActivity( Context context, Class< ? > clazz, Bundle bundle )
    {
        Intent intent = new Intent( context, clazz );
        intent.putExtras( bundle );
        context.startActivity( intent );
    }

    public static void startActivity( Context context, Class< ? > clazz, String key,
            Parcelable data )
    {
        Intent intent = new Intent( context, clazz );
        intent.putExtra( key, data );
        context.startActivity( intent );
    }

    public static void startActivity( Context context, Class< ? > clazz, String key,
            Serializable data )
    {
        Intent intent = new Intent( context, clazz );
        intent.putExtra( key, data );
        context.startActivity( intent );
    }

    public static void openPictureSelector( Activity activity, int requestCode )
    {
        Intent intent = new Intent( );
        if ( Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT )
        {
            intent.setAction( Intent.ACTION_GET_CONTENT );
        }
        else
        {
            intent.setAction( Intent.ACTION_OPEN_DOCUMENT );
        }
        intent.setType( "image/*" );
        intent.addCategory( Intent.CATEGORY_OPENABLE );
        activity.startActivityForResult( intent, requestCode );
    }

}
