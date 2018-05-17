package net.performance.assessment.view.activity;

import net.performance.assessment.R;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.DateInfo;
import net.performance.assessment.entity.TaskScoreInfo;
import net.performance.assessment.entity.TaskScoreListBean;
import net.performance.assessment.network.http.CommonAPI;
import net.performance.assessment.utils.CommonUtils;
import net.performance.assessment.utils.JsonParser;
import net.performance.assessment.utils.StringUtil;
import net.performance.assessment.utils.ToastUtil;
import net.performance.assessment.utils.ViewUtils;
import net.performance.assessment.view.adapter.TaskScoreAdapter;
import net.performance.assessment.view.widget.CustomDatePicker;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询任务积分页面
 */
public class QueryTaskScoreActivity extends BaseActivity
{
    private TextView tvStartDate;
    private TextView tvEndDate;
    private ListView mListView;
    private TextView tvTotalScore;
    private View btnQuery;

    private CustomDatePicker mStartDatePicker;
    private CustomDatePicker mEndDatePicker;

    private List<TaskScoreInfo> mTaskScoreInfos;
    private TaskScoreAdapter mAdapter;

    private long getScoreListFlag;

    private DateInfo mStartDateInfo;
    private DateInfo mEndDateInfo;

    @Override
    protected void setContentView( )
    {
        super.setContentView( );
        setContentView( R.layout.activity_query_employee_task_score );
    }

    @Override
    protected void initView( )
    {
        super.initView( );
        tvStartDate = ViewUtils.xFindViewById( this , R.id.tv_start_date );
        tvEndDate = ViewUtils.xFindViewById( this , R.id.tv_end_date );
        tvTotalScore = ViewUtils.xFindViewById( this , R.id.tv_total_score );
        mListView = ViewUtils.xFindViewById( this , R.id.score_list_view );
        btnQuery = ViewUtils.xFindViewById( this , R.id.btn_query );
    }

    @Override
    protected void initData( )
    {
        super.initData( );

        mAdapter = new TaskScoreAdapter( );
        mListView.setAdapter( mAdapter );
    }

    @Override
    protected void setListeners( )
    {
        super.setListeners( );
        tvStartDate.setOnClickListener( this );
        tvEndDate.setOnClickListener( this );
        btnQuery.setOnClickListener( this );
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( btnQuery ) )
        {
            String startDateStr = ViewUtils.getEditViewContent( tvStartDate );
            if ( StringUtil.isEmptyOrNullStr( startDateStr ) )
            {
                ToastUtil.showTip( mContext , getString( R.string.hint_input_start_date ) );
                return;
            }
            else {
                startDateStr = startDateStr.replace( "-"  , "" );
            }

            String endDateStr = ViewUtils.getEditViewContent( tvEndDate );
            if ( StringUtil.isEmptyOrNullStr( endDateStr ) )
            {
                ToastUtil.showTip( mContext , getString( R.string.hint_input_end_date ) );
                return;
            }
            else {
                endDateStr = endDateStr.replace( "-"  , "" );
            }

            showProgressDialog( "" );
            getScoreListFlag = CommonAPI.queryTaskScore( startDateStr , endDateStr , mHttpCallback );
        }
        else if ( v.equals( tvStartDate ) )
        {
            if ( mStartDatePicker == null )
            {
                mStartDateInfo = CommonUtils.createCurrentDateInfo( );
                mStartDatePicker = new CustomDatePicker( this, mStartDateInfo.year,
                        mStartDateInfo.month, mStartDateInfo.day, new View.OnClickListener( )
                {
                    @Override
                    public void onClick( View v )
                    {
                        mStartDateInfo = ( DateInfo ) v.getTag( );
                        if ( CommonUtils.compareDateInfo(
                                mStartDateInfo, mEndDateInfo ) > 0 || mEndDateInfo == null )
                        {
                            String timeStr = mStartDateInfo.getTimeFormatStr( ":" );
                            String dateStr = mStartDateInfo.getDateFormatStr( "-" );
                            tvStartDate.setText( dateStr + " " + timeStr );
                        }
                        else
                        {
                            ToastUtil.showTip( mContext,
                                    getString( R.string.toast_input_date_error ) );
                        }
                    }
                } );
            }
            mStartDatePicker.show( tvStartDate );
        }
        else if ( v.equals( tvEndDate ) )
        {
            if ( mEndDatePicker == null )
            {
                mEndDateInfo = CommonUtils.createCurrentDateInfo( );
                mEndDatePicker = new CustomDatePicker( this, mEndDateInfo.year,
                        mEndDateInfo.month, mEndDateInfo.day, new View.OnClickListener( )
                {
                    @Override
                    public void onClick( View v )
                    {
                        mEndDateInfo = ( DateInfo ) v.getTag( );
                        if ( CommonUtils.compareDateInfo(
                                mStartDateInfo, mEndDateInfo ) > 0 || mStartDateInfo == null )
                        {
                            String timeStr = mEndDateInfo.getTimeFormatStr( ":" );
                            String dateStr = mEndDateInfo.getDateFormatStr( "-" );
                            tvEndDate.setText( dateStr + " " + timeStr );
                        }
                        else
                        {
                            ToastUtil.showTip(
                                    mContext, getString( R.string.toast_input_date_error ) );
                        }
                    }
                } );
            }
            mEndDatePicker.show( tvEndDate );
        }
    }

    @Override
    protected void handleResponseSuccess( String result, long flag )
    {
        super.handleResponseSuccess( result, flag );
        if ( getScoreListFlag == flag )
        {
            BaseResultBean detectedBean = JsonParser.getInstance().getBeanFromJsonString( result , BaseResultBean.class );
            if ( detectedBean.isSuccess( ) )
            {
                TaskScoreListBean bean = JsonParser.getInstance( ).getBeanFromJsonString( result , TaskScoreListBean.class );
                if ( bean.data != null && bean.data.size( ) > 0 )
                {
                    if ( mTaskScoreInfos == null )
                    {
                        mTaskScoreInfos = new ArrayList<>(  );
                        mAdapter.setDataList( mTaskScoreInfos );
                    }
                    else {
                        mTaskScoreInfos.clear( );
                    }
                    mTaskScoreInfos.addAll( bean.data );
                    mAdapter.notifyDataSetChanged( );

                    double totalScore = 0;
                    for ( TaskScoreInfo info : mTaskScoreInfos )
                    {
                        totalScore += info.finalScore;
                    }

                    tvTotalScore.setText( String.format( "%.2f" , totalScore ) );
                }
                else {
                    tvTotalScore.setText( "0" );
                    ToastUtil.showTip( mContext , R.string.tips_empty );
                }
            }
            else {
                ToastUtil.showErrorMessage( mContext , detectedBean.message , detectedBean.errorCode );
            }
        }
    }
}
