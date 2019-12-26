package com.nan.noisedetector.ui

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nan.noisedetector.R
import com.nan.noisedetector.event.MessageEvent
import com.nan.noisedetector.ui.adapter.HistoryListAdapter
import com.nan.noisedetector.ui.base.BaseFragment
import com.nan.noisedetector.ui.widget.SwipeItemLayout.OnSwipeItemTouchListener
import com.nan.noisedetector.util.PreferenceHelper
import kotlinx.android.synthetic.main.fragment_history.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.support.v4.startActivity

class HistoryFragment : BaseFragment() {

    private lateinit var adapter: HistoryListAdapter

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
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "historyRecord=${PreferenceHelper.historyRecord}")
        adapter = HistoryListAdapter(PreferenceHelper.historyRecord, activity) {
            startActivity<ChartActivity>(ChartActivity.POSITION to it)
        }
        recycler_history.adapter = adapter
        recycler_history.layoutManager = LinearLayoutManager(activity)
        recycler_history.addOnItemTouchListener(OnSwipeItemTouchListener(context))
        recycler_history.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun event(messageEvent: MessageEvent) {
        Log.d(TAG, "handle event")
        adapter.refresh()
    }
}
