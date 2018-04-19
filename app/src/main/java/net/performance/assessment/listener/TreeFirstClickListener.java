package net.performance.assessment.listener;

import net.performance.assessment.entity.TreeWorkingBean;

public interface TreeFirstClickListener {

    /**
     * 展开列表
     * @param workingBean 实体
     */
    void onExpandThirdChildren(TreeWorkingBean workingBean);

    /**
     * 收缩列表
     * @param workingBean 实体
     */
    void onHideThirdChildren(TreeWorkingBean workingBean);

}
