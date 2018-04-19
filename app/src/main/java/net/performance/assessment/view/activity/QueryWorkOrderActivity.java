package net.performance.assessment.view.activity;

import net.performance.assessment.R;
import net.performance.assessment.common.Constant;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.DateInfo;
import net.performance.assessment.entity.WorkOrderInfo;
import net.performance.assessment.entity.WorkOrderListBean;
import net.performance.assessment.network.http.CommonAPI;
import net.performance.assessment.utils.CommonUtils;
import net.performance.assessment.utils.IntentUtils;
import net.performance.assessment.utils.JsonParser;
import net.performance.assessment.utils.LogUtils;
import net.performance.assessment.utils.StringUtil;
import net.performance.assessment.utils.ToastUtil;
import net.performance.assessment.utils.ViewUtils;
import net.performance.assessment.view.adapter.WorkOrderAdapter;
import net.performance.assessment.view.widget.CustomDatePicker;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询工单页面
 */

public class QueryWorkOrderActivity extends BaseActivity
{
    private TextView tvBeginTime;
    private TextView tvEndTime;
    private ListView mListView;
    private View btnQuery;

    private CustomDatePicker mBeginTimePicker;
    private CustomDatePicker mEndTimePicker;

    private List<WorkOrderInfo > mWorkOrderInfos;
    private WorkOrderAdapter mAdapter;

    private long queryWorkOrderFlag;

    private DateInfo mBeginTimeInfo;
    private DateInfo mEndTimeInfo;

    @Override
    protected void setContentView( )
    {
        super.setContentView( );
        setContentView( R.layout.activity_query_work_order );
    }

    @Override
    protected void initView( )
    {
        super.initView( );

        mCustomTitleBar.setRightBtnVisibility( View.VISIBLE ).setRightText( getString( R.string.btn_query ) );

        tvBeginTime = ViewUtils.xFindViewById( this , R.id.tv_begin_time );
        tvEndTime = ViewUtils.xFindViewById( this , R.id.tv_end_time );
        mListView = ViewUtils.xFindViewById( this , R.id.work_order_list_view );
        btnQuery = ViewUtils.xFindViewById( this , R.id.btn_right );
    }

    @Override
    protected void initData( )
    {
        super.initData( );

        mAdapter = new WorkOrderAdapter( );
        mListView.setAdapter( mAdapter );
        mListView.setOnItemClickListener( mItemClickListener );
    }

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener( )
    {
        @Override
        public void onItemClick( AdapterView< ? > adapterView, View view, int position, long l )
        {
            WorkOrderInfo workOrderInfo = mWorkOrderInfos.get( position );
            if ( workOrderInfo != null )
            {
                IntentUtils.startActivity( mContext , WorkOrderDetailActivity.class , Constant.WORK_ORDER_INFO , workOrderInfo );
            }
        }
    };

    @Override
    protected void setListeners( )
    {
        super.setListeners( );
        tvBeginTime.setOnClickListener( this );
        tvEndTime.setOnClickListener( this );
        btnQuery.setOnClickListener( this );
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( btnQuery ) )
        {
            String startDateStr = ViewUtils.getEditViewContent( tvBeginTime );
            if ( StringUtil.isEmptyOrNullStr( startDateStr ) )
            {
                ToastUtil.showTip( mContext , getString( R.string.hint_input_start_date ) );
                return;
            }
            else {
                startDateStr = startDateStr.replace( "-"  , "" );
            }

            String endDateStr = ViewUtils.getEditViewContent( tvEndTime );
            if ( StringUtil.isEmptyOrNullStr( endDateStr ) )
            {
                ToastUtil.showTip( mContext , getString( R.string.hint_input_end_date ) );
                return;
            }
            else {
                endDateStr = endDateStr.replace( "-"  , "" );
            }

            showProgressDialog( "" );
            queryWorkOrderFlag = CommonAPI.queryWorkOrder( startDateStr , endDateStr , mHttpCallback );
        }
        else if ( v.equals( tvBeginTime ) )
        {
            if ( mBeginTimePicker == null )
            {
                mBeginTimeInfo = CommonUtils.createCurrentDateInfo( );
                mBeginTimePicker = new CustomDatePicker( this, mBeginTimeInfo.year,
                        mBeginTimeInfo.month, mBeginTimeInfo.day, new View.OnClickListener( )
                {
                    @Override
                    public void onClick( View v )
                    {
                        mBeginTimeInfo = ( DateInfo ) v.getTag( );
                        if ( CommonUtils.compareDateInfo(
                                mBeginTimeInfo, mEndTimeInfo ) > 0 || mEndTimeInfo == null )
                        {
                            String dateStr = mBeginTimeInfo.getDateFormatStr( "-" );
                            tvBeginTime.setText( dateStr );
                        }
                        else
                        {
                            ToastUtil.showTip( mContext,
                                    getString( R.string.toast_input_date_error ) );
                        }
                    }
                } );
                mBeginTimePicker.hideHourOption( ).hideMinuteOption().hideSecondOption();
            }
            LogUtils.v( mBeginTimeInfo.toString( ) );
            mBeginTimePicker.show( tvBeginTime );
        }
        else if ( v.equals( tvEndTime ) )
        {
            if ( mEndTimePicker == null )
            {
                mEndTimeInfo = CommonUtils.createCurrentDateInfo( );
                mEndTimePicker = new CustomDatePicker( this, mEndTimeInfo.year,
                        mEndTimeInfo.month, mEndTimeInfo.day, new View.OnClickListener( )
                {
                    @Override
                    public void onClick( View v )
                    {
                        mEndTimeInfo = ( DateInfo ) v.getTag( );
                        if ( CommonUtils.compareDateInfo(
                                mBeginTimeInfo, mEndTimeInfo ) > 0 || mBeginTimeInfo == null )
                        {
                            String dateStr = mEndTimeInfo.getDateFormatStr( "-" );
                            tvEndTime.setText( dateStr );
                        }
                        else
                        {
                            ToastUtil.showTip(
                                    mContext, getString( R.string.toast_input_date_error ) );
                        }
                    }
                } );
                mEndTimePicker.hideHourOption( ).hideMinuteOption().hideSecondOption();
            }
            mEndTimePicker.show( tvEndTime );
        }
    }

    @Override
    protected void handleResponseSuccess( String result, long flag )
    {
        super.handleResponseSuccess( result, flag );
        if ( queryWorkOrderFlag == flag )
        {
            BaseResultBean detectedBean = JsonParser.getInstance( ).getBeanFromJsonString( result , BaseResultBean.class );
            if ( detectedBean.isSuccess( ) )
            {
                WorkOrderListBean bean = JsonParser.getInstance( ).getBeanFromJsonString( result , WorkOrderListBean.class );
                if ( bean.data != null && bean.data.size( ) > 0 )
                {
                    if ( mWorkOrderInfos == null )
                    {
                        mWorkOrderInfos = new ArrayList<>(  );
                        mAdapter.setDataList( mWorkOrderInfos );
                    }
                    else {
                        mWorkOrderInfos.clear( );
                    }
                    mWorkOrderInfos.addAll( bean.data );
                    mAdapter.notifyDataSetChanged( );
                }
                else {
                    ToastUtil.showTip( mContext , R.string.tips_empty );
                }
            }
            else {
                ToastUtil.showErrorMessage( mContext , detectedBean.message , detectedBean.errorCode );
            }
        }
    }
}
