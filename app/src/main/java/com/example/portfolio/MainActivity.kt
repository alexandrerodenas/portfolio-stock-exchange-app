package com.example.portfolio

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.example.portfolio.application.StaticPortfolioRepository
import com.example.portfolio.domain.Portfolio
import com.example.portfolio.ui.activity.PositionsActivity
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val positionRepository = StaticPortfolioRepository.createFromResources(this)
        var portfolio: Portfolio? = null
        val openPositionsButton: Button = findViewById(R.id.openPositionsButton)
        val investmentValue: TextView = findViewById(R.id.investmentValue)
        val estimationValue: TextView = findViewById(R.id.estimationValue)


        lifecycleScope.launch {
            portfolio = positionRepository.getPortfolio()
            portfolio?.let {
                investmentValue.text = getString(R.string.euro_format, it.getTotalInvestment())
                estimationValue.text = getString(R.string.euro_format, it.getTotalEstimation())
                if(it.getTotalInvestment() <= it.getTotalEstimation()){
                    investmentValue.setTextColor(getColor(android.R.color.holo_green_dark))
                } else {
                    investmentValue.setTextColor(getColor(android.R.color.holo_red_dark))
                }
            } ?: run {
                Toast.makeText(this@MainActivity, "Failed to load portfolio", Toast.LENGTH_SHORT).show()
            }
        }

        openPositionsButton.setOnClickListener {
            if (portfolio != null) {
                val intent = Intent(this, PositionsActivity::class.java).apply {
                    putExtra("portfolio", portfolio)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Positions are still loading", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
