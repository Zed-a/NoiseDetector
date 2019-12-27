package com.nan.noisedetector.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nan.noisedetector.bean.DataBean

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

    private const val KEY_HISTORY_RECORD = "key_history_record"
    var historyRecord: ArrayList<DataBean>
        get() =
            with(PreferenceUtil.getInstance().getString(KEY_HISTORY_RECORD, "")) {
                if (this.isEmpty()) {
                    return ArrayList()
                }
                val type = object: TypeToken<List<DataBean>>() {}.type
                return Gson().fromJson(this, type)
            }
        set(value) {
            val json: String = Gson().toJson(value)
            PreferenceUtil.getInstance().putString(KEY_HISTORY_RECORD, json)
        }
}
