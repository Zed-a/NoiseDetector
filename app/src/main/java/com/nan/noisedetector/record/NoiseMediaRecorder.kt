package com.nan.noisedetector.record

import android.media.MediaRecorder
import java.io.File
import java.io.IOException

class NoiseMediaRecorder {
    private var myRecAudioFile: File? = null
    private var mMediaRecorder: MediaRecorder? = null
    private var isRecording = false
    val maxAmplitude: Float
        get() = if (mMediaRecorder != null) {
            try {
                mMediaRecorder!!.maxAmplitude.toFloat()
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                0F
            }
        } else {
            5F
        }

    fun setMyRecAudioFile(myRecAudioFile: File?) {
        this.myRecAudioFile = myRecAudioFile
    }

    /**
     * 录音
     * @return 是否成功开始录音
     */
    fun startRecorder(): Boolean {
        if (myRecAudioFile == null) {
            return false
        }
        try {
            mMediaRecorder = MediaRecorder()
            mMediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            mMediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mMediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mMediaRecorder!!.setOutputFile(myRecAudioFile!!.absolutePath)
            mMediaRecorder!!.prepare()
            mMediaRecorder!!.start()
            isRecording = true
            return true
        } catch (exception: IOException) {
            mMediaRecorder!!.reset()
            mMediaRecorder!!.release()
            mMediaRecorder = null
            isRecording = false
            exception.printStackTrace()
        } catch (e: IllegalStateException) {
            stopRecording()
            e.printStackTrace()
            isRecording = false
        }
        return false
    }

    private fun stopRecording() {
        if (mMediaRecorder != null) {
            if (isRecording) {
                try {
                    mMediaRecorder!!.stop()
                    mMediaRecorder!!.release()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            mMediaRecorder = null
            isRecording = false
        }
    }

    fun delete() {
        stopRecording()
        if (myRecAudioFile != null) {
            myRecAudioFile!!.delete()
            myRecAudioFile = null
        }
    }
}