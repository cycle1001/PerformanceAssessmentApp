package net.performance.assessment.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import net.performance.assessment.R;
import net.performance.assessment.entity.CardApprovalInfo;
import net.performance.assessment.entity.GrabSingleBean;

public class CardApprovalAdapter extends CommonAdapter<CardApprovalInfo> {

    @Override
    protected void setConvertView(ViewHolder holder, int position, Context context) {
        CardApprovalInfo info = getItem(position);

        TextView listNum = holder.getItemView(R.id.tv_list_num);
        TextView punchNum = holder.getItemView(R.id.tv_punch_num);
        TextView punchName = holder.getItemView(R.id.tv_punch_name);
        TextView punchTime = holder.getItemView(R.id.tv_punch_time);

        if (info != null) {
            listNum.setText(String.valueOf(position + 1));
            punchNum.setText(info.punchNum);
            punchName.setText(info.punchPersonName);
            punchTime.setText(info.punchTime);
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_card_approved;
    }
}
