package com.nan.noisedetector.ui.base

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment

abstract class BaseFragment : Fragment() {

    protected lateinit var activity: Activity

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activity = context as Activity
    }
}