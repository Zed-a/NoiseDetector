package com.nan.noisedetector.bean

import java.io.Serializable

/**
 * Created by nan on 2019-12-19.
 */
data class HistoryData(val date: String, val time: String, val max: Int, val average: Int, var location: String, var msg: String) : Serializable