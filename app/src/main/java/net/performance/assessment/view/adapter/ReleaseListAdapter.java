package net.performance.assessment.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.TextView;

import net.performance.assessment.R;
import net.performance.assessment.entity.ReleaseListBean;

public class ReleaseListAdapter extends CommonAdapter<ReleaseListBean> {

    @Override
    protected void setConvertView(ViewHolder holder, int position, Context context) {
        ReleaseListBean releaseBean = getItem(position);

        TextView listNum = holder.getItemView(R.id.tv_list_num);
        TextView oddNum = holder.getItemView(R.id.tv_release_num);
        TextView taskContent = holder.getItemView(R.id.tv_release_content);
        TextView releaseName = holder.getItemView(R.id.tv_release_name);
        TextView releaseTime = holder.getItemView(R.id.tv_release_time);

        if (releaseBean != null) {
            listNum.setText(String.valueOf(position + 1));
            oddNum.setText(releaseBean.oddNum);
            String workItemType = releaseBean.workItemTypeName;
            if (!TextUtils.isEmpty(workItemType)) {
                workItemType = workItemType.replaceAll("\\|", "-");
            }
            String taskContentStr = releaseBean.taskContent;
            if (!TextUtils.isEmpty(taskContentStr)) {
                taskContentStr = workItemType + "，" + taskContentStr;
            } else {
                taskContentStr = workItemType;
            }
            taskContent.setText(taskContentStr);
            releaseName.setText(releaseBean.oddUserName);
            releaseTime.setText(releaseBean.taskReleaseTime);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_order_list;
    }
}
