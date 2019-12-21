package com.nan.noisedetector.location

import android.content.Context
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.nan.noisedetector.util.logd

object LocationManager {
    private lateinit var mLocationClient: LocationClient
    private const val TAG = "LocationManager"

    /**
     * 获取当前位置信息
     */
    fun init(context: Context) {
        logd(TAG, "init")
        mLocationClient = LocationClient(context)
    }

    fun getLocation(callback: LocationCallback) {
        mLocationClient.registerLocationListener(MyLocationListener(callback))
        requestLocation()
    }

    private fun requestLocation() {
        val option = LocationClientOption()
        option.setIsNeedAddress(true)
        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
        mLocationClient.locOption = option
        mLocationClient.start()
    }

    private class MyLocationListener internal constructor(var callback: LocationCallback) : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation) {
            logd(TAG, "onReceiveLocation")
            if (location.locType == BDLocation.TypeGpsLocation || location.locType == BDLocation.TypeNetWorkLocation)
                if (callback.action(location))
                    mLocationClient.stop()
        }
    }
}

interface LocationCallback {
    fun action(location: BDLocation): Boolean
}
