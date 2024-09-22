package com.example.portfolio.ui.fragment

import DateValueFormatter
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.portfolio.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.launch

class ChartFragment : Fragment(R.layout.fragment_chart) {

    companion object {
        private const val ARG_CHART_DATA = "chart_data"
        private const val ARG_CHART_LABEL = "chart_label"

        fun newInstance(chartData: ArrayList<Entry>, chartTitle: String): ChartFragment {
            val fragment = ChartFragment()
            val bundle = Bundle().apply {
                putParcelableArrayList(ARG_CHART_DATA, chartData)
                putString(ARG_CHART_LABEL, chartTitle)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chart = view.findViewById<LineChart>(R.id.chart)

        val chartData: ArrayList<Entry>? = arguments?.getParcelableArrayList(
            ARG_CHART_DATA,
            Entry::class.java
        )
        val chartLabel: String? = arguments?.getString(ARG_CHART_LABEL)

        if (chartData != null && chartLabel != null) {
            lifecycleScope.launch {
                val timestamps = chartData.map { it.x.toLong() }
                val dataSet = LineDataSet(chartData, chartLabel)
                val lineData = LineData(dataSet)
                chart.data = lineData
                chart.legend.isEnabled = false
                chart.setExtraOffsets(0f, 0f, 0f, 20f)
                chart.xAxis.configureXAxisWithFormatter(timestamps)
                chart.invalidate() // Refresh chart
            }
        } else {
            Toast.makeText(requireContext(), "Failed to load chart data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun XAxis.configureXAxisWithFormatter(timestamps: List<Long>) {
        granularity = 1f
        valueFormatter = DateValueFormatter(timestamps)
        position = XAxis.XAxisPosition.BOTTOM
        labelCount = timestamps.size.coerceAtMost(5)
        labelRotationAngle = 45f
    }
}
