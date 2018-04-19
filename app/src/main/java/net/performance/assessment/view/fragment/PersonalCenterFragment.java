package net.performance.assessment.view.fragment;


import net.performance.assessment.R;
import net.performance.assessment.cache.CommonSwitchCache;
import net.performance.assessment.cache.LoginInfoCache;
import net.performance.assessment.common.Config;
import net.performance.assessment.common.Constant;
import net.performance.assessment.entity.LoginInfo;
import net.performance.assessment.utils.IntentUtils;
import net.performance.assessment.utils.SharedPreferenceUtils;
import net.performance.assessment.utils.StringUtil;
import net.performance.assessment.utils.ViewUtils;
import net.performance.assessment.view.activity.ChangePwdActivity;
import net.performance.assessment.view.activity.LoginActivity;
import net.performance.assessment.view.activity.MyTaskListActivity;
import net.performance.assessment.view.activity.QueryWorkOrderActivity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 *
 */

public class PersonalCenterFragment extends BaseDelayLoadFragment
{
    private TextView mTvUserPhoto;
    private TextView mTvUserName;
    private TextView mTvUserNo;

    private View btnMyTask;
    private View btnQueryWorkOrder;
    private View btnModifyPassword;
    private TextView btnLogout;

    private AlertDialog builder;

    @Override
    protected void createView( LayoutInflater inflater, ViewGroup container )
    {
        super.createView( inflater, container );
        mRootView = inflater.inflate( R.layout.fragment_common_view_stub, null );
    }

    @Override
    protected void initView( )
    {
        inflateViewStub( R.layout.fragment_personal_center );

        mTvUserName = ViewUtils.xFindViewById( mRootView, R.id.tv_user_name );
        mTvUserPhoto = ViewUtils.xFindViewById( mRootView, R.id.tv_user_photo );
        mTvUserNo = ViewUtils.xFindViewById( mRootView, R.id.tv_user_no );

        btnMyTask = ViewUtils.xFindViewById( mRootView, R.id.my_task_view );
        btnQueryWorkOrder = ViewUtils.xFindViewById( mRootView, R.id.query_work_order_view );
        btnModifyPassword = ViewUtils.xFindViewById( mRootView, R.id.password_modify_view );
        btnLogout = ViewUtils.xFindViewById( mRootView, R.id.btn_logout );
    }

    @Override
    protected void setListeners( )
    {
        super.setListeners( );
        btnMyTask.setOnClickListener( mClickListener );
        btnQueryWorkOrder.setOnClickListener( mClickListener );
        btnModifyPassword.setOnClickListener( mClickListener );
        btnLogout.setOnClickListener( mClickListener );
    }

    private View.OnClickListener mClickListener = new View.OnClickListener( )
    {
        @Override
        public void onClick( View v )
        {
            if ( StringUtil.isEmptyOrNullStr( Config.sToken ) )
            {
                //请先登录
                IntentUtils.startActivity( mContext, LoginActivity.class );
                return;
            }
            if ( v.equals( btnMyTask ) )
            {
                IntentUtils.startActivity( mContext, MyTaskListActivity.class );
            }
            else if ( v.equals( btnQueryWorkOrder ) )
            {
                IntentUtils.startActivity( mContext, QueryWorkOrderActivity.class );
            }
            else if ( v.equals( btnModifyPassword ) )
            {
                IntentUtils.startActivity(mContext, ChangePwdActivity.class );
            }
            else if ( v.equals( btnLogout ) )
            {
                if ( Config.isLogin( ) )
                {
                    showLogoutDialog(  );
                }
                else {
                    IntentUtils.startActivity( mContext , LoginActivity.class );
                }
            }
        }
    };

    private void showPersonalInfo( )
    {
        LoginInfo loginInfo = LoginInfoCache.getInstance( ).getLoginInfo( );
        if ( loginInfo != null )
        {
            String userName = loginInfo.name;
            mTvUserName.setText( userName );
            mTvUserNo.setText( loginInfo.loginName );
            if ( ! TextUtils.isEmpty( userName ) && userName.length( ) > 0 )
            {
                mTvUserPhoto.setText( userName.substring( 0, 1 ) );
            }
        }
    }

    @Override
    protected void loadData( )
    {
        super.loadData( );
        if ( Config.isLogin( ) )
        {
            btnLogout.setText( getString( R.string.label_logout ) );

            showPersonalInfo( );
        }
        else {
            btnLogout.setText( getString( R.string.label_login ) );
        }
    }

    @Override
    public void onResume( )
    {
        super.onResume( );
        if ( btnLogout != null )
        {
            if ( Config.isLogin( ) )
            {
                btnLogout.setText( getString( R.string.label_logout ) );

                showPersonalInfo( );
            }
            else {
                btnLogout.setText( getString( R.string.label_login ) );
            }
        }
    }

    public void showLogoutDialog( ) {
        if ( builder == null )
        {
            builder = new AlertDialog.Builder( mContext ).create( );
        }
        builder.setButton( DialogInterface.BUTTON_POSITIVE, getString( R.string.btn_confirm ),
                new DialogInterface.OnClickListener( )
                {
                    @Override
                    public void onClick( DialogInterface dialog, int which )
                    {
                        Config.sToken = "";
                        CommonSwitchCache.hasGetData = false;
                        CommonSwitchCache.hasLoadMenu = false;

                        SharedPreferenceUtils.getInstance( mContext ).remove( Constant.LOGIN_DETAIL_INFO );
                        btnLogout.setText( getString( R.string.label_login ) );

                        mTvUserPhoto.setText( "" );
                        mTvUserName.setText( "" );
                        mTvUserNo.setText( "" );

                        LoginInfoCache.getInstance().clear( );
                    }
                } );
        builder.setButton( DialogInterface.BUTTON_NEGATIVE, getString( R.string.btn_cancel ),
                new DialogInterface.OnClickListener( )
                {
                    @Override
                    public void onClick( DialogInterface dialog, int which )
                    {
                        builder.cancel( );
                    }
                } );
        builder.setMessage( getString( R.string.label_logout ) );
        builder.show( );
    }

}
