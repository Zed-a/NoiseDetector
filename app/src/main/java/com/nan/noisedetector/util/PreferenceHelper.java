package com.nan.noisedetector.util;

/**
 * Created by nan on 2019-11-21.
 */
public class PreferenceHelper {
    private static final String KEY_OPEN_NOTIFY = "key_open_notify";

    public static void setOpenNotify(boolean openNotify) {

    }

    public static boolean isOpenNotify() {
         return true;
    }

    private static final String KEY_THRESHOLD = "key_threshold";


    public static void setThreshold(float threshold) {

    }

    public static float getThreshold() {
        return 20;
    }
}
