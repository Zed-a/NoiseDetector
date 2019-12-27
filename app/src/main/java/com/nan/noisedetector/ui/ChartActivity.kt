package com.nan.noisedetector.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
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
        view_pager.adapter = ChartSlidePagerAdapter(supportFragmentManager)
        view_pager.currentItem = intent.getIntExtra(POSITION, 0)
        view_pager.offscreenPageLimit = 4
    }


    inner class ChartSlidePagerAdapter constructor(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return ChartSlideFragment.newInstance(data[position].entries)
        }

        override fun getCount(): Int {
            return data.size
        }
    }
}
