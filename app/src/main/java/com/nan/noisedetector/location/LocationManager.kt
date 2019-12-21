package com.nan.noisedetector.location

import android.content.Context
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption

object LocationManager {
    private lateinit var mLocationClient: LocationClient

    /**
     * 获取当前位置信息
     */
    fun init(context: Context) {
        mLocationClient = LocationClient(context)
    }

    fun getLocation(callback: LocationCallback) {
        requestLocation()
        mLocationClient.registerLocationListener(MyLocationListener(callback))
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
            if (location.locType == BDLocation.TypeGpsLocation
                    || location.locType == BDLocation.TypeNetWorkLocation) {
                if (callback.action(location)) {
                    mLocationClient.stop()
                }
            }
        }
    }
}

interface LocationCallback {
    fun action(location: BDLocation): Boolean
}
