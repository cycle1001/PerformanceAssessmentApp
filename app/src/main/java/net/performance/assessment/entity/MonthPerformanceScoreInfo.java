package net.performance.assessment.entity;

/**
 *
 */

public class MonthPerformanceScoreInfo
{

    /**
     * isNewRecord : true
     * departmentId : 14
     * nameId : 10
     * workAttitudeTime : 2018-01-12 00:00:00
     * monthTime : 201801
     * countNum : 61
     * emplyeeScore : 111
     * departMentScore : 0
     * finalEmpScore : 0
     */

    public boolean isNewRecord;
    public String departmentId;
    public String nameId;
    public String workAttitudeTime;
    public String monthTime;

    /** 任务积分 */
    public double countNum;

    /** 员工综合得分 */
    public double emplyeeScore;

    /** 部门积分 */
    public double departMentScore;

    /** 最后得分 */
    public double finalEmpScore;
}
