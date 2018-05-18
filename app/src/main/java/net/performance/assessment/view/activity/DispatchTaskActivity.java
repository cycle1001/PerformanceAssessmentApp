package net.performance.assessment.view.activity;

import net.performance.assessment.R;
import net.performance.assessment.cache.LoginInfoCache;
import net.performance.assessment.common.Constant;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.LoginInfo;
import net.performance.assessment.entity.PersonnelIconItemInfo;
import net.performance.assessment.entity.TreeWorkingBean;
import net.performance.assessment.entity.type.TaskState;
import net.performance.assessment.entity.type.TaskType;
import net.performance.assessment.network.http.DispatchTaskAPI;
import net.performance.assessment.utils.CommonUtils;
import net.performance.assessment.utils.JsonParser;
import net.performance.assessment.utils.TimeUtils;
import net.performance.assessment.utils.ToastUtil;
import net.performance.assessment.utils.ViewUtils;
import net.performance.assessment.view.adapter.PersonnelIconItemAdapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 派单页面
 */

public class DispatchTaskActivity extends BaseActivity
{
    private final static int REQ_SELECT_PROJECT_TYPE = 100;
    private final static int REQ_SELECT_DIRECTOR = 101;

    private TextView tvTaskId;
    private TextView tvProjectType;
    private TextView tvDispatcher;
    private TextView tvTaskContent;
    private TextView tvDispatchTime;

    private TextView tvTaskStatus;

    private TextView etScoreStandard;
    private GridView lvDirector;

    private Button btnSubmit;

    private long submitFlag;

    private String mSelectWorkItemType = "";
    private String mGroupIds;

    private List<PersonnelIconItemInfo> mDirectorList;
    private PersonnelIconItemAdapter mAdapter;
    private PersonnelIconItemInfo mAddItemInfo;

    @Override
    protected void setContentView( )
    {
        setContentView( R.layout.activity_dispatch_task );
    }

    @Override
    protected void initView( )
    {
        super.initView( );

        tvTaskId = ViewUtils.xFindViewById( this , R.id.tv_task_id );
        tvProjectType = ViewUtils.xFindViewById( this , R.id.tv_project_type );
        tvDispatcher = ViewUtils.xFindViewById( this , R.id.tv_dispatcher );
        tvTaskContent = ViewUtils.xFindViewById( this , R.id.tv_task_content );
        tvDispatchTime = ViewUtils.xFindViewById( this , R.id.tv_dispatch_time );

        tvTaskStatus = ViewUtils.xFindViewById( this , R.id.tv_task_status );
        etScoreStandard = ViewUtils.xFindViewById( this , R.id.tv_score_standard );

        lvDirector = ViewUtils.xFindViewById( this , R.id.director_grid_view );
        btnSubmit = ViewUtils.xFindViewById( this , R.id.btn_submit );
    }

    @Override
    protected void initData( )
    {
        super.initData( );
        LoginInfo loginInfo = LoginInfoCache.getInstance( ).getLoginInfo( );

        String currentTime = TimeUtils.getNowTimeString( );
        String numMillis = String.valueOf(TimeUtils.string2Millis(currentTime) / 1000);

        if ( loginInfo != null )
        {
            String userId = LoginInfoCache.getInstance( ).getLoginInfo( ).seriesno;
            tvTaskId.setText( CommonUtils.generateTaskId( TaskType.DISPATCH ,  userId , numMillis ) );

            String userName = loginInfo.name;
            tvDispatcher.setText( userName );
        }

        tvDispatchTime.setText( currentTime );

        String statusStr = getString( R.string.task_state_non_distribute );
        tvTaskStatus.setText( statusStr );

        initDirectorGroup( );
    }

    private AdapterView.OnItemClickListener mSelectDirectorClickListener = new AdapterView.OnItemClickListener( )
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if ( position == mDirectorList.size( ) - 1 )
            {
                Intent intent = new Intent( mContext, SelectPersonnelActivity.class );
                intent.putExtra("hideName", tvDispatcher.getText().toString().trim());
                startActivityForResult( intent , REQ_SELECT_DIRECTOR );
            }
            else {
                mDirectorList.remove( position );
                mAdapter.notifyDataSetChanged( );
            }
        }
    };

    private void initDirectorGroup( ){
        mDirectorList = new ArrayList<>(  );
        mAdapter = new PersonnelIconItemAdapter( );
        mAdapter.setDataList( mDirectorList );

        lvDirector.setAdapter( mAdapter );
        lvDirector.setOnItemClickListener( mSelectDirectorClickListener );

        mAddItemInfo = new PersonnelIconItemInfo.Builder( ).id( "-1" ).name( "add" ).build( );
        mDirectorList.add( mAddItemInfo );

        mAdapter.notifyDataSetChanged( );
    }

    @Override
    protected void setListeners( )
    {
        super.setListeners( );
        btnSubmit.setOnClickListener( this );
        tvProjectType.setOnClickListener( this );
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( btnSubmit ) )
        {
            String dispatchNumStr = ViewUtils.getEditViewContent( tvTaskId );

            String projectTypeStr = ViewUtils.getEditViewContent( tvProjectType );
            if ( TextUtils.isEmpty( projectTypeStr ) )
            {
                ToastUtil.showCenterTip( this, R.string.tip_select_work_item );
                return;
            }
            String dispatchContentStr = ViewUtils.getEditViewContent( tvTaskContent );
            if ( TextUtils.isEmpty( dispatchContentStr ) )
            {
                ToastUtil.showCenterTip( this, R.string.tip_input_work_content );
                return;
            }
            String scoreStandardStr = ViewUtils.getEditViewContent( etScoreStandard );
            if ( TextUtils.isEmpty( scoreStandardStr ) )
            {
                ToastUtil.showCenterTip( this, R.string.tip_input_score );
                return;
            }

            int size = mDirectorList.size( );
            if ( size <= 0 )
            {
                ToastUtil.showTip( mContext , getString( R.string.toast_select_director ) );
                return;
            }
            StringBuilder builder = new StringBuilder( "" );
            for ( int i = 0; i < size - 1; i++ )
            {
                PersonnelIconItemInfo info = mDirectorList.get( i );
                builder.append( info.id );
                //if ( i < size - 2 )
                {
                    builder.append( "," );
                }
            }
            String directorStr = builder.toString( );

            showProgressDialog( "" );
            submitFlag = DispatchTaskAPI.submitDispatchingTask( dispatchNumStr , mSelectWorkItemType , dispatchContentStr,
                    scoreStandardStr , directorStr , TaskState.NON_DISTRIBUTED, mHttpCallback );
        }
        else if ( v.equals( tvProjectType ) )
        {
            startActivityForResult(new Intent(this, SelectWorkItemTypeActivity.class), REQ_SELECT_PROJECT_TYPE );
        }
    }

    @Override
    protected void handleResponseSuccess( String result, long flag )
    {
        super.handleResponseSuccess( result, flag );
        BaseResultBean bean = JsonParser.getInstance( ).getBeanFromJsonString( result , BaseResultBean.class );
        if ( bean.isSuccess( ) )
        {
            if ( submitFlag == flag )
            {
                ToastUtil.showTip( mContext  , getString( R.string.toast_submit_success ) );
                finish( );
            }
        }
        else {
            ToastUtil.showErrorMessage( mContext , bean.message , bean.errorCode );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_SELECT_PROJECT_TYPE && resultCode == RESULT_OK) {
            TreeWorkingBean workingBean = (TreeWorkingBean) data.getSerializableExtra( Constant.SELECT_PROJECT_TYPE );
            loadWorkItemType(workingBean);
        }
        else if ( requestCode == REQ_SELECT_DIRECTOR && resultCode == RESULT_OK )
        {
            if ( data != null )
            {
                PersonnelIconItemInfo itemInfo = ( PersonnelIconItemInfo ) data.getSerializableExtra( Constant.SELECT_DIRECTOR );
                if ( itemInfo != null )
                {
                    boolean isExisted = false;
                    for ( int i = 0; i < mDirectorList.size( ); i++ )
                    {
                        PersonnelIconItemInfo temp = mDirectorList.get( i );
                        if ( itemInfo.id.equals( temp.id ) )
                        {
                            isExisted = true;
                            break;
                        }
                    }
                    if ( !isExisted )
                    {
                        int index = mDirectorList.size( ) - 1;
                        mDirectorList.add( index , itemInfo );
                        mAdapter.notifyDataSetChanged( );
                    }
                }
            }
        }
    }

    private void loadWorkItemType(TreeWorkingBean workingBean) {
        if (workingBean != null) {
            StringBuffer itemType = new StringBuffer();
            itemType.append(workingBean.firstName);
            itemType.append("-");
            itemType.append(workingBean.secondName);
            itemType.append("-");
            itemType.append(workingBean.thirdName);
            tvProjectType.setText(itemType.toString());
            mSelectWorkItemType = workingBean.firstId + "|" + workingBean.secondId + "|" + workingBean.thirdId;

            tvTaskContent.setText(workingBean.name);
            etScoreStandard.setText(workingBean.standardScore);
            mGroupIds = workingBean.groupIds;
        }
    }
}
