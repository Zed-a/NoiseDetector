package com.nan.noisedetector.bean

import com.bin.david.form.annotation.SmartColumn
import com.bin.david.form.annotation.SmartTable

/**
 * Created by nan on 2019-12-19.
 */
@SmartTable(name = "历史记录")
class HistoryData(
        @field:SmartColumn(id = 0, name = "时间") private val time: String,
        @field:SmartColumn(id = 1, name = "最大分贝") private val max: Int,
        @field:SmartColumn(id = 2, name = "最小分贝") private val min: Int,
        @field:SmartColumn(id = 3, name = "平均分贝") private val average: Int)