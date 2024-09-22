package com.example.portfolio.ui.activity.fragment

import DateValueFormatter
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.portfolio.R
import com.example.portfolio.application.ChartRepositoryImpl
import com.example.portfolio.application.network.YahooApiClient
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.launch

class ChartFragment : Fragment(R.layout.fragment_chart) {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chart = view.findViewById<LineChart>(R.id.chart)
        val caC40Repository = ChartRepositoryImpl(YahooApiClient())

        lifecycleScope.launch {
            val chartData = caC40Repository.getCAC40Chart()
            val timestamps = chartData.map { it.x.toLong() }
            if (chartData.isNotEmpty()) {
                val dataSet = LineDataSet(chartData, "CAC 40 Prices")
                val lineData = LineData(dataSet)
                chart.data = lineData
                chart.legend.isEnabled = false
                chart.setExtraOffsets(0f, 0f, 0f, 20f)
                chart.xAxis.configureXAxisWithFormatter(timestamps)
                chart.invalidate() // Refresh chart
            } else {
                Toast.makeText(requireContext(), "Failed to load chart data", Toast.LENGTH_SHORT).show()
            }
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
