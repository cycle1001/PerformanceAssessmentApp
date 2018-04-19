package net.performance.assessment.utils;

import android.content.Context;

/**
 * 获取资源的工具类
 */
public class ResourceUtils
{
    public static int getLayoutId( Context paramContext, String paramString )
    {
        return paramContext.getResources( ).getIdentifier( paramString, "layout",
                paramContext.getPackageName( ) );
    }

    public static int getStringId( Context paramContext, String paramString )
    {
        return paramContext.getResources( ).getIdentifier( paramString, "string",
                paramContext.getPackageName( ) );
    }

    public static int getDrawableId( Context paramContext, String paramString )
    {
        return paramContext.getResources( ).getIdentifier( paramString, "drawable",
                paramContext.getPackageName( ) );
    }

    public static int getStyleId( Context paramContext, String paramString )
    {
        return paramContext.getResources( ).getIdentifier( paramString, "style",
                paramContext.getPackageName( ) );
    }

    public static int getId( Context paramContext, String paramString )
    {
        return paramContext.getResources( ).getIdentifier( paramString, "id",
                paramContext.getPackageName( ) );
    }

    public static int getColorId( Context paramContext, String paramString )
    {
        return paramContext.getResources( ).getIdentifier( paramString, "color",
                paramContext.getPackageName( ) );
    }
}
