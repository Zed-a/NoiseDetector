package com.nan.noisedetector.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nan.noisedetector.R
import com.nan.noisedetector.bean.HistoryData
import com.nan.noisedetector.util.PreferenceHelper

class HistoryListAdapter(private var mData: ArrayList<HistoryData>, private val context: Context) :
        RecyclerView.Adapter<HistoryListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_history,
                parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.time.text = mData[position].startTime
        holder.position.text = mData[position].startTime
        holder.maxDecibel.text = mData[position].startTime
        holder.averageDecibel.text = mData[position].startTime
    }

    override fun getItemCount() = mData.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val time: TextView = view.findViewById(R.id.tv_time)
        val position: TextView = view.findViewById(R.id.tv_position)
        val maxDecibel: TextView = view.findViewById(R.id.tv_max_decibel)
        val averageDecibel: TextView = view.findViewById(R.id.tv_average_decibel)
    }

    fun refresh() {
        mData = PreferenceHelper.historyRecord
        notifyDataSetChanged()
    }
}