package net.performance.assessment.common;


import com.baidu.mapapi.SDKInitializer;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;

import net.performance.assessment.utils.CustomPushManager;
import net.performance.assessment.utils.GlideImageLoader;
import net.performance.assessment.utils.location.LocationService;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class BaseApplication extends Application {

    public final static int KEEP_LIVE_SERVICE_ID = 1001;

    public LocationService locationService;
    public Vibrator mVibrator;

    private List<Activity> oList;//用于存放所有启动的Activity的集合

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());

        oList = new ArrayList<Activity>();

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .imageDecoder(new BaseImageDecoder(true))
                .build();
        ImageLoader.getInstance().init(configuration);

        //AppInitializeService.start(this);

        Context context = getApplicationContext( );
        CustomPushManager.getInstance( ).enableDebug( context , true );
        CustomPushManager.getInstance().register( context );

        initImagePicker();

        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
    }

    /**
     * 图片选择器初始
     */
    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        //设置图片加载器
        imagePicker.setImageLoader(new GlideImageLoader());
        //显示拍照按钮
        imagePicker.setShowCamera(true);
        //允许裁剪（单选才有效）
        imagePicker.setCrop(false);
        //是否按矩形区域保存
        imagePicker.setSaveRectangle(false);
        imagePicker.setMultiMode(false);
        //选中数量限制
        imagePicker.setSelectLimit(1);
        //裁剪框的形状
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusWidth(900);
        //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(900);
        //保存文件的宽度。单位像素
        imagePicker.setOutPutX(1000);
        //保存文件的高度。单位像素
        imagePicker.setOutPutY(1000);
    }

    /**
     * 添加Activity
     */
    public void addActivity(Activity activity) {
    // 判断当前集合中不存在该Activity
        if (!oList.contains(activity)) {
            oList.add(activity);//把当前Activity添加到集合中
        }
    }

    /**
     * 销毁单个Activity
     */
    public void removeActivity(Activity activity) {
    //判断当前集合中存在该Activity
        if (oList.contains(activity)) {
            oList.remove(activity);//从集合中移除
            activity.finish();//销毁当前Activity
        }
    }

    /**
     * 销毁所有的Activity
     */
    public void removeALLActivity() {
        //通过循环，把集合中的所有Activity销毁
        for (Activity activity : oList) {
            activity.finish();
        }
    }
}
