package com.nan.noisedetector

import android.app.Application
import com.nan.noisedetector.location.LocationManager
import com.nan.noisedetector.util.PreferenceUtil

/**
 * @author nan_xu
 * @date 2019/11/22
 */
class DetectorApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        PreferenceUtil.init(this)
        LocationManager.init(this)
    }
}