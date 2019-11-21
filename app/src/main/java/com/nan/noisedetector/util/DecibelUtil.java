package com.nan.noisedetector.util;

public class DecibelUtil {

	public static float dbCount = 0;

	private static float lastDbCount = dbCount;

	public static void setDbCount(float dbValue) {
		//设置声音最低变化
		float min = 0.5f;
		// 声音分贝值
		float value;
		if (dbValue > lastDbCount) {
			value = dbValue - lastDbCount > min ? dbValue - lastDbCount : min;
		}else{
			value = dbValue - lastDbCount < -min ? dbValue - lastDbCount : -min;
		}
		dbCount = lastDbCount + value * 0.2f ; //防止声音变化太快
		lastDbCount = dbCount;
	}
	
}
