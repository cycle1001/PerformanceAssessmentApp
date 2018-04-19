package net.performance.assessment.network.http;

import net.performance.assessment.common.Config;
import net.performance.assessment.interfaces.HttpCallback;
import net.performance.assessment.utils.HttpUtils;
import net.performance.assessment.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 常用业务请求封装
 */
public class CommonAPI {
    private static final String URL_DIRECTORY = "/appjx/app/api";
    //private static final String URL_DIRECTORY = "/IFaceSuportService/iface/card";
    public final static String QUERY = "?token=%1$s";

    public static long businessPost(String action, JSONObject jsonParams, String requestTag, HttpCallback callback) {
        StringBuilder builder = new StringBuilder();
        builder.append(Config.SERVER_ADDRESS);
        builder.append(URL_DIRECTORY);
        builder.append(action);

        //builder.append( String.format( QUERY , Config.sToken ) );

        String jsonStr = jsonParams.toString();

        return HttpUtils.getInstance().asyncJsonPost(builder.toString(), jsonStr, requestTag, callback);
    }

    public static long businessPost(String serverAddress, String action, JSONObject jsonParams, SimpleHttpCallback callback) {
        StringBuilder builder = new StringBuilder();
        builder.append(serverAddress);
        builder.append(URL_DIRECTORY);
        builder.append(action);

        if (!StringUtil.isEmptyOrNullStr(Config.sToken)) {
            builder.append(String.format(QUERY, Config.sToken));
        }

        String jsonStr = jsonParams.toString();

        return HttpUtils.getInstance().asyncJsonPost(builder.toString(), jsonStr, callback);
    }

    public static long businessPost(String action, JSONObject jsonParams, SimpleHttpCallback callback) {
        StringBuilder builder = new StringBuilder();
        builder.append(Config.SERVER_ADDRESS);
        builder.append(URL_DIRECTORY);
        builder.append(action);

        if (!StringUtil.isEmptyOrNullStr(Config.sToken)) {
            builder.append(String.format(QUERY, Config.sToken));
        }

        String jsonStr = jsonParams.toString();

        return HttpUtils.getInstance().asyncJsonPost(builder.toString(), jsonStr, callback);
    }

    public static long test(String account, String password, HttpCallback callback) {
        JSONObject params = new JSONObject();
        return businessPost("/queryStudentData", params, password, callback);
    }

    /**
     * 注册
     *
     * @param account  用户名
     * @param password 密码
     * @param callback
     * @return
     */
    public static long regist(String account, String password, SimpleHttpCallback callback) {
        JSONObject params = new JSONObject();
        try {
            params.put("Name", account);
            params.put("NewPassword", password);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return businessPost("/userRegist", params, callback);
    }

    /**
     * 登录
     *
     * @param account  用户名
     * @param password 密码
     * @param callback
     * @return
     */
    public static long login(String account, String password,  String xingeToken , SimpleHttpCallback callback) {
        JSONObject params = new JSONObject();
        try {
            params.put("name", account);
            params.put("newPassword", password);
            params.put("xingeToken", xingeToken);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return businessPost("/userLogin", params, callback);
    }

    /**
     * 获取用户app菜单
     *
     * @param callback
     * @return
     */
    public static long getUserMenu( SimpleHttpCallback callback) {
        JSONObject params = new JSONObject();

        return businessPost("/getUserAppMenu", params, callback);
    }

    /**
     * 当前未完成任务查询
     *
     * @param callback
     * @return
     */
    public static long queryUnfinishedTask( SimpleHttpCallback callback) {
        JSONObject params = new JSONObject();

        return businessPost("/personCurrentTask", params, callback);
    }

    /**
     * 工单查询
     *
     * @param beginTime 发布单/派单开始查询日期（yyyy-MM-dd）
     * @param endTime 发布单/派单结束查询日期（yyyy-MM-dd）
     * @param callback
     * @return
     */
    public static long queryWorkOrder( String beginTime, String endTime ,SimpleHttpCallback callback) {
        JSONObject params = new JSONObject();
        try {
            params.put("beginTime", beginTime );
            params.put("endTime", endTime );
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return businessPost("/workListQuery", params, callback);
    }

    /**
     * 任务积分查询
     *
     * @param startDate
     * @param endDate
     * @param callback
     * @return
     */
    public static long queryTaskScore(String startDate, String endDate, SimpleHttpCallback callback) {
        JSONObject params = new JSONObject();
        try {
            params.put("startConditionTime", startDate );
            params.put("endConditionTime", endDate );
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return businessPost("/personalPointsQuery", params, callback);
    }

    /**
     * 员工月绩效查询
     *
     * @param time
     * @param callback
     * @return
     */
    public static long queryMonthPerformanceScore( String time ,SimpleHttpCallback callback) {
        JSONObject params = new JSONObject();
        try {
            params.put("monthTime", time );
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return businessPost("/empMonthPerform", params, callback);
    }

    /**
     * 人员信息查询
     *
     * @param companyId 公司id
     * @param officeId  部门id
     * @param callback
     * @return
     */
    public static long findUsers(String companyId, String officeId, SimpleHttpCallback callback) {
        JSONObject params = new JSONObject();
        try {
            params.put("companyId", companyId);
            params.put("officeId", officeId);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return businessPost("/findUsers", params, callback);
    }

    /**
     * 获取工作项目类型
     *
     * @param firstClassWork  一级工作
     * @param secondClassWork 二级工作
     * @param thirdClassWork  三级工作
     * @param callback
     * @return
     */
    public static long findWorkType(String firstClassWork, String secondClassWork,
                                    String thirdClassWork, SimpleHttpCallback callback) {
        JSONObject params = new JSONObject();
        try {
            params.put("firstClassWork", firstClassWork);
            params.put("secondClassWork", secondClassWork);
            params.put("thirdClassWork", thirdClassWork);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return businessPost("/findWorkType", params, callback);
    }

    /**
     * 密码修改
     *
     * @param oldPassword
     * @param newPassword
     * @param callback
     * @return
     */
    public static long modifyPassword(String oldPassword, String newPassword, SimpleHttpCallback callback) {
        JSONObject params = new JSONObject();
        try {
            params.put("oldPassword", oldPassword);
            params.put("newPassword", newPassword);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return businessPost("/modifyPassword", params, callback);
    }

    /**
     * 获取工作项目类型
     * @param callback
     * @return
     */
    public static long getWorkingItem(SimpleHttpCallback callback) {
        JSONObject params = new JSONObject();
        try {
            params.put("firstClassWork", "");
            params.put("secondClassWork", "");
            params.put("thirdClassWork", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return businessPost("/findWorkType", params, callback);
    }
}
