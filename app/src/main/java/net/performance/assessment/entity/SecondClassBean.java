package net.performance.assessment.entity;

import java.util.ArrayList;
import java.util.List;

public class SecondClassBean {

    public String secondClass;

    public String secondClassName;

    public String firstId;

    public String firstName;

    public List<ThirdClassBean> thirdClass = new ArrayList<>();

    // 是否展开
    public boolean isExpand = false;

    // 路径的深度
    public int treeDepth = 1;

}
