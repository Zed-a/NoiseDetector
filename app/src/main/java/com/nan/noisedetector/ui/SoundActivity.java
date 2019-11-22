package com.nan.noisedetector.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.nan.noisedetector.R;
import com.nan.noisedetector.record.MyMediaRecorder;
import com.nan.noisedetector.util.DecibelUtil;
import com.nan.noisedetector.util.FileUtil;
import com.nan.noisedetector.ui.widget.SoundDiscView;
import com.nan.noisedetector.util.PreferenceHelper;

import java.io.File;

public class SoundActivity extends AppCompatActivity {

    private static final String TAG = "SoundActivity";

    private static final String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int GET_PERMISSION = 1;
    private static final int MSG_WHAT = 0x1001;
    private static final int REFRESH_TIME = 100;
    private float mThreshold;

    private SoundDiscView mSoundDiscView;
    private MyMediaRecorder mRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
//        getWindow().setEnterTransition(new Slide(Gravity.TOP));
        setContentView(R.layout.activity_sound);
        mRecorder = new MyMediaRecorder();
        initView();
        verifyPermissions();
    }

    private void initView() {
        mSoundDiscView = findViewById(R.id.soundDiscView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.action_settings)
                startActivity(new Intent(SoundActivity.this, SettingActivity.class));
            return true;
        });
        findViewById(R.id.btn_start_detect).setOnClickListener(v -> startRecord());
        findViewById(R.id.btn_stop_detect).setOnClickListener(v -> {
            mRecorder.delete();
        });
    }

    private void verifyPermissions() {
        int audioPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int storagePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (audioPermission != PackageManager.PERMISSION_GRANTED || storagePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, GET_PERMISSION);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mThreshold = PreferenceHelper.getThreshold();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (hasMessages(MSG_WHAT)) {
                return;
            }
            float volume = mRecorder.getMaxAmplitude();  //获取声压值
            if(volume > 0 && volume < 1000000) {
                float dbCount = 20 * (float)(Math.log10(volume));
                showNotification(dbCount);
                DecibelUtil.setDbCount(dbCount);  //将声压值转为分贝值
                mSoundDiscView.refresh();
            }
            handler.sendEmptyMessageDelayed(MSG_WHAT, REFRESH_TIME);
        }
    };

    private void showNotification(float dbCount) {
        if (PreferenceHelper.isOpenNotify() && dbCount < mThreshold) {
            return;
        }
        Notification.Builder builder = new Notification.Builder(SoundActivity.this);
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.baidu.com"));
//        PendingIntent pendingIntent = PendingIntent.getActivity(SoundActivity.this,0,intent,0);  //点击跳转
        builder.setSmallIcon(R.mipmap.ic_launcher)  //小图标，在大图标右下角
            .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher)) //大图标，没有设置时小图标就是大图标
//            .setContentIntent(pendingIntent)
            .setAutoCancel(true)   //点击的时候消失
            .setContentTitle("噪音超限")
            .setContentText("当前噪音分贝为"+dbCount+",大于阈值"+mThreshold);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(1,builder.build());
    }

    /**
     * 开始记录
     */
    private void startRecord() {
        File file = FileUtil.createFile("temp.amr");
        if (file != null) {
            Log.d(TAG, "file path=" + file.getPath());
            try {
                mRecorder.setMyRecAudioFile(file);
                if (mRecorder.startRecorder()) {
                    handler.sendEmptyMessageDelayed(MSG_WHAT, REFRESH_TIME);
                } else {
                    Toast.makeText(this, "启动录音失败", Toast.LENGTH_SHORT).show();
                }
            } catch(Exception e) {
                Toast.makeText(this, "录音机已被占用或录音权限被禁止", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "创建文件失败", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRecorder.delete();
        handler.removeMessages(MSG_WHAT);
    }

    @Override
    protected void onDestroy() {
        handler.removeMessages(MSG_WHAT);
        mRecorder.delete();
        super.onDestroy();
    }
}
