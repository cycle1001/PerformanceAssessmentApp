package net.performance.assessment.cache;

import net.performance.assessment.entity.LoginInfo;

/**
 * 登录信息缓存类
 */

public class LoginInfoCache extends BaseCache
{
    private static volatile LoginInfoCache instance;
    private LoginInfo mLoginData;

    public static LoginInfoCache getInstance( )
    {
        if ( instance == null )
        {
            synchronized ( LoginInfoCache.class )
            {
                instance = new LoginInfoCache( );
            }
        }
        return instance;
    }

    private LoginInfoCache( )
    {
        if ( mLoginData == null )
        {
            mLoginData = new LoginInfo( );
        }
    }

    public synchronized void save( LoginInfo loginInfo )
    {
        if ( mLoginData == null )
        {
            mLoginData = new LoginInfo( );
        }
        mLoginData = loginInfo;
    }

    public synchronized LoginInfo getLoginInfo( )
    {
        return mLoginData;
    }

    public void clear(){
        mLoginData = null;
    }
}
