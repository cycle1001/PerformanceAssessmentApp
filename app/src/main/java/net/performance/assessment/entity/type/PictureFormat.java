package net.performance.assessment.entity.type;

/**
 *
 */

public class PictureFormat
{
    public static final String JPG = "jpg";

    public static final String JPEG = "jpeg";

    public static final String PNG = "png";

    public static final String GIF = "gif";

    public static String getFormat( String mimeType ){

        String format = "";
        if ( mimeType.contains( JPG ) || mimeType.contains( JPEG ) )
        {
            format = JPG;
        }
        else if ( mimeType.contains( PNG ) )
        {
            format = PNG;
        }
        else if ( mimeType.contains( GIF ) )
        {
            format = GIF;
        }
        return format;
    }
}
