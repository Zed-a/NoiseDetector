package com.nan.noisedetector.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.nan.noisedetector.R
import com.nan.noisedetector.bean.HistoryData
import kotlinx.android.synthetic.main.fragment_chart_slide.*

class ChartSlideFragment : Fragment() {
    private lateinit var entries: ArrayList<Entry>
    private lateinit var historyData: HistoryData
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        entries = arguments!!.getParcelableArrayList<Entry>(ENTRIES) as ArrayList<Entry>
        historyData = arguments!!.getSerializable(HISTORY_DATA) as HistoryData
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chart_slide, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initChart()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        with(historyData) {
            chart_des.text = "$location   $date   $time"
        }
        initChart()
    }

    companion object {

        const val ENTRIES = "ChartSlideFragment:entries"
        const val HISTORY_DATA = "ChartSlideFragment:HistoryData"

        fun newInstance(entries: ArrayList<Entry>, historyData: HistoryData): ChartSlideFragment {
            val f = ChartSlideFragment()
            val args = Bundle()
            args.putParcelableArrayList(ENTRIES, entries)
            args.putSerializable(HISTORY_DATA, historyData)
            f.arguments = args
            return f
        }
    }

    private fun initChart() {
        val dataSet = LineDataSet(entries, "Label")
        dataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        dataSet.color = mContext.getColor(R.color.pale_green)
        dataSet.fillColor = mContext.getColor(R.color.pale_green)
        dataSet.setDrawCircles(false)
        dataSet.setDrawFilled(true)
        dataSet.fillAlpha = 255
        dataSet.cubicIntensity = 0.2f
        chart.data = LineData(dataSet)
        chart.xAxis.setDrawGridLines(false)
        chart.description.text = ""
        chart.legend.isEnabled = false
        chart.axisRight.isEnabled = false
//        chart.axisLeft.axisMinimum = 0f
//        chart.axisLeft.resetAxisMinimum()
//        chart.axisLeft.valueFormatter = object : ValueFormatter() {
//            override fun getFormattedValue(value: Float, axis: AxisBase?): String {
//                return String.format("%.2f $",value)
//            }
//        }
        chart.invalidate()
    }
}