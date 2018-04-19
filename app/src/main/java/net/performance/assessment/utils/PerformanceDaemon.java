package net.performance.assessment.utils;

/**
 *
 */

public class PerformanceDaemon
{
    private static final PerformanceDaemon ourInstance = new PerformanceDaemon( );

    public static PerformanceDaemon getInstance( )
    {
        return ourInstance;
    }

    private PerformanceDaemon( )
    {
    }
}
