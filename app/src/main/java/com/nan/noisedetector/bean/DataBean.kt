package com.nan.noisedetector.bean

import com.github.mikephil.charting.data.Entry

/**
 * @author nan_xu
 * @date 2019/12/27
 */
data class DataBean (var historyData: HistoryData, val entries: ArrayList<Entry>)