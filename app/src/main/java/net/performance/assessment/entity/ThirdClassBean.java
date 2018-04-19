package net.performance.assessment.entity;

import java.util.ArrayList;
import java.util.List;

public class ThirdClassBean {

    public String thirdClass;

    public String thirdClassName;

    public String firstId;

    public String firstName;

    public String secondId;

    public String secondName;

    public List<WorkingItemBean> workingItem = new ArrayList<>();

    // 是否展开
    public boolean isExpand = false;

    // 路径的深度
    public int treeDepth = 2;

}
