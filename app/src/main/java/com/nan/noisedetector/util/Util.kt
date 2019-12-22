package com.nan.noisedetector.util

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by nan on 2019-12-19.
 */
fun getTime(): String = SimpleDateFormat("HH:mm:ss", Locale.SIMPLIFIED_CHINESE).format(Date())
fun getDate(): String = SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE).format(Date())
fun logd(tag: String, message: String) = Log.d(tag, message)
fun isEmpty(str: String?): Boolean = str==null || str.isEmpty()