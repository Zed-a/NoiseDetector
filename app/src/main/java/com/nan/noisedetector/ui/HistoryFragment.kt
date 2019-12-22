package com.nan.noisedetector.ui

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class HistoryFragment : BaseFragment() {

    private lateinit var recyclerView: RecyclerView
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
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        recyclerView = view.findViewById(R.id.recycler_history)
        Log.d(TAG, "historyRecord=${PreferenceHelper.historyRecord}")
        adapter = HistoryListAdapter(PreferenceHelper.historyRecord, activity)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addOnItemTouchListener(OnSwipeItemTouchListener(context))
        recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        return view
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun event(messageEvent: MessageEvent) {
        Log.d(TAG, "handle event")
        adapter.refresh()
    }
}
