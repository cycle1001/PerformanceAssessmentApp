package net.performance.assessment.entity.type;

/**
 *
 */

public class OrderStatus
{
    /** 待审批 */
    public static final String APPROVE_PENDING = "001";

    /** 审批通过/任务执行中 */
    public static final String APPROVE_PASSED = "002";

    /** 审批不通过/此单失效 */
    public static final String APPROVE_FAILED= "003";

    /** 任务执行完成待考核 */
    public static final String ASSESS_PENDING = "004";

    /** 考核完成/任务单评分完成 */
    public static final String ASSESS_FINISHED = "005";
}
