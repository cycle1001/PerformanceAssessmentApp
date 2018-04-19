package net.performance.assessment.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import net.performance.assessment.R;
import net.performance.assessment.entity.GrabSingleBean;
import net.performance.assessment.entity.MyTaskBean;

public class MyTaskAdapter extends CommonAdapter<MyTaskBean> {

    @Override
    protected void setConvertView(ViewHolder holder, int position, Context context) {
        MyTaskBean myTaskBean = getItem(position);

        TextView listNum = holder.getItemView(R.id.tv_list_num);
        TextView taskNum = holder.getItemView(R.id.tv_task_num);
        TextView taskContent = holder.getItemView(R.id.tv_task_content);
        TextView taskStatus = holder.getItemView(R.id.tv_task_status);

        if (myTaskBean != null) {
            listNum.setText(String.valueOf(position + 1));
            taskNum.setText(myTaskBean.orderNum);
            taskContent.setText(myTaskBean.taskContent);
            taskStatus.setText(myTaskBean.orderStatusName);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_my_task;
    }
}
