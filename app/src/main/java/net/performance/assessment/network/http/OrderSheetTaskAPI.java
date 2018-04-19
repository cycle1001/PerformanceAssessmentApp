package net.performance.assessment.network.http;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 抢单相关的请求封装
 */
public class OrderSheetTaskAPI {

    /**
     * 抢单提交
     * @param oddNum 发布单号
     * @param receiveTime 抢单时间
     * @param receiveLocation 抢单位置
     * @param callback
     * @return
     */
    public static long orderSheetCommit(String oddNum, String receiveTime, String receiveLocation, SimpleHttpCallback callback) {
        JSONObject params = new JSONObject();
        try {
            params.put("oddNum", oddNum);
            params.put("receiveTime", receiveTime);
            params.put("receiveLocation", receiveLocation);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return CommonAPI.businessPost("/orderSheetCommit", params, callback);
    }

    /**
     * 抢单信息查询
     * @param orderNum 抢单号
     * @param oddNum 发布单号
     * @param orderStatus 单据状态
     * @param callback
     * @return
     */
    public static long orderSheetQuery(String orderNum, String oddNum, String orderStatus, SimpleHttpCallback callback) {
        JSONObject params = new JSONObject();
        try {
            params.put("orderNum", orderNum);
            params.put("oddNum", oddNum);
            params.put("orderStatus", orderStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return CommonAPI.businessPost("/orderSheetQuery", params, callback);
    }


    /**
     * 抢单审批
     *
     * @param orderNum     抢单号(抢单号通过抢单信息查询获取)
     * @param auditTime    审批时间（yyyy-MM-hh hh:mm:ss）
     * @param auditOpinion 审批意见
     * @param callback
     * @return
     */
    public static long approveOrderSheet(String orderNum, String auditTime, String auditOpinion, SimpleHttpCallback callback) {
        JSONObject params = new JSONObject();
        try {
            params.put("orderNum", orderNum);
            params.put("auditTime", auditTime);
            params.put("auditOpinion", auditOpinion);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return CommonAPI.businessPost("/orderSheetApprove", params, callback);
    }

    /**
     * 抢单进度提交
     *
     * @param orderNum     抢单号(抢单号通过抢单信息查询获取)
     * @param receiveUnionPersonIds    参与人(如有多个参与人 用“|”分割传人员id串)
     * @param finishPercent 完成百分比（数值传1-100 例如98.23）
     * @param finishTime    完成时间（yyyy-MM-hh hh:mm:ss）
     * @param finishPictures  完成工作图片(base64字符串)
     * @param callback
     * @return
     */
    public static long submitOrderProgress(String orderNum, String receiveUnionPersonIds, String finishPercent,
                                           String finishTime, String finishPictures, String picFormat, SimpleHttpCallback callback) {
        JSONObject params = new JSONObject();
        try {
            params.put("orderNum", orderNum);
            params.put("receiveUnionPersonIds", receiveUnionPersonIds);
            params.put("finishPercent", finishPercent);
            params.put("finishTime", finishTime);
            params.put("finishPictures", finishPictures);
            params.put("picFormate", picFormat);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return CommonAPI.businessPost("/orderSheetProgressCommit", params, callback);
    }

    /**
     * 抢单绩效考核
     *
     * @param orderNum     抢单号(抢单号通过抢单信息查询获取)
     * @param finishScore    质量考核
     * @param extraScore   附加得分
     * @param finalScore    最终得分
     * @param overallMerit  工作完成综合评价
     * @param callback
     * @return
     */
    public static long orderAssessment(String orderNum, String finishScore, String extraScore,
                                           String finalScore, String overallMerit, SimpleHttpCallback callback) {
        JSONObject params = new JSONObject();
        try {
            params.put("orderNum", orderNum);
            params.put("finishScore", finishScore);
            params.put("extraScore", extraScore);
            params.put("finalScore", finalScore);
            params.put("overallMerit", overallMerit);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return CommonAPI.businessPost("/orderSheetAssessment", params, callback);
    }

}
