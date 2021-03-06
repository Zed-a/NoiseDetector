package com.nan.noisedetector.ui.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
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
import cn.pedant.SweetAlert.SweetAlertDialog
import com.nan.noisedetector.R
import com.nan.noisedetector.bean.DataBean
import com.nan.noisedetector.bean.HistoryData
import com.nan.noisedetector.util.PreferenceHelper.historyRecord
import com.nan.noisedetector.util.isEmpty

class HistoryListAdapter(private var mData: ArrayList<DataBean>, private val context: Activity,
                         private val itemClick: (Int) -> Unit, private val showDialog: (Int, Int, String) -> Unit)
    : RecyclerView.Adapter<HistoryListAdapter.ViewHolder>() {

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
            holder.deleteView.setOnClickListener { showDeleteDialog(position) }
            holder.editView.setOnClickListener { showEditDialog(position, this) }
            holder.mainItem.setOnClickListener { itemClick(position) }
            if (isEmpty(msg))
                holder.msgView.visibility = View.GONE
            else {
                holder.msgView.visibility = View.VISIBLE
                holder.msgView.text = "备注:$msg"
            }

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
        val editView: Button = view.findViewById(R.id.btn_edit)
        val time: TextView = view.findViewById(R.id.tv_time)
        val position: TextView = view.findViewById(R.id.tv_position)
        val maxDecibel: TextView = view.findViewById(R.id.tv_max_decibel)
        val averageDecibel: TextView = view.findViewById(R.id.tv_average_decibel)
        val msgView: TextView = view.findViewById(R.id.tv_msg)
    }

    private fun showDeleteDialog(position: Int) {
        SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this file!")
                .setConfirmText("Yes,delete it!")
                .setConfirmClickListener {
                        it.dismissWithAnimation()
                        delete(position)
                }
                .show()
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

    private fun showEditDialog(position: Int, historyData: HistoryData) {
        notifyDataSetChanged()
        val items: Array<String> = arrayOf("编辑地点", "编辑备注")
        AlertDialog.Builder(context,0)
                .setItems(items) { dialog, which ->
                    dialog.dismiss()
                    val text = if (which == 0) historyData.location else historyData.msg
                    showDialog(which, position, text) }
                .show()
    }
}