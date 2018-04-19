package net.performance.assessment.view.activity;

import net.performance.assessment.R;
import net.performance.assessment.common.Constant;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.PersonnelIconItemInfo;
import net.performance.assessment.entity.PersonnelSimpleInfo;
import net.performance.assessment.entity.PersonnelSimpleInfoListBean;
import net.performance.assessment.network.http.CommonAPI;
import net.performance.assessment.utils.FileUtils;
import net.performance.assessment.utils.JsonParser;
import net.performance.assessment.utils.ToastUtil;
import net.performance.assessment.utils.ViewUtils;
import net.performance.assessment.view.adapter.DepartmentInfoAdapter;
import net.performance.assessment.view.adapter.PersonnelInfoAdapter;

import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 选择组织架构下人员的页面
 */

public class SelectPersonnelActivity extends BaseActivity {
    private ListView lvDepartment;
    private ListView lvPersonnel;

    private DepartmentInfoAdapter mDepartmentInfoAdapter;
    private List<PersonnelSimpleInfo> mDepartmentInfos;

    private PersonnelInfoAdapter mPersonnelInfoAdapter;
    private List<PersonnelSimpleInfo> mPersonnelSimpleInfos;

    private long selectPersonnelFlag;

    @Override
    protected void setContentView() {
        super.setContentView();
        setContentView(R.layout.activity_select_personnel);
    }

    @Override
    protected void initView() {
        super.initView();
        lvDepartment = ViewUtils.xFindViewById(this, R.id.department_list_view);
        lvPersonnel = ViewUtils.xFindViewById(this, R.id.personnel_list_view);
    }

    private AdapterView.OnItemClickListener mDepartmentClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*PersonnelSimpleInfo info = mDepartmentInfos.get( position );
            showProgressDialog( "" );
            selectPersonnelFlag = CommonAPI.findUsers( info.companyAreaId , info.officeAreaId , mHttpCallback );*/

            Intent intent = new Intent();
            PersonnelSimpleInfo info = mDepartmentInfos.get(position);
            PersonnelIconItemInfo itemInfo = new PersonnelIconItemInfo.Builder().id(
                    info.id).name(info.name).build();
            intent.putExtra(Constant.SELECT_DIRECTOR, itemInfo);
            setResult(RESULT_OK, intent);
            finish();
        }
    };

    private AdapterView.OnItemClickListener mPersonnelClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent();
            long currentTime = System.currentTimeMillis();
            PersonnelIconItemInfo itemInfo = new PersonnelIconItemInfo.Builder().id(
                    "xyz" + currentTime).name("abc" + currentTime).build();
            intent.putExtra(Constant.SELECT_DIRECTOR, itemInfo);
            setResult(RESULT_OK, intent);
        }
    };

    @Override
    protected void initData() {
        super.initData();

        mDepartmentInfos = new ArrayList<>();
        mPersonnelSimpleInfos = new ArrayList<>();

        mDepartmentInfoAdapter = new DepartmentInfoAdapter();
        mDepartmentInfoAdapter.setDataList(mDepartmentInfos);
        lvDepartment.setAdapter(mDepartmentInfoAdapter);
        lvDepartment.setOnItemClickListener(mDepartmentClickListener);

        mPersonnelInfoAdapter = new PersonnelInfoAdapter();
        mPersonnelInfoAdapter.setDataList(mPersonnelSimpleInfos);
        lvPersonnel.setAdapter(mPersonnelInfoAdapter);
        lvPersonnel.setOnItemClickListener(mPersonnelClickListener);

        showProgressDialog("");
        selectPersonnelFlag = CommonAPI.findUsers("", "", mHttpCallback);
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
                    mDepartmentInfos.clear();
                    mDepartmentInfos.addAll(bean.data);
                    mDepartmentInfoAdapter.notifyDataSetChanged();

                    //saveResult( result );
                }
            } else {
                ToastUtil.showErrorMessage(mContext, detectedBean.message, detectedBean.errorCode);
            }
        }
    }

    private void saveResult(String result) {
        String path = Environment.getExternalStorageDirectory() + File.separator + "payment_record.txt";
        File file = new File(path);
        try {
            FileUtils.writeTxtFile(result, file);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
