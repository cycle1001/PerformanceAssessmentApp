package net.performance.assessment.network.http;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 考勤
 */
public class AttendanceTaskAPI {

    /**
     * 查询考勤单
     * @param callback
     * @return
     */
    public static long personUnSignTask(SimpleHttpCallback callback) {
        JSONObject params = new JSONObject();
        return CommonAPI.businessPost("/personUnSignTask", params, callback);
    }


    /**
     * 外勤打卡
     * @param punchNum 单号
     * @param punchTime 打卡时间
     * @param punchLocation 打卡地点
     * @param callback
     * @return
     */
    public static long punchCardNormal(String punchNum, String punchTime, String punchLocation, SimpleHttpCallback callback) {
        JSONObject params = new JSONObject();
        try {
            params.put("punchNum", punchNum);
            params.put("punchTime", punchTime);
            params.put("punchLocation", punchLocation);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return CommonAPI.businessPost("/punchCardNormal", params, callback);
    }

    /**
     * 外勤补打卡
     * @param punchNum 单号
     * @param extraPunchTime 补打卡时间
     * @param punchReason 缺考勤原因
     * @param workPicture 工作证据图片
     * @param punchTime 打卡时间
     * @param picFormate 图片类型（jpg,png,gif）
     * @param callback
     * @return
     */
    public static long punchCardSpecial(String punchNum, String extraPunchTime, String punchReason,
                                        String workPicture, String punchTime, String picFormate, SimpleHttpCallback callback) {
        JSONObject params = new JSONObject();
        try {
            params.put("punchNum", punchNum);
            params.put("extraPunchDate", extraPunchTime);
            params.put("punchReason", punchReason);
            params.put("workPicture", workPicture);
            params.put("punchTime", punchTime);
            params.put("picFormate", picFormate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return CommonAPI.businessPost("/punchCardSpecial", params, callback);
    }

    /**
     * 当前未完成任务查询
     * @param callback
     * @return
     */
    public static long personCurrentTask(SimpleHttpCallback callback) {
        JSONObject params = new JSONObject();
        return CommonAPI.businessPost("/personCurrentTask", params, callback);
    }

    /**
     * 补卡审批查询
     * @param auditStatus 001	待审批
     *                    002	审批通过
     *                    003	审批不通过
     * @param callback
     * @return
     */
    public static long findWorkListQuery(String auditStatus, SimpleHttpCallback callback) {
        JSONObject params = new JSONObject();
        try {
            params.put("auditStatus", auditStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return CommonAPI.businessPost("/punchCardSpecialQuery", params, callback);
    }

    /**
     * 补卡申请审批
     * @param punchNum
     * @param punchType
     * @param auditStatus
     * @param callback
     * @return
     */
    public static long punchCardSpecialApprove(String punchNum, String punchType, String auditStatus, SimpleHttpCallback callback) {
        JSONObject params = new JSONObject();
        try {
            params.put("punchNum", punchNum);
            params.put("punchType", punchType);
            params.put("auditStatus", auditStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return CommonAPI.businessPost("/punchCardSpecialApprove", params, callback);
    }

}
