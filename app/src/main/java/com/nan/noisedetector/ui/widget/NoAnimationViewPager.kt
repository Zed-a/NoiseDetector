package com.nan.noisedetector.ui.widget

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet

class NoAnimationViewPager : ViewPager {
    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)

    override fun setCurrentItem(item: Int) { //去除页面切换时的滑动翻页效果
        super.setCurrentItem(item, false)
    }
}