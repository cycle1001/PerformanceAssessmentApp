package net.performance.assessment.view.adapter.tree;


import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.performance.assessment.R;
import net.performance.assessment.entity.TreeWorkingBean;
import net.performance.assessment.listener.SelectWorkItemClickListener;

public class ChildViewHolder extends BaseViewHolder {

    private TextView mTvChildContent;
    private LinearLayout mLinearLayout;
    private int mItemMargin;
    private int mOffsetMatgrin;
    private SelectWorkItemClickListener mSelectCallBack;

    public ChildViewHolder(View childItemView, SelectWorkItemClickListener itemCallBack) {
        super(childItemView);
        this.mSelectCallBack = itemCallBack;
        mTvChildContent = childItemView.findViewById(R.id.tv_child_content);
        mLinearLayout = childItemView.findViewById(R.id.ll_child_container);
        mItemMargin = itemView.getContext().getResources()
                .getDimensionPixelSize(R.dimen.dimen_10);
        mOffsetMatgrin = itemView.getContext().getResources()
                .getDimensionPixelSize(R.dimen.dimen_10);
    }

    public void bindView(final TreeWorkingBean treeWorkingBean, int position) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTvChildContent
                .getLayoutParams();
        params.leftMargin = mItemMargin * treeWorkingBean.treeDepth + mOffsetMatgrin;
        String text = treeWorkingBean.groups + "-" + treeWorkingBean.name;
        mTvChildContent.setText(text);
        mLinearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mSelectCallBack.onWorkItemSelectCallBack(treeWorkingBean);
            }
        });
    }
}
