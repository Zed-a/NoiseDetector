package com.nan.noisedetector.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.nan.noisedetector.R
import kotlinx.android.synthetic.main.activity_chart.*

class ChartActivity : AppCompatActivity() {

    companion object {
        const val POSITION = "ChartActivity:position"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)
        initChart()
    }


    private fun initChart() {
        val entries = ArrayList<Entry>()
//        for (data in dataObjects) { // turn your data into Entry objects
//            entries.add(MutableMap.MutableEntry<Any?, Any?>(data.getValueX(), data.getValueY()))
//        }
        entries.add(Entry(1.1F,2F))
        entries.add(Entry(1.2F,2.8F))
        entries.add(Entry(1.8F,2.5F))
        entries.add(Entry(1.9F,2.9F))
        val dataSet = LineDataSet(entries, "Label")
        dataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        dataSet.color = getColor(R.color.pale_green)
        dataSet.fillColor = getColor(R.color.pale_green)
        dataSet.setDrawCircles(false)
        dataSet.setDrawValues(false)
        dataSet.setDrawFilled(true)
        dataSet.fillAlpha = 255
        dataSet.cubicIntensity = 0.2f
        chart.data = LineData(dataSet)
        chart.xAxis.setDrawGridLines(false)
        chart.description.text = ""
        chart.legend.isEnabled = false
        chart.axisRight.isEnabled = false
        chart.axisLeft.setStartAtZero(false)
//        chart.axisLeft.valueFormatter = object : ValueFormatter() {
//            override fun getFormattedValue(value: Float, axis: AxisBase?): String {
//                return String.format("%.2f $",value)
//            }
//        }
        chart.invalidate()
    }

}
