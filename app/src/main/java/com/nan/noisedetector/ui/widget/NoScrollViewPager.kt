package com.nan.noisedetector.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class NoScrollViewPager : ViewPager {
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)
    constructor(context: Context?) : super(context!!)

    override fun onInterceptTouchEvent(ev: MotionEvent) = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent) = true
}