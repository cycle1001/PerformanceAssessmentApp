package net.performance.assessment.view.activity;

import net.performance.assessment.R;
import net.performance.assessment.entity.BaseResultBean;
import net.performance.assessment.entity.DateInfo;
import net.performance.assessment.entity.MonthPerformanceScoreInfo;
import net.performance.assessment.entity.MonthPerformanceScoreInfoListBean;
import net.performance.assessment.network.http.CommonAPI;
import net.performance.assessment.utils.CommonUtils;
import net.performance.assessment.utils.JsonParser;
import net.performance.assessment.utils.StringUtil;
import net.performance.assessment.utils.ToastUtil;
import net.performance.assessment.utils.ViewUtils;
import net.performance.assessment.view.adapter.MonthPerformanceScoreAdapter;
import net.performance.assessment.view.widget.CustomDatePicker;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *  月绩效得分查询页面
 */
public class QueryMonthPerformanceScoreActivity extends BaseActivity
{
    private TextView tvDate;
    private View btnQuery;
    private ListView lvPerformanceScore;

    private long getScoreListFlag;

    private CustomDatePicker mDatePicker;
    private DateInfo mDateInfo;

    private MonthPerformanceScoreAdapter mAdapter;
    private List<MonthPerformanceScoreInfo > mTaskScoreInfos;

    @Override
    protected void setContentView( )
    {
        super.setContentView( );
        setContentView( R.layout.activity_query_month_performance_score );
    }

    @Override
    protected void initView( )
    {
        super.initView( );
        tvDate = ViewUtils.xFindViewById( this , R.id.tv_date );
        btnQuery = ViewUtils.xFindViewById( this , R.id.btn_query );
        lvPerformanceScore = ViewUtils.xFindViewById( this , R.id.score_list_view );
    }

    @Override
    protected void initData( )
    {
        super.initData( );

        mAdapter = new MonthPerformanceScoreAdapter( );
        lvPerformanceScore.setAdapter( mAdapter );

        mDateInfo = CommonUtils.createCurrentDateInfo( );
        showDate( );
    }

    private void showDate( ){
        if ( mDateInfo != null )
        {
            StringBuilder builder = new StringBuilder( );
            builder.append( mDateInfo.getYear( ) );
            builder.append( "-" );
            int month = mDateInfo.getMonth( );
            builder.append( month > 9 ? String.valueOf( month ) : "0" + String.valueOf( month ) );
            String dateStr = builder.toString( );
            tvDate.setText( dateStr /*+ " " + timeStr*/ );
        }
    }

    @Override
    protected void setListeners( )
    {
        super.setListeners( );
        tvDate.setOnClickListener( this );
        btnQuery.setOnClickListener( this );
    }

    @Override
    public void onClick( View v )
    {
        if ( v.equals( btnQuery ) )
        {
            String dateStr = ViewUtils.getEditViewContent( tvDate );
            if ( StringUtil.isEmptyOrNullStr( dateStr ) )
            {
                ToastUtil.showTip( mContext , getString( R.string.hint_input_target_date ) );
                return;
            }
            else {
                dateStr = dateStr.replaceAll( "-"  , "" );
            }

            showProgressDialog( "" );
            getScoreListFlag = CommonAPI.queryMonthPerformanceScore( dateStr , mHttpCallback );
        }
        else if ( v.equals( tvDate ) )
        {
            if ( mDatePicker == null )
            {
                Calendar calendar = Calendar.getInstance( );
                int currentYear = calendar.get( Calendar.YEAR );
                mDatePicker = new CustomDatePicker( this, 2018,
                        2050,  new View.OnClickListener( )
                {
                    @Override
                    public void onClick( View v )
                    {
                        mDateInfo = ( DateInfo ) v.getTag( );
                        showDate( );
                    }
                } );

                mDatePicker.hideDayOption( ).hideHourOption( ).hideMinuteOption( ).hideSecondOption( );
            }
            mDatePicker.show( tvDate );
        }
    }

    @Override
    protected void handleResponseSuccess( String result, long flag )
    {
        super.handleResponseSuccess( result, flag );
        if ( getScoreListFlag == flag )
        {
            BaseResultBean detectedBean = JsonParser.getInstance( ).getBeanFromJsonString( result , BaseResultBean.class );
            if ( detectedBean.isSuccess( ) )
            {
                MonthPerformanceScoreInfoListBean bean = JsonParser.getInstance( ).getBeanFromJsonString( result , MonthPerformanceScoreInfoListBean.class );
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
