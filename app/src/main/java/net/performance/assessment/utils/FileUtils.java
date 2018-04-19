package net.performance.assessment.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Writer;

/**
 *
 */

public class FileUtils
{

    public static final String FILE_NAME = /*File.separator + */"./payment_record.txt";

    /**
     * 生成.json格式文件
     */
    public static boolean createJsonFile( String jsonString , String filePath , String fileName ) {
        // 标记文件生成是否成功
        boolean flag = true;

        // 拼接文件完整路径
        String fullPath = filePath + File.separator + fileName + ".json";

        // 生成json格式文件
        try {
            // 保证创建一个新文件
            File file = new File( fullPath );
            if ( !file.getParentFile( ).exists( ) ) { // 如果父目录不存在，创建父目录
                file.getParentFile( ).mkdirs( );
            }
            if ( file.exists( ) ) { // 如果已存在,删除旧文件
                file.delete( );
            }
            file.createNewFile( );

            // 格式化json字符串
            // jsonString = JsonFormatTool.formatJson(jsonString);

            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter( new FileOutputStream( file ) , "UTF-8" );
            write.write( jsonString );
            write.flush( );
            write.close( );
        }
        catch ( Exception e ) {
            flag = false;
            e.printStackTrace( );
        }

        // 返回是否成功的标记
        return flag;
    }

    /**
     * 创建文件
     *
     * @param fileName
     * @return
     */
    public static boolean createFile( File fileName ) throws Exception {
        boolean flag = false;
        try {
            if ( !fileName.exists( ) ) {
                fileName.createNewFile( );
                flag = true;
            }
        }
        catch ( Exception e ) {
            e.printStackTrace( );
        }
        return true;
    }

    /**
     * 读TXT文件内容
     *
     * @param fileName
     * @return
     */
    public static String readTxtFile( File fileName ) throws Exception {
        String result = "";
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader( fileName );
            bufferedReader = new BufferedReader( fileReader );
            try {
                String read = "";
                while ( ( read = bufferedReader.readLine( ) ) != null ) {
                    result = result + read /*+ "\r\n"*/;
                }
            }
            catch ( Exception e ) {
                e.printStackTrace( );
            }
        }
        catch ( Exception e ) {
            e.printStackTrace( );
        }
        finally {
            if ( bufferedReader != null ) {
                bufferedReader.close( );
            }
            if ( fileReader != null ) {
                fileReader.close( );
            }
        }
        System.out.println( "读取出来的文件内容是：" + "\r\n" + result );
        return result;
    }


    public static boolean writeTxtFile( String content , File fileName ) throws Exception {
        RandomAccessFile mm = null;
        boolean flag = false;
        FileOutputStream o = null;
        try {
            o = new FileOutputStream( fileName );
            o.write( content.getBytes( "UTF-8" ) );
            o.close( );
            // mm=new RandomAccessFile(fileName,"rw");
            // mm.writeBytes(content);
            flag = true;
        }
        catch ( Exception e ) {
            // TODO: handle exception
            e.printStackTrace( );
        }
        finally {
            if ( mm != null ) {
                mm.close( );
            }
        }
        return flag;
    }

    public static void contentToTxt( String filePath , String content ) {
        String str = new String( ); // 原有txt内容
        String s1 = new String( );// 内容更新
        try {
            File f = new File( filePath );
            if ( f.exists( ) ) {
                System.out.print( "文件存在" );
            }
            else {
                System.out.print( "文件不存在" );
                f.createNewFile( );// 不存在则创建
            }
            BufferedReader input = new BufferedReader( new FileReader( f ) );

            while ( ( str = input.readLine( ) ) != null ) {
                s1 += str + "\n";
            }
            System.out.println( s1 );
            input.close( );
            s1 += content;

            BufferedWriter output = new BufferedWriter( new FileWriter( f ) );
            output.write( s1 );
            output.close( );
        }
        catch ( Exception e ) {
            e.printStackTrace( );

        }
    }

    public static byte[ ] readFile( String fileName ) {
        byte[ ] data = null;
        File file = new File( fileName );
        if ( file == null || !file.exists( ) ) {
            Log.v( "FileUtil" , "no file---" );
            return data;
        }
        FileInputStream fis = null;
        int length = ( int ) file.length( );
        Log.v( "FileUtil" , "file length---" + length );
        try {
            fis = new FileInputStream( file );
            data = new byte[ length ];
            fis.read( data );
        }
        catch ( Exception e ) {
            // TODO Auto-generated catch block
            e.printStackTrace( );
        }
        finally{
            if ( fis != null ) {
                try {
                    fis.close( );
                }
                catch ( IOException e ) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return data;
    }
}
