package com.nan.noisedetector.ui

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.nan.noisedetector.R
import com.nan.noisedetector.ui.widget.ChartSlideFragment
import com.nan.noisedetector.util.PreferenceHelper
import kotlinx.android.synthetic.main.activity_chart.*

class ChartActivity : AppCompatActivity() {

    private val data = PreferenceHelper.historyRecord

    companion object {
        const val POSITION = "ChartActivity:position"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)
        initView()
    }

    private fun initView() {
        btn_last_page.setOnClickListener {
            view_pager.currentItem = view_pager.currentItem - 1
        }
        btn_next_page.setOnClickListener {
            view_pager.currentItem = view_pager.currentItem + 1
        }

        view_pager.adapter = ChartSlidePagerAdapter(supportFragmentManager)
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {}
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

            override fun onPageSelected(p0: Int) {
                setSlideBtnEnable(p0)
            }
        })
        view_pager.offscreenPageLimit = 4

        with(intent.getIntExtra(POSITION, 0)) {
            view_pager.currentItem = this
            setSlideBtnEnable(this)
        }
    }

    fun setSlideBtnEnable(position :Int) {
        btn_last_page.isEnabled = position != 0
        btn_next_page.isEnabled = position != data.size -1
    }

    inner class ChartSlidePagerAdapter constructor(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int) = ChartSlideFragment.newInstance(data[position].entries, data[position].historyData)

        override fun getCount() = data.size
    }
}
