package net.performance.assessment.view.activity;

import net.performance.assessment.R;
import net.performance.assessment.common.Constant;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.PersonnelIconItemInfo;
import net.performance.assessment.entity.PersonnelSimpleInfo;
import net.performance.assessment.entity.PersonnelSimpleInfoListBean;
import net.performance.assessment.network.http.CommonAPI;
import net.performance.assessment.utils.JsonParser;
import net.performance.assessment.utils.LogUtils;
import net.performance.assessment.utils.ToastUtil;
import net.performance.assessment.utils.ViewUtils;
import net.performance.assessment.view.adapter.DepartmentInfoAdapter;
import net.performance.assessment.view.adapter.PersonnelInfoAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 选择组织架构下人员的页面
 */

public class SelectPersonnelActivity extends BaseActivity {
    private ListView lvDepartment;
    private ListView lvPersonnel;

    private int checkNum;
    private Button btnCheck;

    private DepartmentInfoAdapter mDepartmentInfoAdapter;
    private List<PersonnelSimpleInfo> mDepartmentInfos;

    private PersonnelInfoAdapter mPersonnelInfoAdapter;
    private List<PersonnelSimpleInfo> mPersonnelSimpleInfos;

    private long selectPersonnelFlag;

    private String mHideName = "";

    @Override
    protected void setContentView() {
        super.setContentView();
        setContentView(R.layout.activity_select_personnel);
    }

    @Override
    protected void initView() {
        super.initView();

        //lvDepartment = ViewUtils.xFindViewById(this, R.id.department_list_view);
        if (getIntent() != null) {
            mHideName = getIntent().getStringExtra("hideName");
        }

        lvPersonnel = ViewUtils.xFindViewById(this, R.id.personnel_list_view);
        btnCheck = ViewUtils.xFindViewById(this, R.id.btn_select_confirm);
    }

    private AdapterView.OnItemClickListener mDepartmentClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            /*Intent intent = new Intent();
            PersonnelSimpleInfo info = mDepartmentInfos.get(position);
            PersonnelIconItemInfo itemInfo = new PersonnelIconItemInfo.Builder().id(
                    info.id).name(info.name).build();
            intent.putExtra(Constant.SELECT_DIRECTOR, itemInfo);
            setResult(RESULT_OK, intent);
            finish();*/
        }
    };

    private AdapterView.OnItemClickListener mPersonnelClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*Intent intent = new Intent();
            long currentTime = System.currentTimeMillis();
            PersonnelIconItemInfo itemInfo = new PersonnelIconItemInfo.Builder().id(
                    "xyz" + currentTime).name("abc" + currentTime).build();
            intent.putExtra(Constant.SELECT_DIRECTOR, itemInfo);
            setResult(RESULT_OK, intent);*/

            CheckBox cbSelect = ViewUtils.xFindViewById( view , R.id.cb_select );
            cbSelect.toggle();
            mPersonnelInfoAdapter.getSelectionMap().put(mPersonnelSimpleInfos.get(position).id, cbSelect.isChecked());
            if (cbSelect.isChecked()) {
                checkNum++;
            } else {
                checkNum--;
            }
            btnCheck.setText("完成(" + checkNum + ")");
        }
    };

    @Override
    protected void initData() {
        super.initData();

        //mDepartmentInfos = new ArrayList<>();
        mPersonnelSimpleInfos = new ArrayList<>();

        /*mDepartmentInfoAdapter = new DepartmentInfoAdapter();
        mDepartmentInfoAdapter.setDataList(mDepartmentInfos);
        lvDepartment.setAdapter(mDepartmentInfoAdapter);
        lvDepartment.setOnItemClickListener(mDepartmentClickListener);*/

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
                    List<PersonnelSimpleInfo> list = bean.data;
                    Iterator<PersonnelSimpleInfo> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        PersonnelSimpleInfo info = iterator.next();
                        if (info.name.equals(mHideName)) {
                            iterator.remove();
                        }
                    }
                    mPersonnelSimpleInfos.clear();
                    mPersonnelSimpleInfos.addAll(bean.data);
                    mPersonnelInfoAdapter.initData();
                    mPersonnelInfoAdapter.notifyDataSetChanged();

                    //saveResult( result );
                }
            } else {
                ToastUtil.showErrorMessage(mContext, detectedBean.message, detectedBean.errorCode);
            }
        }
    }

    public void selectAll(View view) {
        if (mPersonnelSimpleInfos != null && mPersonnelSimpleInfos.size() > 0) {
            for (PersonnelSimpleInfo info : mPersonnelSimpleInfos) {
                mPersonnelInfoAdapter.getSelectionMap().put(info.id, true);
            }
            checkNum = mPersonnelSimpleInfos.size();
            dataChange();
        }
    }

    public void selectCancel(View view) {
        if (mPersonnelSimpleInfos != null && mPersonnelSimpleInfos.size() > 0) {
            for (PersonnelSimpleInfo info : mPersonnelSimpleInfos) {
                if (mPersonnelInfoAdapter.getSelectionMap().get(info.id)) {
                    mPersonnelInfoAdapter.getSelectionMap().put(info.id, false);
                    checkNum--;
                }
            }
            dataChange();
        }
    }

    public void antiElection(View view) {
        if (mPersonnelSimpleInfos != null && mPersonnelSimpleInfos.size() > 0) {
            for (PersonnelSimpleInfo info : mPersonnelSimpleInfos) {
                if (mPersonnelInfoAdapter.getSelectionMap().get(info.id)) {
                    mPersonnelInfoAdapter.getSelectionMap().put(info.id, false);
                    checkNum--;
                } else {
                    mPersonnelInfoAdapter.getSelectionMap().put(info.id, true);
                    checkNum++;
                }
            }
            dataChange();
        }
    }

    public void confirmSelect(View view) {
        PersonnelIconItemInfo[ ] selectItems;
        if (mPersonnelSimpleInfos != null && mPersonnelSimpleInfos.size() > 0) {
            int size = mPersonnelSimpleInfos.size();
            selectItems = new PersonnelIconItemInfo[ checkNum ];

            int index= 0;
            for ( int i = 0; i < size; i++ )
            {
                PersonnelSimpleInfo itemInfo = mPersonnelSimpleInfos.get( i );
                if ( mPersonnelInfoAdapter.getSelectionMap().get( itemInfo.id ) )
                {
                    selectItems[ index ] = new PersonnelIconItemInfo.Builder()
                        .id(itemInfo.id).name(itemInfo.name).build();
                    /*Parcel parcel = Parcel.obtain();
                    selectItems[ index ] = PersonnelIconItemInfo.CREATOR.createFromParcel( parcel );

                            //new PersonnelIconItemInfo( itemInfo.id , itemInfo.name );
                   selectItems[ index ].id = itemInfo.id;
                    selectItems[ index ].name = itemInfo.name;
                    parcel.recycle();*/

                    index += 1;
                }
            }

            LogUtils.v( "selectItems---size===" + selectItems.length );

            Intent intent = new Intent();
            Bundle bundle = new Bundle(  );
            bundle.putSerializable( Constant.SELECT_DIRECTOR, selectItems );
            intent.putExtras( bundle );
            setResult(RESULT_OK, intent);
            this.finish();
        }
        else {
            ToastUtil.showTip( mContext , "请选择责任人" );
        }

    }

    private void dataChange() {
        mPersonnelInfoAdapter.notifyDataSetChanged();
        btnCheck.setText("完成(" + checkNum + ")");
    }
}
