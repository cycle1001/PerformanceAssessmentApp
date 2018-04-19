package net.performance.assessment.view.fragment;


import net.performance.assessment.R;
import net.performance.assessment.utils.ViewUtils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

/**
 *
 */

public class BaseDelayLoadFragment extends Fragment {
    protected Context mContext;

    protected View mRootView;

    /**
     * rootView是否初始化标志，防止回调函数在rootView为空的时候触发
     */
    protected boolean hasCreateView;

    /**
     * 是否进行过加载
     */
    private boolean hasLoadFinished = false;

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        createView(inflater, container);
        hasCreateView = true;
        return mRootView;
    }

    protected void createView(LayoutInflater inflater, ViewGroup container) {

    }

    protected void loadData() {
        if (!hasLoadFinished) {
            hasLoadFinished = true;
            initView();
            initData();
            setListeners();
        }
    }

    protected void initView() {

    }

    protected void inflateViewStub(int layoutRes) {
        ViewStub viewStub = ViewUtils.xFindViewById(mRootView, R.id.view_stub);
        viewStub.setLayoutResource(layoutRes);
        mRootView = viewStub.inflate();
    }

    protected void initData() {

    }

    protected void setListeners() {

    }

    protected void showProgressDialog() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        if (hasCreateView && getUserVisibleHint()) {
            loadData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (hasCreateView) {
                loadData();
            }
        }
    }
}
