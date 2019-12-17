package com.nan.noisedetector.util

object DecibelUtil {
    private var dbCount = 0f

    private var lastDbCount = dbCount

    fun getDbCount(): Float {
        return dbCount
    }

    fun setDbCount(dbValue: Float) {
        //设置声音最低变化
        val min = 0.5f
        // 声音分贝值
        val value: Float
        if (dbValue > lastDbCount) {
            value = if (dbValue - lastDbCount > min) dbValue - lastDbCount else min
        } else {
            value = if (dbValue - lastDbCount < -min) dbValue - lastDbCount else -min
        }
        dbCount = lastDbCount + value * 0.2f //防止声音变化太快
        lastDbCount = dbCount
    }

    fun clear() {
        dbCount = 0f
        lastDbCount = 0f
    }

}
