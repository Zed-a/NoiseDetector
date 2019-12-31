package com.nan.noisedetector.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.baidu.location.BDLocation
import com.github.mikephil.charting.data.Entry
import com.nan.noisedetector.R
import com.nan.noisedetector.bean.DataBean
import com.nan.noisedetector.bean.HistoryData
import com.nan.noisedetector.event.MessageEvent
import com.nan.noisedetector.location.LocationCallback
import com.nan.noisedetector.location.LocationManager
import com.nan.noisedetector.recorder.NoiseMediaRecorder
import com.nan.noisedetector.ui.base.BaseFragment
import com.nan.noisedetector.util.*
import com.nan.noisedetector.util.DecibelUtil.clear
import com.nan.noisedetector.util.DecibelUtil.getDbCount
import com.nan.noisedetector.util.DecibelUtil.setDbCount
import com.nan.noisedetector.util.PreferenceHelper.historyRecord
import com.nan.noisedetector.util.PreferenceHelper.threshold
import kotlinx.android.synthetic.main.fragment_sound.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.support.v4.toast
import kotlin.math.log10

class SoundFragment : BaseFragment() {
    private var mThreshold = 0f
    private val mRecorder: NoiseMediaRecorder = NoiseMediaRecorder()

    private var startTime = ""
    private var endTime = ""
    private var maxDecibel = 0
    private var minDecibel = 1000
    private var totalDecibel = 0L
    private var count = 0
    private var mLocation = ""
    private var hasRequestLocation = false

    private var entries = ArrayList<Entry>()

    companion object {
        private val ALL_PERMISSIONS = arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        )

        private val NECESSARY_PERMISSIONS = arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        private const val GET_PERMISSION = 1
        private const val MSG_WHAT = 0x1001
        private const val REFRESH_TIME = 500

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sound, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
        initView()
        verifyPermissions()
        getLocation()
    }

    private fun initView() {
        btn_start_detect.setOnClickListener {
            if (checkPermissions()) {
                if (!hasRequestLocation) {
                    LocationManager.init(activity)
                    getLocation()
                }
                startRecord()
                btn_start_detect.isEnabled = false
                btn_stop_detect.isEnabled = true
            } else {
                showWaringDialog()
            }
        }
        btn_stop_detect.setOnClickListener {
            stopRecord()
            btn_start_detect.isEnabled = true
            btn_stop_detect.isEnabled = false
        }
    }

    private fun getLocation() {
        if(LocationManager.getLocation(object : LocationCallback {
            override fun action(location: BDLocation) {
                with(location) {
                    Log.d(TAG, "latitude=${latitude} longitude=${longitude} " +
                            "street=${street} number=${streetNumber}")
                    mLocation = "$city   $street$streetNumber"
                    tv_location.text = mLocation
                    logD(TAG, "getLocation mLocation=$mLocation")
                }
            }
        })) hasRequestLocation = true
    }

    private fun verifyPermissions() = ActivityCompat.requestPermissions(activity, ALL_PERMISSIONS, GET_PERMISSION)

    private fun checkPermissions(): Boolean {
        for (permission: String in NECESSARY_PERMISSIONS) {
            logD(TAG, "permission=$permission")
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }

    private fun showWaringDialog() {
        AlertDialog.Builder(activity)
                .setTitle("警告！")
                .setMessage("请前往设置中打开相关权限(录音、读取存储)，否则功能无法正常运行！")
                .show()
    }

    override fun onResume() {
        super.onResume()
        mThreshold = threshold
    }

    @SuppressLint("HandlerLeak")
    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (hasMessages(MSG_WHAT)) {
                return
            }
            val volume = mRecorder.maxAmplitude //获取声压值
            if (volume > 0 && volume < 1000000) {
                setDbCount(20 * log10(volume.toDouble()).toFloat()) //将声压值转为分贝值并平滑处理
                //mSoundDiscView.refresh();
                with(getDbCount()) {
                    //showNotification(dbCount);
                    val intCount = this.toInt()
                    maxDecibel = if (intCount > maxDecibel) intCount else maxDecibel
                    minDecibel = if (intCount in 1 until minDecibel) intCount else minDecibel
                    totalDecibel += intCount
                    entries.add(Entry(count.toFloat()/2, this))
                    count++
                    soundDiscView.text = intCount.toString()
                }

            }
            sendEmptyMessageDelayed(MSG_WHAT, REFRESH_TIME.toLong())
        }
    }

    /**
     * 开始记录
     */
    private fun startRecord() {
        initRecordData()
        val file = FileUtil.createFile("temp.amr")
        if (file != null) {
            Log.d(TAG, "file path=" + file.path)
            try {
                mRecorder.setMyRecAudioFile(file)
                if (mRecorder.startRecorder()) {
                    handler.sendEmptyMessage(MSG_WHAT)
                } else {
                    toast("启动录音失败")
                }
            } catch (e: Exception) {
                toast("录音机已被占用或录音权限被禁止")
                e.printStackTrace()
            }
        } else {
            toast("创建文件失败")
        }
    }

    private fun initRecordData() {
        maxDecibel = 0
        minDecibel = 1000
        totalDecibel = 0
        startTime = getTime()
        count = 0
    }

    private fun stopRecord() {
        logD(TAG, "stopRecorder mLocation=$mLocation")
        endTime = getTime()
        val list = historyRecord
        if (count>5) {
            list.add(DataBean(HistoryData(
                    getDate(),
                    "$startTime-$endTime",
                    maxDecibel,
                    (totalDecibel / count).toInt(),
                    if (isEmpty(mLocation)) "定位失败" else mLocation, "")
                    , entries))
            historyRecord = list
            Log.d(TAG, "post event")
            EventBus.getDefault().post(MessageEvent())
        }
        entries = ArrayList()
        mRecorder.delete()
        handler.removeMessages(MSG_WHAT)
        clear()
        soundDiscView.text = "0"
    }

    override fun onDestroy() {
        stopRecord()
        super.onDestroy()
    }
}