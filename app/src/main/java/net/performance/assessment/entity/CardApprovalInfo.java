package net.performance.assessment.entity;

import java.io.Serializable;

public class CardApprovalInfo implements Serializable {

    public String id;

    public boolean isNewRecord;

    public String remarks;
    // 打卡单号
    public String punchNum;
    // 打卡类型（正常/补打）
    public String punchType;
    // 打卡人
    public String punchPersonId;

    public String punchPersonName;

    // 打卡时间
    public String punchTime;
    // 打卡地点
    public String punchLocation;
    // 工作证据图片
    public String workPicture;
    // 单据状态
    public String auditStatus;
    // 审核人
    public String auditor;
    // 审核时间
    public String auditTime;
    // 审核意见
    public String auditOpinion;
    // 开始打卡时间
    public String beginPunchTime;
    // 结束打卡时间
    public String endPunchTime;
    // 上班时间
    public String workDate;
    // 下班时间
    public String workOffDate;
    // 缺考勤原因
    public String punchReason;
    // 补打卡时间
    public String extraPunchDate;
    // 图片类型
    public String picFormate;
    //
    public String punchPersonid;

}
