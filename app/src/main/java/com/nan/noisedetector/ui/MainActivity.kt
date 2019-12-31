package com.nan.noisedetector.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import com.nan.noisedetector.R
import com.nan.noisedetector.ui.fragment.HistoryFragment
import com.nan.noisedetector.ui.fragment.SoundFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val fragments = ArrayList<Fragment>()
    private val fragTitles= ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFrag()
        initViewPager()
    }

    private fun initViewPager() {
        viewPager.adapter = NoiseFragmentPagerAdapter(supportFragmentManager, fragments, fragTitles)
        indicator.setViewPager(viewPager)
        viewPager.offscreenPageLimit = 3
    }

    private fun initFrag() {
        fragments.add(SoundFragment())
        fragTitles.add("主页")
        fragments.add(HistoryFragment())
        fragTitles.add("历史")
    }

    internal class NoiseFragmentPagerAdapter(fm: FragmentManager?, private val fragmentList: List<Fragment>,
                                             private val titleList: List<String>) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int) = fragmentList[position]

        override fun getCount() = fragmentList.size

        override fun getPageTitle(position: Int) = titleList[position]
    }
}

