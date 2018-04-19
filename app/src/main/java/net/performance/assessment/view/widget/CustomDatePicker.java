package net.performance.assessment.view.widget;

import net.performance.assessment.R;
import net.performance.assessment.entity.DateInfo;
import net.performance.assessment.utils.ViewUtils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelScrollListener;
import antistatic.spinnerwheel.adapters.AbstractWheelTextAdapter;
import antistatic.spinnerwheel.adapters.NumericWheelAdapter;

/**
 *
 */

public class CustomDatePicker
{
    private Context mContext;

    private RelativePopupWindow mPopupWindow;

    // 年月日控件
    private AbstractWheel mYearWheel;
    private AbstractWheel mMonthWheel;
    private AbstractWheel mDayWheel;

    private AbstractWheel mHourWheel;
    private AbstractWheel mMinuteWheel;
    private AbstractWheel mSecondWheel;

    private View mConfirmBtn;
    private View mCancelBtn;

    private View.OnClickListener mConfirmClickListener;
    //变动监听
    private OnWheelScrollListener mScrollListener;

    // 起始年份
    private int startYear;
    // 结束年份
    private int endYear;

    // 当前年份
    private int thisYear;
    // 当前月份
    private int thisMonth;
    // 当前日
    private int thisDay;

    private int thisHour;

    private int thisMinute;

    private int thisSecond;

    public CustomDatePicker( Context context, int startYear, int endYear ,
            View.OnClickListener confirmClickListener )
    {
        mContext = context;
        mConfirmClickListener = confirmClickListener;

        this.startYear = startYear;
        this.endYear = endYear;

        this.thisHour = 0;
        this.thisMinute = 0;
        this.thisSecond = 0;

        inflate( );
    }

    public CustomDatePicker( Context context, int currentYear, int currentMonth, int currentDay,
            View.OnClickListener confirmClickListener )
    {
        mContext = context;
        mConfirmClickListener = confirmClickListener;
        this.thisYear = currentYear;
        this.thisMonth = currentMonth;
        this.thisDay = currentDay;
        this.thisHour = 0;
        this.thisMinute = 0;
        this.thisSecond = 0;

        inflate( );
    }

    public CustomDatePicker( Context context, int currentYear, int currentMonth, int currentDay,
            int currentHour , int currentMinute , int currentSecond ,
            View.OnClickListener confirmClickListener )
    {
        mContext = context;
        mConfirmClickListener = confirmClickListener;
        this.thisYear = currentYear;
        this.thisMonth = currentMonth;
        this.thisDay = currentDay;
        this.thisHour = currentHour;
        this.thisMinute = currentMinute;
        this.thisSecond = currentSecond;

        inflate( );
    }

    private void inflate( ){
        LayoutInflater inflater = ( LayoutInflater ) mContext.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate( R.layout.custom_date_picker_layout, null );
        initView( view );
        if ( mPopupWindow == null )
        {
            mPopupWindow = new RelativePopupWindow( view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT );
        }
    }

    public void show( View anchorView )
    {
        if ( ! mPopupWindow.isShowing( ) )
        {
            mPopupWindow.setCancelFromOutside( true );
            mPopupWindow.showAtLocation( anchorView, Gravity.BOTTOM, 0, 0 );
        }
    }

    public void hide( )
    {
        if ( mPopupWindow.isShowing( ) )
        {
            mPopupWindow.dismiss( );
        }
    }

    private void initView( View contentView )
    {
        mYearWheel = ( AbstractWheel ) contentView.findViewById( R.id.wvYear );
        Calendar calendar = Calendar.getInstance( );
        int currentYear = calendar.get( Calendar.YEAR );
        //String thisMonth = calendar.get( Calendar.MONTH );
        if ( startYear <= 0 && endYear <= 0 )
        {
            startYear = currentYear - 150;
            endYear = currentYear + 150;
        }
        else {

            if ( startYear > 0 && endYear <= 0  )
            {
               endYear = currentYear + 150;
            }
            if ( endYear > 0 && startYear <= 0  )
            {
                startYear = currentYear - 150;
            }
        }
        AbstractWheelTextAdapter yearAdapter = new NumericWheelAdapter( mContext , startYear, endYear );
        yearAdapter.setItemResource( R.layout.item_spinner_wheel );
        yearAdapter.setItemTextResource( R.id.value );
        mYearWheel.setViewAdapter( yearAdapter );

        mYearWheel.setCyclic( true );
        if ( mScrollListener != null )
        {
            mYearWheel.addScrollingListener( mScrollListener );
        }
        else
        {
            mYearWheel.addScrollingListener( mDefaultScrollListener );
        }

        mMonthWheel = ( AbstractWheel ) contentView.findViewById( R.id.wvMonth );
        AbstractWheelTextAdapter monthAdapter = new NumericWheelAdapter( mContext , 1, 12, "%02d" );
        monthAdapter.setItemResource( R.layout.item_spinner_wheel );
        monthAdapter.setItemTextResource( R.id.value );
        mMonthWheel.setViewAdapter( monthAdapter );
        mMonthWheel.setCyclic( true );
        if ( mScrollListener != null )
        {
            mMonthWheel.addScrollingListener( mScrollListener );
        }
        else
        {
            mMonthWheel.addScrollingListener( mDefaultScrollListener );
        }

        mDayWheel = ( AbstractWheel ) contentView.findViewById( R.id.wvDay );
        initDay( thisYear, thisMonth );
        mDayWheel.setCyclic( true );
        if ( mScrollListener != null )
        {
            mDayWheel.addScrollingListener( mScrollListener );
        }

        mHourWheel = ( AbstractWheel ) contentView.findViewById( R.id.wvHour );
        AbstractWheelTextAdapter hourAdapter = new NumericWheelAdapter( mContext , 0, 23, "%02d" );
        hourAdapter.setItemResource( R.layout.item_spinner_wheel );
        hourAdapter.setItemTextResource( R.id.value );
        mHourWheel.setViewAdapter( hourAdapter );
        mHourWheel.setCyclic( true );

        mMinuteWheel = ( AbstractWheel ) contentView.findViewById( R.id.wvMinute );
        AbstractWheelTextAdapter minuteAdapter = new NumericWheelAdapter( mContext , 0, 59, "%02d" );
        minuteAdapter.setItemResource( R.layout.item_spinner_wheel );
        minuteAdapter.setItemTextResource( R.id.value );
        mMinuteWheel.setViewAdapter( minuteAdapter );
        mMinuteWheel.setCyclic( true );

        mSecondWheel = ( AbstractWheel ) contentView.findViewById( R.id.wvSecond );
        AbstractWheelTextAdapter secondAdapter = new NumericWheelAdapter( mContext , 0, 59, "%02d" );
        secondAdapter.setItemResource( R.layout.item_spinner_wheel );
        secondAdapter.setItemTextResource( R.id.value );
        mSecondWheel.setViewAdapter( minuteAdapter );
        mSecondWheel.setCyclic( true );

        mYearWheel.setCurrentItem( thisYear - startYear );
        mMonthWheel.setCurrentItem( thisMonth - 1 );
        mDayWheel.setCurrentItem( thisDay - 1 );
        mHourWheel.setCurrentItem( thisHour );
        mMinuteWheel.setCurrentItem( thisMinute );
        mSecondWheel.setCurrentItem( thisSecond );

        mConfirmBtn = ViewUtils.xFindViewById( contentView, R.id.btn_confirm );
        mConfirmBtn.setOnClickListener( mDefaultClickListener );
        mCancelBtn = ViewUtils.xFindViewById( contentView, R.id.btn_cancel );
        mCancelBtn.setOnClickListener( mDefaultClickListener );
    }

    /**
     * 设置日期
     */
    private void initDay( int currentYear, int currentMonth )
    {
        AbstractWheelTextAdapter dayAdapter = new NumericWheelAdapter( mContext ,
                1, getDay( currentYear, currentMonth ), "%02d" );
        dayAdapter.setItemResource( R.layout.item_spinner_wheel );
        dayAdapter.setItemTextResource( R.id.value );
        mDayWheel.setViewAdapter( dayAdapter );
    }

    /**
     * 计算每月的天数
     */
    private int getDay( int year, int month )
    {
        int day = 30;
        boolean flag = false;
        switch ( year % 4 )
        {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch ( month )
        {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }

    private View.OnClickListener mDefaultClickListener = new View.OnClickListener( )
    {
        @Override
        public void onClick( View v )
        {
            if ( v.equals( mConfirmBtn ) )
            {
                if ( mConfirmClickListener != null )
                {
                    int year = getYear( );
                    int month = getMonth( );
                    int day = getDay( );

                    int hour = getHour( );
                    int minute = getMinute( );
                    int second = getSecond( );
                    DateInfo dateInfo = new DateInfo.Builder( ).year( year ).month( month ).day(
                            day ).hour( hour ).minute( minute ).second( second ).build( );
                    v.setTag( dateInfo );
                    mConfirmClickListener.onClick( v );
                }
            }
            if ( mPopupWindow != null )
            {
                mPopupWindow.dismiss( );
            }
        }
    };

    /**
     * 滚动监听
     */
    OnWheelScrollListener mDefaultScrollListener = new OnWheelScrollListener( )
    {

        @Override
        public void onScrollingStarted( AbstractWheel wheel )
        {

        }

        @Override
        public void onScrollingFinished( AbstractWheel wheel )
        {
            int n_year = mYearWheel.getCurrentItem( ) + startYear;
            int n_month = mMonthWheel.getCurrentItem( ) + 1;
            initDay( n_year, n_month );
        }
    };

    /**
     * 获取年
     */
    public int getYear( )
    {
        return mYearWheel.getCurrentItem( ) + startYear;
    }

    /**
     * 获取月
     */
    public int getMonth( )
    {
        return mMonthWheel.getCurrentItem( ) + 1;

    }

    /**
     * 获取日
     */
    public int getDay( )
    {
        return mDayWheel.getCurrentItem( ) + 1;
    }

    public int getHour(){
        return  mHourWheel.getCurrentItem( );
    }

    public int getMinute(){
        return  mMinuteWheel.getCurrentItem( );
    }

    public int getSecond(){
        return  mSecondWheel.getCurrentItem( );
    }

    public CustomDatePicker hideYearOption( ){
        if ( mYearWheel != null )
        {
            mYearWheel.setVisibility( View.GONE );
        }
        return this;
    }

    public CustomDatePicker hideMonthOption( ){
        if ( mMonthWheel != null )
        {
            mMonthWheel.setVisibility( View.GONE );
        }
        return this;
    }

    public CustomDatePicker hideDayOption( ){
        if ( mDayWheel != null )
        {
            mDayWheel.setVisibility( View.GONE );
        }
        return this;
    }

    public CustomDatePicker hideHourOption( ){
        if ( mHourWheel != null )
        {
            mHourWheel.setVisibility( View.GONE );
        }
        return this;
    }

    public CustomDatePicker hideMinuteOption( ){
        if ( mMinuteWheel != null )
        {
            mMinuteWheel.setVisibility( View.GONE );
        }
        return this;
    }

    public CustomDatePicker hideSecondOption( ){
        if ( mSecondWheel != null )
        {
            mSecondWheel.setVisibility( View.GONE );
        }
        return this;
    }
}
