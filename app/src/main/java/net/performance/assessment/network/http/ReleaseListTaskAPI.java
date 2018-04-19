package net.performance.assessment.network.http;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 发布单相关的请求封装
 */
public class ReleaseListTaskAPI {

    /**
     * 发布单提交
     *
     * @param oddNum          发布单号
     * @param workItemType    工作项目类型
     * @param taskContent     发布单内容
     * @param taskReleaseTime 任务发布时间
     * @param callback
     * @return
     */
    public static long submitReleaseList(String oddNum, String workItemType, String taskContent, String taskReleaseTime,
            String groupIds, String scoreStandard, SimpleHttpCallback callback) {
        JSONObject params = new JSONObject();
        try {
            params.put("oddNum", oddNum);
            params.put("workItemType", workItemType);
            params.put("taskContent", taskContent);
            params.put("taskReleaseTime", taskReleaseTime);
            params.put("taskState", "001");
            params.put("groupIds", groupIds);
            params.put("scoreStandard", scoreStandard);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return CommonAPI.businessPost("/releaseListCommit", params, callback);
    }

    /**
     * 发布单查询
     *
     * @param callback
     * @return
     */
    public static long queryReleaseList(String oddNum, String workItemType, String taskState, SimpleHttpCallback callback) {
        JSONObject params = new JSONObject();
        try {
            params.put("oddNum", oddNum);
            params.put("workItemType", workItemType);
            params.put("taskState", taskState);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return CommonAPI.businessPost("/releaseListQuery", params, callback);
    }

}
