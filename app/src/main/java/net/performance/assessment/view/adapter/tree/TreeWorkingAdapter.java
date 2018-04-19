package net.performance.assessment.view.adapter.tree;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.performance.assessment.R;
import net.performance.assessment.entity.FirstClassBean;
import net.performance.assessment.entity.SecondClassBean;
import net.performance.assessment.entity.ThirdClassBean;
import net.performance.assessment.entity.TreeWorkingBean;
import net.performance.assessment.entity.WorkingItemBean;
import net.performance.assessment.listener.SelectWorkItemClickListener;
import net.performance.assessment.listener.TreeFirstClickListener;
import net.performance.assessment.listener.TreeSecondClickListener;
import net.performance.assessment.listener.TreeThirdClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TreeWorkingAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context mContext;
    private List<TreeWorkingBean> mTreeDatas = new ArrayList<>();
    private List<FirstClassBean> mAllList = new ArrayList<>();
    private SelectWorkItemClickListener mSelectItemCallBack;

    public TreeWorkingAdapter(Context ctx, List<FirstClassBean> treeData, SelectWorkItemClickListener callBack) {
        this.mContext = ctx;
        this.mAllList = treeData;
        this.mSelectItemCallBack = callBack;
        sortAllData();
    }


    private void sortAllData() {
        try {
            List<TreeWorkingBean> firstDatas = new ArrayList<>();

            for (FirstClassBean firstClassBean : mAllList) {
                TreeWorkingBean firstTreeBean = new TreeWorkingBean();

                firstTreeBean.type = TreeWorkingBean.ITEM_TYPE_FIRST;
                firstTreeBean.id = firstClassBean.fristClass;
                firstTreeBean.name = firstClassBean.fristClassName;
                firstTreeBean.treeDepth = 0;
                firstDatas.add(firstTreeBean);
            }
            Collections.sort(firstDatas);
            mTreeDatas.addAll(firstDatas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TreeWorkingBean.ITEM_TYPE_FIRST:
                view = LayoutInflater.from(mContext).inflate(
                        R.layout.item_recycler_parent, parent, false);
                return new ParentViewHolder(view);

            case TreeWorkingBean.ITEM_TYPE_SECOND:
                view = LayoutInflater.from(mContext).inflate(
                        R.layout.item_recycler_parent, parent, false);
                return new SecondViewHolder(view);

            case TreeWorkingBean.ITEM_TYPE_THIRD:
                view = LayoutInflater.from(mContext).inflate(
                        R.layout.item_recycler_parent, parent, false);
                return new ThirdViewHolder(view);

            case TreeWorkingBean.ITEM_TYPE_WORKING:
                view = LayoutInflater.from(mContext).inflate(
                        R.layout.item_recycler_child, parent, false);
                return new ChildViewHolder(view, mSelectItemCallBack);

            default:
                view = LayoutInflater.from(mContext).inflate(
                        R.layout.item_recycler_child, parent, false);
                return new ChildViewHolder(view, mSelectItemCallBack);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TreeWorkingBean.ITEM_TYPE_FIRST:
                ParentViewHolder parentViewHolder = (ParentViewHolder) holder;
                parentViewHolder.bindView(mTreeDatas.get(position), position, firstClickListener);
                break;

            case TreeWorkingBean.ITEM_TYPE_SECOND:
                SecondViewHolder secondViewHolder = (SecondViewHolder) holder;
                secondViewHolder.bindView(mTreeDatas.get(position), position, secondClickListener);
                break;

            case TreeWorkingBean.ITEM_TYPE_THIRD:
                ThirdViewHolder thirdViewHolder = (ThirdViewHolder) holder;
                thirdViewHolder.bindView(mTreeDatas.get(position), position, thirdClickListener);
                break;

            case TreeWorkingBean.ITEM_TYPE_WORKING:
                ChildViewHolder childViewHolder = (ChildViewHolder) holder;
                childViewHolder.bindView(mTreeDatas.get(position), position);
                break;

            default:
                break;
        }
    }

    private TreeFirstClickListener firstClickListener = new TreeFirstClickListener() {
        @Override
        public void onExpandThirdChildren(TreeWorkingBean workingBean) {
            int position = getCurrentFirstPosition(workingBean.id);
            List<SecondClassBean> secondList = getSecondBeans(workingBean.id);
            if (secondList == null) {
                return;
            }
            List<TreeWorkingBean> secondData = new ArrayList<>();
            for (SecondClassBean classBean : secondList) {
                TreeWorkingBean bean = new TreeWorkingBean();
                bean.type = TreeWorkingBean.ITEM_TYPE_SECOND;
                bean.id = classBean.secondClass;
                bean.name = classBean.secondClassName;
                bean.firstId = classBean.firstId;
                bean.firstName = classBean.firstName;
                bean.treeDepth = 1;
                secondData.add(bean);
            }
            Collections.sort(secondData);
            addAll(secondData, position + 1);
            workingBean.childrenData = secondData;
        }

        @Override
        public void onHideThirdChildren(TreeWorkingBean workingBean) {
            int position = getCurrentFirstPosition(workingBean.id);
            List<TreeWorkingBean> children = workingBean.childrenData;
            if (children == null) {
                return;
            }
            removeAll(position + 1, getChildrenCount(workingBean) - 1);
            workingBean.childrenData = null;
        }
    };

    private TreeSecondClickListener secondClickListener = new TreeSecondClickListener() {
        @Override
        public void onExpandThirdChildren(TreeWorkingBean workingBean) {
            int position = getCurrentFirstPosition(workingBean.id);
            List<ThirdClassBean> thirdList = getThirdBeans(workingBean.firstId, workingBean.id);
            if (thirdList == null) {
                return;
            }
            List<TreeWorkingBean> secondData = new ArrayList<>();
            for (ThirdClassBean thirdBean : thirdList) {
                TreeWorkingBean bean = new TreeWorkingBean();
                bean.type = TreeWorkingBean.ITEM_TYPE_THIRD;
                bean.id = thirdBean.thirdClass;
                bean.name = thirdBean.thirdClassName;
                bean.firstId = thirdBean.firstId;
                bean.firstName = thirdBean.firstName;
                bean.secondId = thirdBean.secondId;
                bean.secondName = thirdBean.secondName;
                bean.treeDepth = 2;
                secondData.add(bean);
            }
            Collections.sort(secondData);
            addAll(secondData, position + 1);
            workingBean.childrenData = secondData;
        }

        @Override
        public void onHideThirdChildren(TreeWorkingBean workingBean) {
            int position = getCurrentFirstPosition(workingBean.id);
            List<TreeWorkingBean> children = workingBean.childrenData;
            if (children == null) {
                return;
            }
            removeAll(position + 1, getChildrenCount(workingBean) - 1);
            workingBean.childrenData = null;
        }
    };

    private TreeThirdClickListener thirdClickListener = new TreeThirdClickListener() {
        @Override
        public void onExpandThirdChildren(TreeWorkingBean workingBean) {
            int position = getCurrentFirstPosition(workingBean.id);
            List<WorkingItemBean> itemList = getWorkBeans(workingBean.firstId, workingBean.secondId, workingBean.id);
            if (itemList == null) {
                return;
            }
            List<TreeWorkingBean> secondData = new ArrayList<>();
            for (WorkingItemBean itemBean : itemList) {
                TreeWorkingBean bean = new TreeWorkingBean();
                bean.type = TreeWorkingBean.ITEM_TYPE_WORKING;
                bean.id = itemBean.groupIds;
                bean.name = itemBean.workingItemName;
                bean.firstId = itemBean.firstId;
                bean.firstName = itemBean.firstName;
                bean.secondId = itemBean.secondId;
                bean.secondName = itemBean.secondName;
                bean.thirdId = itemBean.thirdId;
                bean.thirdName = itemBean.thirdName;
                bean.groupIds = itemBean.groupIds;
                bean.groups = itemBean.groups;
                bean.standardScore = itemBean.standardScore;
                bean.frequencyTypeName = itemBean.frequencyTypeName;
                bean.qualityIntegralStandard = itemBean.qualityIntegralStandard;
                bean.treeDepth = 3;
                secondData.add(bean);
            }
            Collections.sort(secondData);
            addAll(secondData, position + 1);
            workingBean.childrenData = secondData;
        }

        @Override
        public void onHideThirdChildren(TreeWorkingBean workingBean) {
            int position = getCurrentFirstPosition(workingBean.id);
            List<TreeWorkingBean> children = workingBean.childrenData;
            if (children == null) {
                return;
            }
            removeAll(position + 1, getChildrenCount(workingBean) - 1);
            workingBean.childrenData = null;
        }
    };

    @Override
    public int getItemViewType(int position) {
        if (mTreeDatas != null && mTreeDatas.size() > 0) {
            return mTreeDatas.get(position).type;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return mTreeDatas == null && mTreeDatas.size() <= 0 ? 0 : mTreeDatas.size();
    }

    private int getCurrentFirstPosition(String firstClassId) {
        for (int i = 0; i < mTreeDatas.size(); i++) {
            if (firstClassId.equalsIgnoreCase(mTreeDatas.get(i).id)) {
                return i;
            }
        }
        return -1;
    }


    public void addAll(List<TreeWorkingBean> list, int position) {
        mTreeDatas.addAll(position, list);
        notifyItemRangeInserted(position, list.size());
    }

    protected void removeAll(int position, int itemCount) {
        for (int i = 0; i < itemCount; i++) {
            mTreeDatas.remove(position);
        }
        notifyItemRangeRemoved(position, itemCount);
    }

    private int getChildrenCount(TreeWorkingBean item) {
        List<TreeWorkingBean> list = new ArrayList<>();
        printChild(item, list);
        return list.size();
    }

    private void printChild(TreeWorkingBean item, List<TreeWorkingBean> list) {
        list.add(item);
        if (item.childrenData != null) {
            for (int i = 0; i < item.childrenData.size(); i++) {
                printChild(item.childrenData.get(i), list);
            }
        }
    }

    private List<SecondClassBean> getSecondBeans(String firstId) {
        if (mAllList == null || mAllList.size() <= 0) {
            return null;
        }
        for (FirstClassBean firstBean : mAllList) {
            if (firstBean.fristClass.equalsIgnoreCase(firstId)) {
                List<SecondClassBean> seconds = firstBean.secondClass;
                if (seconds != null && seconds.size() > 0) {
                    for (SecondClassBean second : seconds) {
                        second.firstId = firstBean.fristClass;
                        second.firstName = firstBean.fristClassName;
                    }
                }
                return seconds;
            }
        }
        return null;
    }

    private List<ThirdClassBean> getThirdBeans(String firstId, String secondId) {
        if (mAllList == null || mAllList.size() <= 0) {
            return null;
        }
        for (FirstClassBean firstBean : mAllList) {
            if (firstBean.fristClass.equalsIgnoreCase(firstId)) {
                List<SecondClassBean> seconds = firstBean.secondClass;
                if (seconds == null || seconds.size() <= 0) {
                    return null;
                }
                for (SecondClassBean secondBean : seconds) {
                    if (secondBean.secondClass.equalsIgnoreCase(secondId)) {
                        List<ThirdClassBean> thirds = secondBean.thirdClass;
                        if (thirds == null || thirds.size() <= 0) {
                            return null;
                        }
                        for (ThirdClassBean bean : thirds) {
                            bean.firstId = firstBean.fristClass;
                            bean.firstName = firstBean.fristClassName;
                            bean.secondId = secondBean.secondClass;
                            bean.secondName = secondBean.secondClassName;
                        }
                        return thirds;
                    }
                }
            }
        }
        return null;
    }

    private List<WorkingItemBean> getWorkBeans(String firstId, String secondId, String thirdId) {
        if (mAllList == null || mAllList.size() <= 0) {
            return null;
        }
        for (FirstClassBean firstBean : mAllList) {
            if (firstBean.fristClass.equalsIgnoreCase(firstId)) {
                List<SecondClassBean> seconds = firstBean.secondClass;
                if (seconds == null || seconds.size() <= 0) {
                    return null;
                }
                for (SecondClassBean secondBean : seconds) {
                    if (secondBean.secondClass.equalsIgnoreCase(secondId)) {
                        List<ThirdClassBean> thirds = secondBean.thirdClass;
                        if (thirds == null || thirds.size() <= 0) {
                            return null;
                        }
                        for (ThirdClassBean bean : thirds) {
                            if (bean.thirdClass.equalsIgnoreCase(thirdId)) {
                                List<WorkingItemBean> itemBeans = bean.workingItem;

                                if (itemBeans == null || itemBeans.size() <= 0) {
                                    return null;
                                }

                                for (WorkingItemBean itemBean : itemBeans) {
                                    itemBean.firstId = firstBean.fristClass;
                                    itemBean.firstName = firstBean.fristClassName;
                                    itemBean.secondId = secondBean.secondClass;
                                    itemBean.secondName = secondBean.secondClassName;
                                    itemBean.thirdId = bean.thirdClass;
                                    itemBean.thirdName = bean.thirdClassName;
                                }
                                return itemBeans;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 退出清除数据
     */
    public void clearAllData() {
        mAllList.clear();
        mAllList = null;
        mTreeDatas.clear();
        mTreeDatas = null;
    }
}
