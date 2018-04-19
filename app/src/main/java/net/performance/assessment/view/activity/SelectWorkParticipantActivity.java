package net.performance.assessment.view.activity;

import net.performance.assessment.R;

/**
 * 选择参与人
 */
public class SelectWorkParticipantActivity extends BaseActivity {

    @Override
    protected void setContentView() {
        super.setContentView();
        setContentView(R.layout.activity_select);
    }

    @Override
    protected void initView() {
        super.initView();
        mCustomTitleBar.setTitle(getString(R.string.select_participant));
    }
}
