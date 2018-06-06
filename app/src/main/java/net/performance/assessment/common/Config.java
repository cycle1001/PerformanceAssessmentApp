package net.performance.assessment.common;

import net.performance.assessment.utils.StringUtil;

/**
 *
 */
public class Config {

    public static final boolean isDebug = false;

    public static final boolean enableCheckLeak = true;

    /* 内网调试地址 */
//    public static final String SERVER_ADDRESS= "http://jy110.51mypc.cn:19175";//"http://t185168f10.iok.la:18419";//"http://luokeqiutian.oicp.net";
    //public static final String SERVER_ADDRESS = "http://192.168.253.1:8080";
    /* 外网正式地址 */
//    public static final String SERVER_ADDRESS = "http://123.207.68.70";
    public static final String SERVER_ADDRESS = "http://119.29.94.182";

    /*登录成功后获取的token*/
    public static String sToken = "";

    /*是否使用测试数据*/
    public static final boolean isTest = false;

    public static volatile String sXinGeToken = "";

    public static boolean isLogin( )
    {
        return !StringUtil.isEmptyOrNullStr( Config.sToken );
    }

    public static void setToken( String sToken )
    {
        Config.sToken = sToken;
    }

}
