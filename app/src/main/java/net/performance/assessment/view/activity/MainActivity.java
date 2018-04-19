package net.performance.assessment.view.activity;

import net.performance.assessment.R;
import net.performance.assessment.common.CustomPermissionManager;
import net.performance.assessment.interfaces.CheckPermissionCallback;
import net.performance.assessment.receiver.KeepLiveReceiver;
import net.performance.assessment.service.KeepLiveService;
import net.performance.assessment.utils.CommonUtils;
import net.performance.assessment.utils.LoginHelper;
import net.performance.assessment.utils.ToastUtil;
import net.performance.assessment.utils.ViewUtils;
import net.performance.assessment.view.adapter.TabsAdapter;
import net.performance.assessment.view.fragment.MainPageFragment;
import net.performance.assessment.view.fragment.PersonalCenterFragment;
import net.performance.assessment.view.fragment.TaskFragment;
import net.performance.assessment.view.widget.GradientTabImageView;
import net.performance.assessment.view.widget.GradientTabTextView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 首页
 */
public class MainActivity extends BaseActivity {
    private int TAB_COUNT = 3;

    private ViewPager mViewPager;
    private View[] mTabGroupViews;

    private Fragment[] mTabFragments;
    private TabsAdapter mTabsAdapter;

    private GradientTabImageView[] mGradientTabImageViews;
    private GradientTabTextView[] mGradientTabTextViews;

    private BroadcastReceiver mReceiver;
    private Activity mActivity;

    @Override
    protected void setTheme( )
    {
        super.setTheme( );
        setTheme( R.style.AppTheme );
    }

    @Override
    protected void setContentView() {
        super.setContentView();
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {
        super.initView();

        //获取缓存的登录信息
        /*String loginDetailInfoStr = SharedPreferenceUtils.getInstance( mContext ).getString(
                Constant.LOGIN_DETAIL_INFO );
        LogUtils.v( "loginDetailInfoStr===" + loginDetailInfoStr );
        if ( !StringUtil.isEmptyOrNullStr( loginDetailInfoStr ) )
        {
            LoginInfo loginInfo = JsonParser.getInstance( ).getBeanFromJsonString( loginDetailInfoStr , LoginInfo.class );
            Config.setToken(loginInfo.appToken);
            LoginInfoCache.getInstance( ).save( loginInfo );
            CustomConfigManager.getInstance( ).getUserMenu( );
        }
        else
        {
            if ( StringUtil.isEmptyOrNullStr( Config.sToken ) )
            {
                redirectTo( );
            }
        }*/

        mCustomTitleBar.setLeftBtnVisibility( View.GONE );

        mGradientTabImageViews = new GradientTabImageView[TAB_COUNT];
        mGradientTabImageViews[0] = ViewUtils.xFindViewById(this, R.id.tab_main_page_image);
        mGradientTabImageViews[1] = ViewUtils.xFindViewById(this, R.id.tab_task_image);
        mGradientTabImageViews[2] = ViewUtils.xFindViewById(this, R.id.tab_personal_center_image);

        mGradientTabTextViews = new GradientTabTextView[TAB_COUNT];
        mGradientTabTextViews[0] = ViewUtils.xFindViewById(this, R.id.tab_main_page_text);
        mGradientTabTextViews[1] = ViewUtils.xFindViewById(this, R.id.tab_task_text);
        mGradientTabTextViews[2] = ViewUtils.xFindViewById(this, R.id.tab_personal_center_text);

        mTabGroupViews = new View[TAB_COUNT];
        mTabGroupViews[0] = ViewUtils.xFindViewById(this, R.id.tab_main_page_group_view);
        mTabGroupViews[1] = ViewUtils.xFindViewById(this, R.id.tab_task_group_view);
        mTabGroupViews[2] = ViewUtils.xFindViewById(this,
                R.id.tab_personal_center_group_view);

        mViewPager = ViewUtils.xFindViewById(this, R.id.view_pager);

        mTabFragments = new Fragment[TAB_COUNT];
        mTabFragments[0] = new MainPageFragment();
        mTabFragments[1] = new TaskFragment( );
        mTabFragments[2] = new PersonalCenterFragment();

        mTabsAdapter = new TabsAdapter(getSupportFragmentManager(), mViewPager, mTabFragments);
        mViewPager.setAdapter(mTabsAdapter);

        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(TAB_COUNT);
        mTabsAdapter.notifyDataSetChanged();
        refreshTabsView(0);

        LoginHelper.getInstance().autoLogin( mContext );

        initKeepLiveService( );
        initKeepLiveReceiver( );
    }

    private void redirectTo() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void setListeners() {
        super.setListeners();
        for (View tabView : mTabGroupViews) {
            tabView.setOnClickListener(this);
        }

        mViewPager.addOnPageChangeListener(mOnPageChangeListener);

        mActivity = this;
        mViewPager.postDelayed( new Runnable( )
        {
            @Override
            public void run( )
            {

                CustomPermissionManager.getInstance( ).init( mActivity );
                CustomPermissionManager.getInstance( ).request( mCheckPermissionCallback );
            }
        } , 1000 );
    }

    private CheckPermissionCallback mCheckPermissionCallback = new CheckPermissionCallback() {
        @Override
        public void onPermissionAllGranted() {
            //redirectTo( );
        }

        @Override
        public void onPermissionDeniedTemporarily() {
            CustomPermissionManager.getInstance().release();
            CustomPermissionManager.getInstance().init(mActivity);
            CustomPermissionManager.getInstance().request(mCheckPermissionCallback);
        }

        @Override
        public void onPermissionDeniedPermantly() {
            //设置权限，重启应用
            AlertDialog builder = new AlertDialog.Builder(mActivity).setTitle(
                    getString(R.string.label_title_prompt) ).create();
            builder.setButton(
                    DialogInterface.BUTTON_POSITIVE, getString(R.string.btn_confirm ),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CommonUtils.exitApp( );
                        }
                    });
            builder.setMessage(getString(R.string.msg_set_permission_and_restart));
            builder.show();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CustomPermissionManager.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CustomPermissionManager.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    private void refreshTabsView(int position) {
        for (int i = 0; i < TAB_COUNT; i++) {

            if (position == i) {
                mGradientTabImageViews[i].setTabOffSet(0);
            } else {
                mGradientTabImageViews[i].setTabOffSet(1.0f);
            }
        }
    }

    private void selectTab(int position) {
        mViewPager.setCurrentItem(position, true);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(mTabGroupViews[0])) {
            //首页
            selectTab(0);
        } else if (view.equals(mTabGroupViews[1])) {
            //功能列表
            selectTab(1);
            //IntentUtils.startActivity(mContext, ChangePwdActivity.class);
        } else if (view.equals(mTabGroupViews[2])) {
            //个人中心
            selectTab(2);
            /*if ( StringUtil.isEmptyOrNullStr( Config.sToken )) {
                selectTab(2);
            } else {
                IntentUtils.startActivity(mContext, LoginActivity.class);
            }*/
        }
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (positionOffset > 0) {
                int nextPosition = position + 1;
                float nextOffset = 1 - positionOffset;
                mGradientTabImageViews[position].setTabOffSet(positionOffset);
                mGradientTabImageViews[nextPosition].setTabOffSet(nextOffset);

                mGradientTabTextViews[position].setTabOffSet(positionOffset);
                mGradientTabTextViews[nextPosition].setTabOffSet(nextOffset);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
            switch (state) {
                case ViewPager.SCROLL_STATE_IDLE:
                    refreshTabsView(mViewPager.getCurrentItem());
                    break;
            }
        }
    };

    private boolean isQuit = false;

    @Override
    public void onBackPressed() {

        if (!isQuit) {
            ToastUtil.showTip(this, "再按一次退出程序");
            isQuit = true;

            //这段代码意思是,在两秒钟之后isQuit会变成false
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        isQuit = false;
                    }
                }
            }).start();


        } else {
            removeALLActivity();
            System.exit(0);
        }
    }

    private void initKeepLiveService( )
    {
        Intent intent = new Intent(MainActivity.this, KeepLiveService.class);
        startService( intent );
    }

    private void initKeepLiveReceiver( )
    {
        mReceiver = new KeepLiveReceiver( );
        IntentFilter filter = new IntentFilter(  );
        filter.addAction( Intent.ACTION_SCREEN_OFF );
        filter.addAction( Intent.ACTION_USER_PRESENT );
        registerReceiver( mReceiver , filter );
    }

    @Override
    protected void onDestroy( )
    {
        super.onDestroy( );
        if ( mReceiver != null )
        {
            unregisterReceiver( mReceiver );
        }
        LoginHelper.getInstance().exit();
    }
}
