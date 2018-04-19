package net.performance.assessment.entity;

import java.io.Serializable;

/**
 *
 */

public class DispatchTaskInfo implements Serializable
{

    /**
     * id : afa351eba7264e29b2326e0a2fde083b
     * isNewRecord : false
     * createDate : 2017-12-14 16:56:51
     * updateDate : 2017-12-14 16:56:51
     * dispatchNum : NO000000120171214165651
     * workItemType : 001|251|03999
     * dispatchContent : 1|1|工作票执行情况
     * dispatchTime : 2017-12-14 16:56:51
     * receivePersonId : 1,2,
     * finishPercent : 13
     * finishTime : 2017-12-14 16:56:51
     * finishScore : 12
     * extraScore : 222
     * scoreTime : 2017-11-14 17:30:08
     * dispatchStatus : 004
     * finalScore : 12
     * fieldPunchStatus : 0
     * workItemTypeName : 日常工作|优质服务|其他
     * dispatchPersonName : 10
     * dispatchStatusName : 派单考核完成
     */

    public String id;
    public boolean isNewRecord;
    public String createDate;
    public String updateDate;
    public String dispatchNum;
    public String workItemType;
    public String dispatchContent;
    public String dispatchTime;
    public String receivePersonId;
    public String receivePersonName;
    public String finishPictures;
    public double finishPercent;
    public String finishTime;
    public double finishScore;
    public double extraScore;
    public String scoreTime;
    public String dispatchStatus;
    public double finalScore;
    public String fieldPunchStatus;
    public String workItemTypeName;
    public String dispatchPersonName;
    public String dispatchStatusName;

    public String integralStandard;
    public String dispatchPerson;
}
