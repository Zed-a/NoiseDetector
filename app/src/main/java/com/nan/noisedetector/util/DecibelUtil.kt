package com.nan.noisedetector.util

object DecibelUtil {
    private var dbCount = 0f

    fun getDbCount(): Float {
        return dbCount
    }

    fun setDbCount(dbValue: Float) {
        dbCount += (dbValue - dbCount) //防止声音变化太快
    }

    fun clear() {
        dbCount = 0f
    }

}
