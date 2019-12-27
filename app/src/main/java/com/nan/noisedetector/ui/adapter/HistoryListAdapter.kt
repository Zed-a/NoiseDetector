package com.nan.noisedetector.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.nan.noisedetector.R
import com.nan.noisedetector.bean.DataBean
import com.nan.noisedetector.util.PreferenceHelper.historyRecord

class HistoryListAdapter(private var mData: ArrayList<DataBean>, private val context: Context,
                         private val itemClick: (Int) -> Unit) :
        RecyclerView.Adapter<HistoryListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_history, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(mData[position].historyData) {
            holder.time.text = "$date    $time"
            holder.position.text = location
            holder.position.setOnLongClickListener {
                if (holder.position.layout.getEllipsisCount(0) != 0) {
                    Toast.makeText(context, holder.position.text, LENGTH_LONG).show()
                    true
                } else false
            }
            holder.item.setOnClickListener {}
            holder.deleteView.setOnClickListener { delete(position) }
            holder.mainItem.setOnClickListener { itemClick(position) }

            var spanString = SpannableStringBuilder("最大分贝 $max")
            spanString.setSpan(ForegroundColorSpan(context.getColor(R.color.colorPrimary)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spanString.setSpan(AbsoluteSizeSpan(14, true), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            holder.maxDecibel.text = spanString

            spanString = SpannableStringBuilder("平均分贝 $average")
            spanString.setSpan(ForegroundColorSpan(context.getColor(R.color.colorPrimary)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spanString.setSpan(AbsoluteSizeSpan(10, true), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            holder.averageDecibel.text = spanString
        }
    }

    override fun getItemCount() = mData.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val item = view
        val mainItem: LinearLayout = view.findViewById(R.id.main_item)
        val deleteView: Button = view.findViewById(R.id.delete)
        val time: TextView = view.findViewById(R.id.tv_time)
        val position: TextView = view.findViewById(R.id.tv_position)
        val maxDecibel: TextView = view.findViewById(R.id.tv_max_decibel)
        val averageDecibel: TextView = view.findViewById(R.id.tv_average_decibel)
    }

    fun refresh() {
        mData = historyRecord
        notifyDataSetChanged()
    }

    private fun delete(position: Int) {
        mData.removeAt(position)
        historyRecord = mData
        notifyDataSetChanged()
    }
}