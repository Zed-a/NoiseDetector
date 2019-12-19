package com.nan.noisedetector.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by nan on 2019-12-19.
 */
fun getFormatTime() = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE).format(Date())