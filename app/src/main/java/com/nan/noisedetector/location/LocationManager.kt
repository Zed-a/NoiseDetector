package com.nan.noisedetector.location

import android.content.Context
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.nan.noisedetector.util.logD

object LocationManager {
    private var mLocationClient: LocationClient? = null
    private const val TAG = "LocationManager"

    /**
     * 获取当前位置信息
     */
    fun init(context: Context) {
        logD(TAG, "init")
        if (mLocationClient == null) mLocationClient = LocationClient(context)
    }

    fun getLocation(callback: LocationCallback): Boolean {
        if (mLocationClient == null) return false
        mLocationClient!!.registerLocationListener(MyLocationListener(callback))
        requestLocation()
        return true
    }

    private fun requestLocation() {
        logD(TAG, "requestLocation")
        val option = LocationClientOption()
        option.setIsNeedAddress(true)
        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
        option.setScanSpan(5000)
        mLocationClient!!.locOption = option
        mLocationClient!!.start()
    }

    private class MyLocationListener internal constructor(var callback: LocationCallback) : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation) {
            logD(TAG, "onReceiveLocation")
            if (location.locType == BDLocation.TypeGpsLocation || location.locType == BDLocation.TypeNetWorkLocation)
                callback.action(location)
        }
    }
}

interface LocationCallback {
    fun action(location: BDLocation)
}
