package net.performance.assessment.entity;

import java.util.ArrayList;
import java.util.List;

public class FirstClassBean {

    public String fristClass;

    public String fristClassName;

    public List<SecondClassBean> secondClass = new ArrayList<>();

    // 是否展开
    public boolean isExpand = false;

    // 路径的深度
    public int treeDepth = 0;

}
