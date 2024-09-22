package com.example.portfolio

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.portfolio.application.ChartRepositoryImpl
import com.example.portfolio.application.StaticPortfolioRepositoryImpl
import com.example.portfolio.application.network.YahooApiClient
import com.example.portfolio.domain.EvaluatedPosition
import com.example.portfolio.domain.Portfolio
import com.example.portfolio.ui.activity.PositionsActivity
import com.example.portfolio.ui.fragment.ChartFragment
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val portfolioRepository = StaticPortfolioRepositoryImpl.createFromResources(this)
        val caC40Repository = ChartRepositoryImpl(YahooApiClient())


        var portfolio: Portfolio? = null
        val openPositionsButton: Button = findViewById(R.id.openPositionsButton)
        val investmentValue: TextView = findViewById(R.id.investmentValue)
        val estimationValue: TextView = findViewById(R.id.estimationValue)

        var positions: List<EvaluatedPosition> = emptyList()

        lifecycleScope.launch {
            portfolio = portfolioRepository.getPortfolio()
            portfolio?.let {
                investmentValue.text = getString(R.string.euro_format, it.getTotalInvestment())
                estimationValue.text = getString(R.string.euro_format, it.getTotalEstimation())
                if (it.getTotalInvestment() <= it.getTotalEstimation()) {
                    investmentValue.setTextColor(getColor(android.R.color.holo_green_dark))
                } else {
                    investmentValue.setTextColor(getColor(android.R.color.holo_red_dark))
                }
                positions = it.evaluatedPositions
            } ?: run {
                Toast.makeText(this@MainActivity, "Failed to load portfolio", Toast.LENGTH_SHORT).show()
            }
            injectChartFragment(caC40Repository.getCAC40Chart(), "CAC 40")
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

    private fun injectChartFragment(chartData: List<Entry>, chartTitle: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val chartFragment = ChartFragment.newInstance(ArrayList(chartData), chartTitle)
        fragmentTransaction.replace(R.id.chartFragmentContainer, chartFragment)
        fragmentTransaction.commit()
    }
}
