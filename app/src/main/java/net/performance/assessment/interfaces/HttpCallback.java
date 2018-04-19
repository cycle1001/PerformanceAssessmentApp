package net.performance.assessment.interfaces;

/**
 *
 */

public interface HttpCallback
{

    /**
     * Http响应成功回调
     *
     * @param result
     */
    void onHttpResponseSuccess( String result , long flag );

    /**
     * Http响应失败回调( 网络断开 )
     *
     * @param error
     */
    void onHttpResponseFailure( int error , long flag );

    /**
     * Http响应超时回调( 网络连接超时或者读取超时 )
     */
    void onHttpResponseTimeout( long flag );

}
