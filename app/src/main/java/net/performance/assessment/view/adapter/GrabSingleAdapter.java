package net.performance.assessment.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import net.performance.assessment.R;
import net.performance.assessment.entity.GrabSingleBean;
import net.performance.assessment.entity.ReleaseListBean;

public class GrabSingleAdapter extends CommonAdapter<GrabSingleBean> {

    @Override
    protected void setConvertView(ViewHolder holder, int position, Context context) {
        GrabSingleBean grabBean = getItem(position);

        TextView listNum = holder.getItemView(R.id.tv_list_num);
        TextView oddNum = holder.getItemView(R.id.tv_release_num);
        TextView taskContent = holder.getItemView(R.id.tv_release_content);
        TextView grabName = holder.getItemView(R.id.tv_grab_name);
        TextView grabTime = holder.getItemView(R.id.tv_grab_time);
        TextView grabLocation = holder.getItemView(R.id.tv_grab_location);

        if (grabBean != null) {
            listNum.setText(String.valueOf(position + 1));
            oddNum.setText(grabBean.oddNum);
            String workItemType = grabBean.workItemTypeName;
            if (!TextUtils.isEmpty(workItemType)) {
                workItemType = workItemType.replaceAll("\\|", "-");
            }
            String taskContentStr = grabBean.taskContent;
            if (!TextUtils.isEmpty(taskContentStr)) {
                taskContentStr = workItemType + "，" + taskContentStr;
            } else {
                taskContentStr = workItemType;
            }
            taskContent.setText(taskContentStr);
            grabName.setText(grabBean.receivePersonName);
            grabTime.setText(grabBean.receiveTime);
            grabLocation.setText(grabBean.receiveLocation);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_grab_single_approved;
    }
}
