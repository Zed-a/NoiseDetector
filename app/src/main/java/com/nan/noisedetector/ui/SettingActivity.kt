package com.nan.noisedetector.ui

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ListView

import com.nan.noisedetector.R
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {


    //    private Toolbar mToolbar;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
    }



    // 后台通知
    //噪声过大提醒
    //噪音峰值

    //    private void initContentView(View view, int layoutResID, LayoutParams params) {
    //        View allView;
    //        FrameLayout llContent;
    //        allView = getLayoutInflater().inflate(R.layout.have_toolbar_layout,	null);
    //        llContent = (FrameLayout) allView.findViewById(R.id.ll_content);
    //        mToolbar = (Toolbar) allView.findViewById(R.id.toolbar);
    //        mToolbar.setTitleTextColor(Color.WHITE);
    //        mToolbar.setTitle(getTitle());
    //        mToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
    //        mToolbar.setNavigationOnClickListener(v -> {
    //            if(doBeforeFinish()){
    //
    //            }else{
    //                closeActivity();
    //            }
    //            LogUtils.LOGE(TAG, "navigation onclick");
    //        });
    //        if (view == null) {
    //            view = getLayoutInflater().inflate(layoutResID, null);
    //        }
    //        llContent.addView(view);
    //        ListView listView = (ListView) llContent.findViewById(android.R.id.list);
    //        listView.setDivider(null);
    //        listView.setDividerHeight(0);
    //        if (params != null) {
    //            super.setContentView(allView, params);
    //        } else {
    //            super.setContentView(allView);
    //        }
    //        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
    //            getWindow().setFlags(
    //                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
    //                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
    //        }
    //    }
}
