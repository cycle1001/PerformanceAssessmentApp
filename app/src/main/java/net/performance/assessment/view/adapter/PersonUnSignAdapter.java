package net.performance.assessment.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import net.performance.assessment.R;
import net.performance.assessment.entity.GrabSingleBean;
import net.performance.assessment.entity.PersonUnSignBean;

public class PersonUnSignAdapter extends CommonAdapter<PersonUnSignBean> {

    @Override
    protected void setConvertView(ViewHolder holder, int position, Context context) {
        PersonUnSignBean unSignBean = getItem(position);

        TextView listNum = holder.getItemView(R.id.tv_list_num);
        TextView oddNum = holder.getItemView(R.id.tv_order_num);
        TextView workItemType = holder.getItemView(R.id.tv_work_type);
        TextView taskContent = holder.getItemView(R.id.tv_work_content);

        if (unSignBean != null) {
            listNum.setText(String.valueOf(position + 1));
            oddNum.setText(unSignBean.orderNum);
            String workItemTypeStr = unSignBean.workItemTypeName;
            if (!TextUtils.isEmpty(workItemTypeStr)) {
                workItemTypeStr = workItemTypeStr.replaceAll("\\|", "-");
            }
            workItemType.setText(workItemTypeStr);
            taskContent.setText(unSignBean.taskContent);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_person_unsign;
    }
}
