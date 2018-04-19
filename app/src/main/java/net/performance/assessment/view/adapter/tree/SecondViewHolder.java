package net.performance.assessment.view.adapter.tree;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.performance.assessment.R;
import net.performance.assessment.entity.SecondClassBean;
import net.performance.assessment.entity.TreeWorkingBean;
import net.performance.assessment.listener.TreeSecondClickListener;

public class SecondViewHolder extends BaseViewHolder {

    private ImageView mIvExpand;
    private TextView mTvParentContent;
    private LinearLayout mLinearLayout;
    private int itemMargin;

    public SecondViewHolder(View parentItemView) {
        super(parentItemView);
        mIvExpand = parentItemView.findViewById(R.id.iv_parent_expand);
        mTvParentContent = parentItemView.findViewById(R.id.tv_parent_content);

        mLinearLayout = parentItemView.findViewById(R.id.ll_parent_container);

        itemMargin = itemView.getContext().getResources()
                .getDimensionPixelSize(R.dimen.dimen_10);
    }

    public void bindView(final TreeWorkingBean workingBean, final int position, final TreeSecondClickListener secondClickListener) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mIvExpand
                .getLayoutParams();
        params.leftMargin = itemMargin * workingBean.treeDepth;
        mIvExpand.setLayoutParams(params);
        mTvParentContent.setText(workingBean.name);
        if (workingBean.isExpand) {
            mIvExpand.setRotation(45);
        } else {
            mIvExpand.setRotation(0);
        }

        mLinearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (secondClickListener != null) {
                    if (workingBean.isExpand) {
                        secondClickListener.onHideThirdChildren(workingBean);
                        workingBean.isExpand = false;
                        rotationExpandIcon(mIvExpand, 45, 0);
                    } else {
                        secondClickListener.onExpandThirdChildren(workingBean);
                        workingBean.isExpand = true;
                        rotationExpandIcon(mIvExpand,0, 45);
                    }
                }

            }
        });
    }
}
