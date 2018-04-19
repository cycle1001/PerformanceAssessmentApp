package net.performance.assessment.entity;

/**
 *
 */
public class DateInfo
{
    public int year;

    public int month;

    public int day;

    private int hour;

    private int minute;

    private int second;

    private DateInfo( Builder builder )
    {
        this.year = builder.year;
        this.month = builder.month;
        this.day = builder.day;
        this.hour = builder.hour;
        this.minute = builder.minute;
        this.second = builder.second;
    }


    public int getYear( )
    {
        return year;
    }

    public int getMonth( )
    {
        return month;
    }

    public int getDay( )
    {
        return day;
    }

    public int getHour( )
    {
        return hour;
    }

    public void setHour( int hour )
    {
        this.hour = hour;
    }

    public int getMinute( )
    {
        return minute;
    }

    public void setMinute( int minute )
    {
        this.minute = minute;
    }

    public int getSecond( )
    {
        return second;
    }

    public void setSecond( int second )
    {
        this.second = second;
    }

    public String getFormatStr( CharSequence concatStr )
    {
        StringBuilder builder = new StringBuilder( );
        builder.append( year );
        builder.append( concatStr );
        builder.append( month > 9 ? String.valueOf( month ) : "0" + String.valueOf( month ) );
        builder.append( concatStr );
        builder.append( day > 9 ? String.valueOf( day ) : "0" + String.valueOf( day ) );
        return builder.toString( );
    }

    public String getDateFormatStr( CharSequence concatStr )
    {
        StringBuilder builder = new StringBuilder( );
        builder.append( year );
        builder.append( concatStr );
        builder.append( month > 9 ? String.valueOf( month ) : "0" + String.valueOf( month ) );
        builder.append( concatStr );
        builder.append( day > 9 ? String.valueOf( day ) : "0" + String.valueOf( day ) );
        return builder.toString( );
    }

    public String getTimeFormatStr( CharSequence concatStr )
    {
        StringBuilder builder = new StringBuilder( );
        builder.append( hour > 9 ? String.valueOf( hour ) : "0" + String.valueOf( hour ) );
        builder.append( concatStr );
        builder.append( minute > 9 ? String.valueOf( minute ) : "0" + String.valueOf( minute ) );
        builder.append( concatStr );
        builder.append( second > 9 ? String.valueOf( second ) : "0" + String.valueOf( second ) );
        return builder.toString( );
    }

    public static class Builder
    {
        private int year;
        private int month;
        private int day;
        private int hour;
        private int minute;
        private int second;

        public Builder year( int year )
        {
            this.year = year;
            return this;
        }

        public Builder month( int month )
        {
            this.month = month;
            return this;
        }

        public Builder day( int day )
        {
            this.day = day;
            return this;
        }

        public Builder hour( int hour )
        {
            this.hour = hour;
            return this;
        }

        public Builder minute( int minute )
        {
            this.minute = minute;
            return this;
        }

        public Builder second( int second )
        {
            this.second = second;
            return this;
        }

        public Builder fromPrototype( DateInfo prototype )
        {
            year = prototype.year;
            month = prototype.month;
            day = prototype.day;
            hour = prototype.hour;
            minute = prototype.minute;
            second = prototype.second;
            return this;
        }

        public DateInfo build( )
        {
            return new DateInfo( this );
        }
    }

    @Override
    public String toString( )
    {
        return "DateInfo{" + "year=" + year + ", month=" + month + ", day=" + day + ", hour=" + hour + ", minute=" + minute + ", second=" + second + '}';
    }
}
