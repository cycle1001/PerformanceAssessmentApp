package net.performance.assessment.entity;

/**
 *
 */

public class BaseResultBean
{
    public String status;

    public String message;

    public String errorCode;

    public boolean isSuccess( )
    {
        return status.equals( "200" );
    }
}
