package net.performance.assessment.utils;

/**
 *
 */

public class HexUtils
{
    private static final char[ ] DIGITS =
            { '0' , '1' , '2' , '3' , '4' , '5' , '6' , '7' , '8' , '9' , 'a' , 'b' , 'c' , 'd' ,
              'e' , 'f' };

    public static String toHex( byte[ ] data )
    {
        StringBuffer buffer = new StringBuffer( );
        for ( int i = 0 ; i < data.length ; i++ )
        {
            buffer.append( String.format( "%02x" , data[ i ] ) );
        }
        return buffer.toString( );
    }

    public static byte[ ] toByte( String hexData )
    {
        if ( hexData == null )
        {
            return null;
        }
        int len = hexData.length( );
        if ( (len & 0x1) == 1 )
        {
            return null;
        }
        int dataLength = len / 2;
        byte[ ] result = new byte[ dataLength ];
        for ( int i = 0 ; i < dataLength ; i++ )
        {
            result[ i ] = ( byte ) Integer.parseInt( hexData.substring( i * 2 , i * 2 + 2 ) ,
                    16 );
        }
        return result;
    }

    public static String encodeHexString( byte[ ] data )
    {
        char[ ] charArray = encodeHex( data );
        return new String( charArray );
    }

    public static byte[ ] decodeHexString( String hexData )
    {
        return decodeHex( hexData.toCharArray( ) );
    }

    public static byte[ ] decodeHex( char[ ] data )
    {
        int len = data.length;

        if ( ( len & 0x1 ) != 0 )
        {
            throw new RuntimeException( "Odd number of characters." );
        }

        byte[ ] out = new byte[ len >> 1 ];

        int i = 0;
        for ( int j = 0 ; j < len ; i++ )
        {
            int f = toDigit( data[ j ] , j ) << 4;
            j++;
            f |= toDigit( data[ j ] , j );
            j++;
            out[ i ] = ( byte ) ( f & 0xFF );
        }

        return out;
    }

    protected static int toDigit( char ch , int index )
    {
        int digit = Character.digit( ch , 16 );
        if ( digit == -1 )
        {
            throw new RuntimeException( "Illegal hexadecimal charcter " + ch + " at index "
                                        + index );
        }
        return digit;
    }

    public static char[ ] encodeHex( byte[ ] data )
    {
        int l = data.length;

        char[ ] out = new char[ l << 1 ];

        int i = 0;
        for ( int j = 0 ; i < l ; i++ )
        {
            out[ ( j++ ) ] = DIGITS[ ( ( 0xF0 & data[ i ] ) >>> 4 ) ];
            out[ ( j++ ) ] = DIGITS[ ( 0xF & data[ i ] ) ];
        }

        return out;
    }

    public byte[ ] decode( byte[ ] array )
    {
        return decodeHex( new String( array ).toCharArray( ) );
    }

    public byte[ ] decode( Object object )
    {
        try
        {
            char[ ] charArray = ( object instanceof String ) ? ( ( String ) object )
                    .toCharArray( ) : ( char[ ] ) object;
            return decodeHex( charArray );
        }
        catch ( ClassCastException e )
        {

            throw new RuntimeException( e.getMessage( ) );
        }
    }

    public byte[ ] encode( byte[ ] array )
    {
        return new String( encodeHex( array ) ).getBytes( );
    }

    public char[ ] encode( Object object )
    {
        try
        {
            byte[ ] byteArray = ( object instanceof String ) ? ( ( String ) object )
                    .getBytes( ) : ( byte[ ] ) object;
            return encodeHex( byteArray );
        }
        catch ( ClassCastException e )
        {

            throw new RuntimeException( e.getMessage( ) );
        }
    }
}
