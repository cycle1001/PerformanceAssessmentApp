package net.performance.assessment.utils;

import com.google.gson.Gson;

/**
 *
 */

public class JsonParser
{

    private Gson mGson;

    private volatile  static JsonParser instance;

    private JsonParser( )
    {
        mGson = new Gson( );
    }

    public static JsonParser getInstance( )
    {
        if ( instance == null ) {
            synchronized ( HttpUtils.class )
            {
                if ( instance == null )
                {
                    instance = new JsonParser( );
                }
            }
        }
        return instance;
    }

    /**
     * @param <T>
     * @Title: getServerBean
     * @Description: 将一个json字符串转换成对象
     * @param jsonStr
     *            json字符串
     * @param cls
     *            需要转换成的类
     * @return
     */
    public < T > T getBeanFromJsonString( String jsonStr , Class< T > cls )
    {
        T obj = null;
        try
        {
            obj = mGson.fromJson( jsonStr , cls );
        }
        catch ( Exception e )
        {
            e.printStackTrace( );
        }
        return obj;
    }

    /**
     * @Title: getStringFromJsonObject
     * @Description: 将一个object序列化为json字符串
     * @param obj
     * @return
     */
    public String getJsonStringFromObject( Object obj )
    {
        String jsonStr = "";
        try
        {
            jsonStr = mGson.toJson( obj );
        }
        catch ( Exception e )
        {
            e.printStackTrace( );
        }
        return jsonStr;
    }
}
