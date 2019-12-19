package com.nan.noisedetector.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.nan.noisedetector.R
import com.nan.noisedetector.record.NoiseMediaRecorder
import com.nan.noisedetector.ui.SoundActivity
import com.nan.noisedetector.util.DecibelUtil.clear
import com.nan.noisedetector.util.DecibelUtil.getDbCount
import com.nan.noisedetector.util.DecibelUtil.setDbCount
import com.nan.noisedetector.util.FileUtil
import com.nan.noisedetector.util.PreferenceHelper.isOpenNotify
import com.nan.noisedetector.util.PreferenceHelper.threshold
import kotlinx.android.synthetic.main.activity_sound.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import kotlin.math.log10

class SoundActivity : AppCompatActivity() {
    private var mThreshold = 0f
    //    private SoundDiscView mSoundDiscView;
    private var mRecorder: NoiseMediaRecorder = NoiseMediaRecorder()

    companion object {
        private const val TAG = "SoundActivity"
        private val PERMISSIONS = arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        private const val GET_PERMISSION = 1
        private const val MSG_WHAT = 0x1001
        private const val REFRESH_TIME = 100

        fun isAppAlive(context: Context, packageName: String): Int {
            val activityManager = context
                    .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val listInfos = activityManager
                    .getRunningTasks(20)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
//        getWindow().setEnterTransition(new Slide(Gravity.TOP));
        setContentView(R.layout.activity_sound)
        initView()
        verifyPermissions()
    }

    private fun initView() {
        //        mSoundDiscView = findViewById(R.id.soundDiscView);
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitle(R.string.app_name)
        toolbar.inflateMenu(R.menu.menu_main)
        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.action_settings)
                startActivity<SettingActivity>()
            true
        }
        btn_start_detect.setOnClickListener {
            startRecord()
            btn_start_detect.isEnabled = false
            btn_stop_detect.isEnabled = true
        }
        btn_stop_detect.setOnClickListener{
            stopRecord()
            btn_start_detect.isEnabled = true
            btn_stop_detect.isEnabled = false
        }
    }

    private fun verifyPermissions() {
        val audioPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        val storagePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (audioPermission != PackageManager.PERMISSION_GRANTED || storagePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, GET_PERMISSION)
        }
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
                val dbCount = 20 * log10(volume.toDouble()).toFloat()
                //showNotification(dbCount);
                setDbCount(dbCount) //将声压值转为分贝值
                //mSoundDiscView.refresh();
                soundDiscView.text = (getDbCount().toInt() + 20).toString()
            }
            sendEmptyMessageDelayed(MSG_WHAT, REFRESH_TIME.toLong())
        }
    }

    private fun showNotification(dbCount: Float) {
        if (isOpenNotify && dbCount < mThreshold) {
            return
        }
        val builder = Notification.Builder(this@SoundActivity)
        var intent: Intent
        //        PendingIntent pendingIntent = PendingIntent.getActivity(SoundActivity.this,0,intent,0);  //点击跳转
        builder.setSmallIcon(R.mipmap.ic_launcher) //小图标，在大图标右下角
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)) //大图标，没有设置时小图标就是大图标
//            .setContentIntent(pendingIntent)
                .setAutoCancel(true) //点击的时候消失
                .setContentTitle("噪音超限")
                .setContentText("当前噪音分贝为$dbCount,大于阈值$mThreshold")
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1, builder.build())
    }

    /**
     * 开始记录
     */
    private fun startRecord() {
        val file = FileUtil.createFile("temp.amr")
        if (file != null) {
            Log.d(TAG, "file path=" + file.path)
            try {
                mRecorder.setMyRecAudioFile(file)
                if (mRecorder.startRecorder()) {
                    handler.sendEmptyMessageDelayed(MSG_WHAT, REFRESH_TIME.toLong())
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

    private fun stopRecord() {
        mRecorder.delete()
        handler.removeMessages(MSG_WHAT)
        clear()
        soundDiscView.text = "0"
        //        mSoundDiscView.refresh();
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRecord()
    }
}