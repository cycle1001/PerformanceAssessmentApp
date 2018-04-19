package net.performance.assessment.utils.location;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import net.performance.assessment.common.BaseApplication;
import net.performance.assessment.listener.LocationResultListener;

public class LocationUtils {

    private static LocationService locationService;
    private static Object objLock = new Object();

    public static boolean isLocationEnabled(Context ctx) {
        int locationMode;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(ctx.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public static void startLocation(Activity activity, LocationResultListener resultListener) {
        synchronized (objLock) {
            locationService = ((BaseApplication) activity.getApplication()).locationService;
            locationService.registerListener(resultListener);
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
            locationService.start();
        }
    }

    public static void stopLocation(LocationResultListener resultListener) {
        synchronized (objLock) {
            if (locationService != null) {
                //注销掉监听
                locationService.unregisterListener(resultListener);
                //停止定位服务
                locationService.stop();
                locationService = null;
            }
        }
    }
}
