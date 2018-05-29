package net.performance.assessment.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import net.performance.assessment.R;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.PersonnelIconItemInfo;
import net.performance.assessment.entity.PersonnelSimpleInfo;
import net.performance.assessment.entity.PersonnelSimpleInfoListBean;
import net.performance.assessment.network.http.CommonAPI;
import net.performance.assessment.utils.JsonParser;
import net.performance.assessment.utils.ToastUtil;
import net.performance.assessment.view.adapter.SelectClassPersonAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClassPersonSelectActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private SelectClassPersonAdapter mAdapter;
    private int checkNum;
    private Button mTvCheck;

    private List<PersonnelSimpleInfo> mInfoList;
    private long selectPersonnelFlag;

    private String mHideName = "";

    @Override
    protected void setContentView() {
        super.setContentView();
        setContentView(R.layout.activity_select_class_person);
    }

    @Override
    protected void initView() {
        super.initView();
        mListView = findViewById(R.id.class_person_listview);
        mTvCheck = findViewById(R.id.btn_select_confirm);
    }

    @Override
    protected void initData() {
        super.initData();
        if (getIntent() != null) {
            mHideName = getIntent().getStringExtra("hideName");
        }
        showProgressDialog("");
        selectPersonnelFlag = CommonAPI.findUsers("", "", mHttpCallback);
    }

    @Override
    protected void setListeners() {
        super.setListeners();
        mListView.setOnItemClickListener(this);
    }

    public void selectAll(View view) {
        if (mInfoList != null && mInfoList.size() > 0) {
            for (PersonnelSimpleInfo info : mInfoList) {
                mAdapter.getIsSelected().put(info.id, true);
            }
            checkNum = mInfoList.size();
            dataChange();
        }
    }

    public void selectCancel(View view) {
        if (mInfoList != null && mInfoList.size() > 0) {
            for (PersonnelSimpleInfo info : mInfoList) {
                if (mAdapter.getIsSelected().get(info.id)) {
                    mAdapter.getIsSelected().put(info.id, false);
                    checkNum--;
                }
            }
            dataChange();
        }
    }

    public void antiElection(View view) {
        if (mInfoList != null && mInfoList.size() > 0) {
            for (PersonnelSimpleInfo info : mInfoList) {
                if (mAdapter.getIsSelected().get(info.id)) {
                    mAdapter.getIsSelected().put(info.id, false);
                    checkNum--;
                } else {
                    mAdapter.getIsSelected().put(info.id, true);
                    checkNum++;
                }
            }
            dataChange();
        }
    }

    public void confirmSelect(View view) {
        List<PersonnelIconItemInfo> selectItems = new ArrayList<>();
        if (mInfoList != null && mInfoList.size() > 0) {
            for (PersonnelSimpleInfo info : mInfoList) {
                if (mAdapter.getIsSelected().get(info.id)) {
                    PersonnelIconItemInfo itemInfo = new PersonnelIconItemInfo.Builder()
                            .id(info.id).name(info.name).build();
                    selectItems.add(itemInfo);
                }
            }
        }
        StringBuffer bufIds = new StringBuffer();
        StringBuffer bufNames = new StringBuffer();
        if (selectItems.size() > 0) {
            for (int i = 0; i < selectItems.size(); i++) {
                bufIds = bufIds.append(selectItems.get(i).id).append("|");
                bufNames = bufNames.append(selectItems.get(i).name).append(",");
            }
        }
        if (bufIds.length() > 0) {
            bufIds.replace(bufIds.length() - 1, bufIds.length(), "");
            bufNames.replace(bufNames.length() - 1, bufNames.length(), "");
        }
        Intent intent = new Intent();
        intent.putExtra("select_person_ids", bufIds.toString());
        intent.putExtra("select_person_names", bufNames.toString());
        setResult(RESULT_OK, intent);
        this.finish();
        }

    private void dataChange() {
        mAdapter.notifyDataSetChanged();
        mTvCheck.setText("完成(" + checkNum + ")");
    }

    @Override
    protected void handleResponseSuccess(String result, long flag) {
        super.handleResponseSuccess(result, flag);
        BaseResultBean detectedBean = JsonParser.getInstance().getBeanFromJsonString(result, BaseResultBean.class);
        if (selectPersonnelFlag == flag) {
            if (detectedBean.isSuccess()) {
                PersonnelSimpleInfoListBean bean = JsonParser.getInstance().getBeanFromJsonString(
                        result, PersonnelSimpleInfoListBean.class);
                if (bean.data != null && bean.data.size() > 0) {
                    mInfoList = bean.data;
                    Iterator<PersonnelSimpleInfo> iterator = mInfoList.iterator();
                    while (iterator.hasNext()) {
                        PersonnelSimpleInfo info = iterator.next();
                        if (info.name.equals(mHideName)) {
                            iterator.remove();
                        }
                    }
                    mAdapter = new SelectClassPersonAdapter(ClassPersonSelectActivity.this, mInfoList);
                    mListView.setAdapter(mAdapter);
                }
            } else {
                ToastUtil.showErrorMessage(mContext, detectedBean.message, detectedBean.errorCode);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SelectClassPersonAdapter.ViewHolder holder = (SelectClassPersonAdapter.ViewHolder) view.getTag();
        holder.cb.toggle();
        mAdapter.getIsSelected().put(mInfoList.get(position).id, holder.cb.isChecked());
        if (holder.cb.isChecked()) {
            checkNum++;
        } else {
            checkNum--;
        }
        mTvCheck.setText("完成(" + checkNum + ")");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
