package com.nan.noisedetector;

import android.app.Application;

import com.nan.noisedetector.util.PreferenceUtil;

/**
 * @author nan_xu
 * @date 2019/11/22
 */
public class DetectorApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceUtil.init(this);
    }
}