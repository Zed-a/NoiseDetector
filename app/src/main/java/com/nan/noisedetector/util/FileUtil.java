package com.nan.noisedetector.util;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    private static final String LOCAL = "NoiseDetector";

    private static final String LOCAL_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator;

    /**
     * 录音文件目录
     */
    private static final String REC_PATH = LOCAL_PATH + LOCAL + File.separator;

    /*
      自动在SD卡创建相关的目录
     */
    static {
        File dirRootFile = new File(LOCAL_PATH);
        if (!dirRootFile.exists()) {
            dirRootFile.mkdirs();
        }
        File recFile = new File(REC_PATH);
        if (!recFile.exists()) {
            recFile.mkdirs();
        }
    }


    public static File createFile(String fileName) {

        File myCaptureFile = new File(REC_PATH + fileName);
        if (myCaptureFile.exists()) {
            myCaptureFile.delete();
        }
        try {
            myCaptureFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCaptureFile;
    }


}
