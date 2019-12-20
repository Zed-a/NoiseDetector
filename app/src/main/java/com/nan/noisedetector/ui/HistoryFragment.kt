package com.nan.noisedetector.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bin.david.form.core.SmartTable
import com.nan.noisedetector.R
import com.nan.noisedetector.bean.HistoryData
import com.nan.noisedetector.event.MessageEvent
import com.nan.noisedetector.ui.base.BaseFragment
import com.nan.noisedetector.util.PreferenceHelper.historyRecord
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class HistoryFragment : BaseFragment() {

    private lateinit var table: SmartTable<HistoryData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        initTable(view)
        return view
    }

    private fun initTable(view: View) {
        table = view.findViewById(R.id.table_view)
        val config = table.config
        table.isClickable = false
        table.setData(historyRecord)
        config.isShowYSequence = false
        config.isShowXSequence = false
//        config.contentBackground = BaseBackgroundFormat(activity.getColor(R.color.btn_background_color))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun event(messageEvent: MessageEvent) {
        Log.d(TAG, "handle event")
//        val list = ArrayList<HistoryData>()
//        list.add(HistoryData("1","1",1,1))
//        table.addData(list, true)
//        table.notifyDataChanged()
    }
}
