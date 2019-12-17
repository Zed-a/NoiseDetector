package com.nan.noisedetector.util

/**
 * Created by nan on 2019-11-21.
 */
object PreferenceHelper {
    private const val KEY_OPEN_NOTIFY = "key_open_notify"

    var isOpenNotify: Boolean
        get() = PreferenceUtil.getInstance().getBoolean(KEY_OPEN_NOTIFY, false)
        set(openNotify) {
            PreferenceUtil.getInstance().putBoolean(KEY_OPEN_NOTIFY, openNotify)
        }

    private const val KEY_THRESHOLD = "key_threshold"

    var threshold: Float
        get() = PreferenceUtil.getInstance().getFloat(KEY_THRESHOLD, 20f)
        set(threshold) {
            PreferenceUtil.getInstance().putFloat(KEY_THRESHOLD, threshold)
        }
}
