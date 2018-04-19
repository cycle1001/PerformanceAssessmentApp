package net.performance.assessment.view.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.performance.assessment.R;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.TreeWorkingBean;
import net.performance.assessment.entity.WorkingItemListBean;
import net.performance.assessment.listener.SelectWorkItemClickListener;
import net.performance.assessment.network.http.CommonAPI;
import net.performance.assessment.utils.JsonParser;
import net.performance.assessment.view.adapter.tree.TreeWorkingAdapter;
import net.performance.assessment.view.widget.LoadingDialog;

/**
 * 工作项目类型选择
 */
public class SelectWorkItemTypeActivity extends BaseActivity implements SelectWorkItemClickListener {

    private RecyclerView recyclerView;
    private TreeWorkingAdapter myAdapter;
    private LinearLayoutManager linearLayoutManager;
    private WorkingItemListBean listBean;

    private long workFlag;

    @Override
    protected void setContentView() {
        super.setContentView();
        setContentView(R.layout.activity_select);
    }

    @Override
    protected void initView() {
        super.initView();
        mCustomTitleBar.setTitle(getString(R.string.select_work_item_type));

        recyclerView = findViewById(R.id.rv_select);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.getItemAnimator().setAddDuration(100);
        recyclerView.getItemAnimator().setRemoveDuration(100);
        recyclerView.getItemAnimator().setMoveDuration(200);
        recyclerView.getItemAnimator().setChangeDuration(100);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.custom_divider));
        recyclerView.addItemDecoration(divider);
    }

    @Override
    protected void initData() {
        super.initData();
        getWorkingItem();
    }

    private void getWorkingItem() {
        mLoadingDialog = new LoadingDialog(this);
        mLoadingDialog.show();
        workFlag = CommonAPI.getWorkingItem(mHttpCallback);
    }

    @Override
    protected void handleResponseSuccess(String result, long flag) {
        super.handleResponseSuccess(result, flag);
        BaseResultBean detectedBean = JsonParser.getInstance( ).getBeanFromJsonString( result , BaseResultBean.class );
        if (detectedBean.isSuccess()) {
            if (workFlag == flag) {
                listBean = JsonParser.getInstance( ).getBeanFromJsonString( result , WorkingItemListBean.class );
                if (listBean.data != null && listBean.data.size() > 0) {
                    myAdapter = new TreeWorkingAdapter(SelectWorkItemTypeActivity.this, listBean.data, this);
                    recyclerView.setAdapter(myAdapter);
                }
            }
        }
    }

    @Override
    public void onWorkItemSelectCallBack(TreeWorkingBean workingBean) {
        Intent intent = new Intent();
        intent.putExtra("select_item", workingBean);
        this.setResult(RESULT_OK, intent);
        myAdapter.clearAllData();
        listBean.data.clear();
        listBean.data = null;
        listBean = null;
        this.finish();
    }
}
