package net.performance.assessment.entity;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class TreeWorkingBean implements Comparable<TreeWorkingBean>, Serializable {

    public static final int ITEM_TYPE_FIRST = 0;

    public static final int ITEM_TYPE_SECOND = 1;

    public static final int ITEM_TYPE_THIRD = 2;

    public static final int ITEM_TYPE_WORKING = 3;

    public int type;

    public String id;

    public String name;

    public boolean isExpand = false;

    public int treeDepth;

    public String firstId;

    public String firstName;

    public String secondId;

    public String secondName;

    public String thirdId;

    public String thirdName;

    public String groups;

    public String groupIds;

    public String standardScore;

    public String frequencyTypeName;

    public String qualityIntegralStandard;

    public List<TreeWorkingBean> childrenData;

    @Override
    public int compareTo(@NonNull TreeWorkingBean o) {
        return 0;
    }
}
