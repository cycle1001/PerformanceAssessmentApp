package net.performance.assessment.utils;

import net.performance.assessment.entity.DateInfo;

import android.content.Context;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 *
 */

public class CommonUtils
{
    private static float density;

    /*private void testGlide(){
        GlideApp.with( mContext ).load( "http://imgm.tiyushe.cn/images/edit/2017/09/15/ceef7cf703d2cb43271c109d721b8108.jpg" )
                .placeholder( new ColorDrawable( Color.GREEN ) )
                .error( new ColorDrawable( Color.RED ) )
                .thumbnail( 0.2f )
                .centerCrop( )
                .circleCrop( )
                .into( ivTaskImage );
    }*/

    /*private void test( ){
        String[] functionDescArray = getResources( ).getStringArray(
                R.array.attendance_function_arrays );
        int[] iconArray = {
                R.mipmap.ic_launcher , R.mipmap.ic_launcher
        };

        List< FunctionIconInfo > mAttendanceList = new ArrayList<>( );
        for ( int i = 0; i < iconArray.length; i++ )
        {
            FunctionIconInfo info = new FunctionIconInfo( );
            info.functionIconRes = iconArray[ i ];
            info.functionDesc = functionDescArray[ i ];
            mAttendanceList.add( info );
        }
        BaseOptionPickerAdapter<FunctionIconInfo> adapter = new BaseOptionPickerAdapter<FunctionIconInfo>( mAttendanceList )
        {
            @Override
            protected void setConvertView( ViewHolder holder, int position, Context context )
            {
                FunctionIconInfo info = mDataList.get( position );
                TextView tvContent = holder.getItemView( R.id.content );
                tvContent.setText( info.functionDesc );
            }

        };
        BaseOptionPicker<FunctionIconInfo> picker = new BaseOptionPicker<>( mContext ,  adapter );
        picker.setItemClickListener( new CustomItemClickListener( )
        {
            @Override
            public void onItemClick( int position, Object data )
            {
                FunctionIconInfo info = ( FunctionIconInfo ) data;
                ToastUtil.showTip( mContext ,  "data===" + info.functionDesc );
            }
        } );
        picker.show( tvTaskId );
    }*/

    public static String generateTaskId( String taskType, String userId, String timeStamp )
    {
        StringBuilder builder = new StringBuilder( taskType );
        builder.append( userId );

        /*String pattern = "yyyyMMddHHmmss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( pattern );
        String timeStamp = simpleDateFormat.format( new Date( ) );*/

        builder.append( timeStamp );
        return builder.toString( );
    }

    public static int compareDateInfo( DateInfo startDate, DateInfo endDate )
    {
        if ( startDate == null || endDate == null )
        {
            return - 1;
        }

        if ( startDate.year > endDate.year )
        {
            return - 1;
        }
        else if ( startDate.year == endDate.year )
        {
            if ( startDate.month > endDate.month )
            {
                return - 1;
            }
            else if ( startDate.month == endDate.month )
            {
                if ( startDate.day > endDate.day )
                {
                    return - 1;
                }
                else
                {
                    return 1;
                }
            }
            else
            {
                return 1;
            }
        }
        else
        {
            return 1;
        }
    }

    public static DateInfo createCurrentDateInfo( )
    {
        Calendar calendar = Calendar.getInstance( );
        int currentYear = calendar.get( Calendar.YEAR );
        int currentMonth = calendar.get( Calendar.MONTH ) + 1;
        int currentDay = calendar.get( Calendar.DAY_OF_MONTH );
        DateInfo dateInfo = new DateInfo.Builder( ).year( currentYear ).month( currentMonth ).day(
                currentDay ).build( );
        return dateInfo;
    }

    /** dip转px */
    public static int dipToPx( Context context, int dip )
    {
        if ( density <= 0 )
        {
            density = context.getResources( ).getDisplayMetrics( ).density;
        }
        return ( int ) ( dip * density + 0.5f );
    }

    public static String trimDoubleToString( double num )
    {
        //使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("0.00").format(num );

        /*String strNum = String.valueOf( num );
        int n = strNum.indexOf( "." );
        if ( n > 0 )
        {
            //截取小数点后的数字
            String dotNum = strNum.substring( n + 1 );
            if ( "0".equals( dotNum ) )
            {
                return strNum + "0";
            }
            else
            {
                if ( dotNum.length( ) == 1 )
                {
                    return strNum + "0";
                }
                else
                {
                    return strNum;
                }
            }
        }
        else
        {
            return strNum + ".00";
        }*/
    }

    public static void exitApp( )
    {
        android.os.Process.killProcess( android.os.Process.myPid( ) );   //获取PID
        System.exit( 0 );   //常规java、c#的标准退出法，返回值为0代表正常退出
    }
}
