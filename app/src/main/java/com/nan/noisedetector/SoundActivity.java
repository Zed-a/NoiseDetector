package com.nan.noisedetector;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.nan.noisedetector.widget.SoundDiscView;

import java.io.File;

public class SoundActivity extends AppCompatActivity {

//    private static final String TAG = "SoundActivity";

    private static String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int GET_PERMISSION = 1;
    private static final int msgWhat = 0x1001;
    private static final int refreshTime = 100;

    float volume = 10000;
    private SoundDiscView soundDiscView;
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
        soundDiscView = findViewById(R.id.soundDiscView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.action_settings)
                startActivity(new Intent(SoundActivity.this, SettingActivity.class));
            return true;
        });
        findViewById(R.id.btn_start_record).setOnClickListener(v -> startRecord());
    }

    public void verifyPermissions() {
        int audioPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int storagePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (audioPermission != PackageManager.PERMISSION_GRANTED || storagePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, GET_PERMISSION);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (this.hasMessages(msgWhat)) {
                return;
            }
            volume = mRecorder.getMaxAmplitude();  //获取声压值
            if(volume > 0 && volume < 1000000) {
                World.setDbCount(20 * (float)(Math.log10(volume)));  //将声压值转为分贝值
                soundDiscView.refresh();
            }
            handler.sendEmptyMessageDelayed(msgWhat, refreshTime);
        }
    };

    private void startListenAudio() {
        handler.sendEmptyMessageDelayed(msgWhat, refreshTime);
    }

    /**
     * 开始记录
     */
    public void startRecord(){

        File file = FileUtil.createFile("temp.amr");
        if (file != null) {
            Log.v("file", "file =" + file.getAbsolutePath());
            try{
                mRecorder.setMyRecAudioFile(file);
                if (mRecorder.startRecorder()) {
                    startListenAudio();
                }else{
                    Toast.makeText(this, "启动录音失败", Toast.LENGTH_SHORT).show();
                }
            }catch(Exception e){
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
        mRecorder.delete(); //停止记录并删除录音文件
        handler.removeMessages(msgWhat);
    }

    @Override
    protected void onDestroy() {
        handler.removeMessages(msgWhat);
        mRecorder.delete();
        super.onDestroy();
    }
}
