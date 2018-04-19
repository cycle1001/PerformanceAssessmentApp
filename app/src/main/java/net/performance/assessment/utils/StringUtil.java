package net.performance.assessment.utils;

import android.text.TextUtils;

/**
 *
 */

public class StringUtil
{
    /**
     * 判断字符串是否为空字符串、null或“null”字符串包括所有大小写情况
     *
     * @param str
     * @return 是否为空
     */
    public static boolean isEmptyOrNullStr( String str )
    {
        return TextUtils.isEmpty( str ) || "".equals( str ) || str == null || "null".equals( str );
    }

}
