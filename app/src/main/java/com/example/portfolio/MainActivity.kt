package com.example.portfolio

import DateValueFormatter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.example.portfolio.application.ChartRepositoryImpl
import com.example.portfolio.application.StaticPortfolioRepository
import com.example.portfolio.application.network.YahooApiClient
import com.example.portfolio.domain.EvaluatedPosition
import com.example.portfolio.domain.Portfolio
import com.example.portfolio.ui.activity.PositionsActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val portfolioRepository = StaticPortfolioRepository.createFromResources(this)
        val caC40Repository = ChartRepositoryImpl(YahooApiClient())
        var portfolio: Portfolio? = null
        val openPositionsButton: Button = findViewById(R.id.openPositionsButton)
        val investmentValue: TextView = findViewById(R.id.investmentValue)
        val estimationValue: TextView = findViewById(R.id.estimationValue)
        val chart = findViewById<LineChart>(R.id.chart)

        var positions : List<EvaluatedPosition> = emptyList()

        lifecycleScope.launch {
            portfolio = portfolioRepository.getPortfolio()
            portfolio?.let {
                investmentValue.text = getString(R.string.euro_format, it.getTotalInvestment())
                estimationValue.text = getString(R.string.euro_format, it.getTotalEstimation())
                if(it.getTotalInvestment() <= it.getTotalEstimation()){
                    investmentValue.setTextColor(getColor(android.R.color.holo_green_dark))
                } else {
                    investmentValue.setTextColor(getColor(android.R.color.holo_red_dark))
                }
                positions = it.evaluatedPositions
            } ?: run {
                Toast.makeText(this@MainActivity, "Failed to load portfolio", Toast.LENGTH_SHORT).show()
            }
            val chartData = caC40Repository.getCAC40Chart()
            val timestamps = chartData.map { it.x.toLong() }
            if (chartData.isNotEmpty()) {
                val dataSet = LineDataSet(chartData, "CAC 40 Prices")
                val lineData = LineData(dataSet)
                chart.data = lineData
                chart.legend.isEnabled = false
                chart.setExtraOffsets(0f, 0f, 0f, 20f)
                chart.xAxis.configureXAxisWithFormatter(timestamps)
                chart.invalidate()
            } else {
                Toast.makeText(this@MainActivity, "Failed to load chart data", Toast.LENGTH_SHORT).show()
            }
        }



        openPositionsButton.setOnClickListener {
            if (portfolio != null) {
                val intent = Intent(this, PositionsActivity::class.java).apply {
                    putParcelableArrayListExtra(
                        "evaluatedPositions",
                        ArrayList(positions)
                    )
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Positions are still loading", Toast.LENGTH_SHORT).show()
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
