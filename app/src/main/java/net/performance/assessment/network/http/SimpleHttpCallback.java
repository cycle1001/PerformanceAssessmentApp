package net.performance.assessment.network.http;


import net.performance.assessment.interfaces.HttpCallback;

/**
 * 专用于Activity和Fragment的Http请求
 */
public class SimpleHttpCallback implements HttpCallback
{
    private Object requestTag;

    @Override
    public void onHttpResponseSuccess( String result, long flag )
    {

    }

    @Override
    public void onHttpResponseFailure( int error, long flag )
    {

    }

    @Override
    public void onHttpResponseTimeout( long flag )
    {

    }

    public Object getRequestTag( )
    {
        return requestTag;
    }

    public void setRequestTag( Object requestTag )
    {
        this.requestTag = requestTag;
    }
}
