package net.performance.assessment.network.image;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import android.content.Context;
import android.util.Log;

/**
 *
 */
@GlideModule
public class CustomGlideModule extends AppGlideModule
{

    private final static String DEFAULT_CACHE_FOLDER = "performance_assessment";

    @Override
    public void applyOptions( Context context, GlideBuilder builder )
    {
        //设置内存缓存大小，通过考虑设备给定的可用内存和屏幕大小得出合理的默认大小
        /*MemorySizeCalculator calculator = new MemorySizeCalculator.Builder( context )
                .setMemoryCacheScreens( 2 ).build( );
        LruResourceCache cache = new LruResourceCache( calculator.getMemoryCacheSize() );
        //用于配置Glide的内存缓存策略，默认配置是LruResourceCache
        builder.setMemoryCache( cache );
        //用于配置Glide的Bitmap缓存池，默认配置是LruBitmapPool
        builder.setBitmapPool( new LruBitmapPool( calculator.getBitmapPoolSize( ) ) );*/

        //设置内置磁盘缓存大小和外置磁盘缓存大小，并指定路径
        /*int diskCacheSize = 100 * 1024 * 1024;
        builder.setDiskCache( new InternalCacheDiskCacheFactory( context , DEFAULT_CACHE_FOLDER , diskCacheSize ) );
        builder.setDiskCache( new ExternalPreferredCacheDiskCacheFactory( context , DEFAULT_CACHE_FOLDER , diskCacheSize ) );*/

        /*GlideExecutor diskCacheExecutor = GlideExecutor.newDiskCacheExecutor(  );
        builder.setDiskCacheExecutor( diskCacheExecutor );//用于配置Glide读取缓存中图片的异步执行器，默认配置是FifoPriorityThreadPoolExecutor，也就是先入先出原则

        GlideExecutor resizeExecutor = GlideExecutor.newSourceExecutor( );
        builder.setResizeExecutor( resizeExecutor );//用于配置Glide读取非缓存中图片的异步执行器，默认配置也是FifoPriorityThreadPoolExecutor*/

        builder.setLogLevel( Log.VERBOSE );

        //GlideExecutor executor = GlideExecutor.calculateBestThreadCount( );
    }

    @Override
    public void registerComponents( Context context, Glide glide, Registry registry )
    {
        super.registerComponents( context, glide, registry );
    }

    @Override
    public boolean isManifestParsingEnabled( )
    {
        return false;
    }
}
