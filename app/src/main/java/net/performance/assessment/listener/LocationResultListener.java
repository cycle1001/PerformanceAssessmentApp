package net.performance.assessment.listener;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

import net.performance.assessment.entity.LocationResultBean;
import net.performance.assessment.interfaces.LocationCallBackListener;
import net.performance.assessment.utils.LogUtils;

/**
 * 百度定位返回结果监听
 */
public class LocationResultListener extends BDAbstractLocationListener {

    private LocationCallBackListener mCallBackListener;

    public LocationResultListener(LocationCallBackListener callBackListener) {
        super();
        this.mCallBackListener = callBackListener;
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        LocationResultBean resultBean = new LocationResultBean();
        if (null != bdLocation && bdLocation.getLocType() != BDLocation.TypeServerError) {
            resultBean.locType = bdLocation.getLocType();
            resultBean.locTypeDescription = bdLocation.getLocTypeDescription();
            resultBean.latitude = bdLocation.getLatitude();
            resultBean.longitude = bdLocation.getLongitude();
            resultBean.locationAddr = bdLocation.getAddrStr();

            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果

            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果

            } else if (bdLocation.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果

            } else if (bdLocation.getLocType() == BDLocation.TypeServerError) {
                resultBean.locationErrDescribe = "服务端网络定位失败";
                // ，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因
            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkException) {
                resultBean.locationErrDescribe = "网络不桶导致定位失败，请检查网络是否通畅";
                // 网络不同导致定位失败，请检查网络是否通畅
            } else if (bdLocation.getLocType() == BDLocation.TypeCriteriaException) {
                resultBean.locationErrDescribe = "无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机";
                // 无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机
            }
        } else {
            resultBean.locationErrDescribe = "定位失败";
        }
        if (mCallBackListener != null) {
            mCallBackListener.onLocationResult(resultBean);
        }
    }
}
