package com.nan.noisedetector.util;

/**
 * Created by nan on 2019-11-21.
 */
public class PreferenceHelper {
    private static final String KEY_OPEN_NOTIFY = "key_open_notify";

    public static void setOpenNotify(boolean openNotify) {
        PreferenceUtil.getInstance().putBoolean(KEY_OPEN_NOTIFY, openNotify);
    }

    public static boolean isOpenNotify() {
        return PreferenceUtil.getInstance().getBoolean(KEY_OPEN_NOTIFY, false);
    }

    private static final String KEY_THRESHOLD = "key_threshold";


    public static void setThreshold(float threshold) {
        PreferenceUtil.getInstance().putFloat(KEY_THRESHOLD, threshold);
    }

    public static float getThreshold() {
        return PreferenceUtil.getInstance().getFloat(KEY_THRESHOLD, 20);
    }
}
