package com.nan.noisedetector.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.baidu.location.BDLocation
import com.baidu.location.Poi
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
import com.nan.noisedetector.util.PreferenceHelper.isOpenNotify
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

    private var entries = ArrayList<Entry>()

    companion object {
        private val PERMISSIONS = arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.INTERNET
        )
        private const val GET_PERMISSION = 1
        private const val MSG_WHAT = 0x1001
        private const val REFRESH_TIME = 500

        fun isAppAlive(context: Context, packageName: String): Int {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val listInfos = activityManager.getRunningTasks(20)
            // 判断程序是否在栈顶
            return if (listInfos[0].topActivity.packageName == packageName) {
                1
            } else { // 判断程序是否在栈里
                for (info in listInfos) {
                    if (info.topActivity.packageName == packageName) {
                        return 2
                    }
                }
                0 // 栈里找不到，返回3
            }
        }
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
        //        mSoundDiscView = findViewById(R.id.soundDiscView);
        btn_start_detect.setOnClickListener {
            startRecord()
            btn_start_detect.isEnabled = false
            btn_stop_detect.isEnabled = true
        }
        btn_stop_detect.setOnClickListener {
            stopRecord()
            btn_start_detect.isEnabled = true
            btn_stop_detect.isEnabled = false
        }
    }

    private fun getLocation() {
        LocationManager.getLocation(object : LocationCallback {
            override fun action(location: BDLocation) {
                with(location) {
                    Log.d(TAG, "latitude=${latitude} longitude=${longitude} " +
                            "street=${street} number=${streetNumber}")
                    mLocation = street+streetNumber
                    tv_location.text = mLocation
                    logd(TAG, "getLocation mLocation=$mLocation")
                }
            }
        })
    }

    private fun verifyPermissions() = ActivityCompat.requestPermissions(activity, PERMISSIONS, GET_PERMISSION)

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

    private fun showNotification(dbCount: Float) {
        if (isOpenNotify && dbCount < mThreshold) {
            return
        }
        val builder = Notification.Builder(activity)
//        var intent: Intent
        //        PendingIntent pendingIntent = PendingIntent.getActivity(SoundActivity.this,0,intent,0);  //点击跳转
        builder.setSmallIcon(R.mipmap.ic_launcher) //小图标，在大图标右下角
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)) //大图标，没有设置时小图标就是大图标
//            .setContentIntent(pendingIntent)
                .setAutoCancel(true) //点击的时候消失
                .setContentTitle("噪音超限")
                .setContentText("当前噪音分贝为$dbCount,大于阈值$mThreshold")
        val manager = activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1, builder.build())
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
        logd(TAG, "stopRecorder mLocation=$mLocation")
        endTime = getTime()
        val list = historyRecord
        if (count>5) {
            list.add(DataBean(HistoryData(
                    getDate(),
                    "$startTime-$endTime",
                    maxDecibel,
                    (totalDecibel / count).toInt(),
                    if (isEmpty(mLocation)) "定位失败" else mLocation)
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