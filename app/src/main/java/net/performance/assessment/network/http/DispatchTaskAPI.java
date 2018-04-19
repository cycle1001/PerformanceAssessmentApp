package net.performance.assessment.network.http;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 派单相关的请求封装
 */
public class DispatchTaskAPI
{
    private static final String DISPATCH_TASK_URL = "http://t185168f10.iok.la:18419";

    /**
     * 派单查询
     *
     * @param dispatchNum 派发单号
     * @param receiverId 接单人
     * @param dispatchStatus 派单状态
     * @param callback
     * @return
     */
    public static long queryDispatchingTask( String dispatchNum , String receiverId , String dispatchStatus ,  SimpleHttpCallback callback )
    {
        JSONObject params = new JSONObject( );
        try
        {
            params.put( "dispatchNum" , dispatchNum );
            params.put( "receivePersonId" , receiverId );
            params.put( "dispatchStatus" , dispatchStatus );
        }
        catch ( JSONException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace( );
        }
        //return CommonAPI.businessPost( DISPATCH_TASK_URL ,"/dispatchListQuery" , params , callback );
        return CommonAPI.businessPost( "/dispatchListQuery" , params , callback );
    }

    /**
     * 任务派单提交
     *
     * @param dispatchNum 派发单号
     * @param projectType 工作项目类型
     * @param dispatchContent 派单内容
     * @param receiverId 责任人
     * @param dispatchStatus 派单状态
     * @param callback
     * @return
     */
    public static long submitDispatchingTask( String dispatchNum , String projectType , String dispatchContent ,
            String integralStandard , String receiverId , String dispatchStatus ,  SimpleHttpCallback callback )
    {
        JSONObject params = new JSONObject( );
        try
        {
            params.put( "dispatchNum" , dispatchNum );
            params.put( "workItemType" , projectType );
            params.put( "dispatchContent" , dispatchContent );

            params.put( "integralStandard" , integralStandard );
            params.put( "receivePersonId" , receiverId );
            params.put( "dispatchStatus" , dispatchStatus );
        }
        catch ( JSONException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace( );
        }
        return CommonAPI.businessPost( "/dispatchListCommit" , params , callback );
    }

    /**
     * 接单任务确认
     *
     * @param dispatchNum 派发单号
     * @param receiverId 接单人(责任人)
     * @param dispatchStatus 派单状态
     * @param callback
     * @return
     */
    public static long receiveTask( String dispatchNum, String receiverId, String dispatchStatus,
            SimpleHttpCallback callback )
    {
        JSONObject params = new JSONObject( );
        try
        {
            params.put( "dispatchNum", dispatchNum );
            params.put( "receivePersonId", receiverId );
            params.put( "dispatchStatus", dispatchStatus );
        }
        catch ( JSONException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace( );
        }
        return CommonAPI.businessPost( "/dispatchListSingleCommit", params, callback );
    }

    /**
     * 任务单实施进度
     *
     * @param dispatchNum 派发单号
     * @param finishedPercent 完成百分比（数值传1-100 例如98.23）
     * @param finishedTime 完成时间（yyyy-MM-hh hh:mm:ss）
     * @param finishedPicture 完成工作图片(base64字符串)
     * @param dispatchStatus 派单状态
     * @param picFormat  图片类型（jpg,png,gif）
     * @param callback
     * @return
     */
    public static long submitDispatchedTaskProgress( String dispatchNum, String finishedPercent , String finishedTime,
            String finishedPicture , String dispatchStatus , String picFormat , SimpleHttpCallback callback )
    {
        JSONObject params = new JSONObject( );
        try
        {
            params.put( "dispatchNum", dispatchNum );
            params.put( "finishPercent", finishedPercent );
            params.put( "finishTime", finishedTime );
            params.put( "finishPictures", finishedPicture );
            params.put( "dispatchStatus", dispatchStatus );
            params.put( "picFormate", picFormat );
        }
        catch ( JSONException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace( );
        }
        return CommonAPI.businessPost( "/dispatchScheduleCommit", params, callback );
    }

    /**
     * 任务单绩效考核
     *
     * @param dispatchNum 派发单号
     * @param qualityScore 质量考核分
     * @param additionalScore 附加得分
     * @param finalScore 最终得分
     * @param overview 综合评价
     * @param dispatchStatus 派单状态(1-10)
     * @param callback
     * @return
     */
    public static long submitDispatchedTaskAssessment( String dispatchNum, String qualityScore , String additionalScore,
            String finalScore , String overview , String dispatchStatus , SimpleHttpCallback callback )
    {
        JSONObject params = new JSONObject( );
        try
        {
            params.put( "dispatchNum", dispatchNum );
            params.put( "finishScore", qualityScore );
            params.put( "extraScore", additionalScore );
            params.put( "finalScore", finalScore );
            params.put( "overallMerit", overview );
            params.put( "dispatchStatus", dispatchStatus );
        }
        catch ( JSONException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace( );
        }
        return CommonAPI.businessPost( "/assessmentCommit", params, callback );
    }
}
