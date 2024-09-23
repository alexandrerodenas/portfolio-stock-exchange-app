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
import com.example.portfolio.application.biometric.AuthenticatorFactory
import com.example.portfolio.application.portfolio.StaticPortfolioRepositoryImpl
import com.example.portfolio.domain.StockApiClient
import com.example.portfolio.application.network.YahooApiClient
import com.example.portfolio.database.AppDatabase
import com.example.portfolio.domain.Authenticator
import com.example.portfolio.domain.EvaluatedPosition
import com.example.portfolio.domain.Portfolio
import com.example.portfolio.ui.activity.PositionsActivity
import com.example.portfolio.ui.activity.fragment.ChartFragment
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        "fr".setLocale()
        setContentView(R.layout.activity_main)

        val portfolioRepository = StaticPortfolioRepositoryImpl.createFromResources(this)
        val stockApiClient: StockApiClient = YahooApiClient()
        val authenticator: Authenticator = AuthenticatorFactory().provide(this) {
            var portfolio: Portfolio? = null
            val openPositionsButton: Button = findViewById(R.id.openPositionsButton)
            val investmentValue: TextView = findViewById(R.id.investmentValue)
            val estimationValue: TextView = findViewById(R.id.estimationValue)
            val pendingPlusMinusValue: TextView = findViewById(R.id.pendingPlusMinusValue)

            var positions: List<EvaluatedPosition> = emptyList()

            db = AppDatabase.getDatabase(this)

            lifecycleScope.launch {
                val stocks = withContext(Dispatchers.IO) {
                    db.stockDao().getAll()
                }

                portfolio = withContext(Dispatchers.IO) {
                    portfolioRepository.getPortfolio()
                }

                // Update UI on the main thread
                if (portfolio != null) {
                    investmentValue.text = getString(R.string.euro_format, portfolio!!.getTotalInvestment())
                    estimationValue.text = getString(R.string.euro_format, portfolio!!.getTotalEstimation())
                    pendingPlusMinusValue.text = getString(R.string.euro_format, portfolio!!.getPendingPlusMinusValue())

                    pendingPlusMinusValue.setTextColor(
                        if (portfolio!!.getTotalInvestment() <= portfolio!!.getTotalEstimation())
                            getColor(android.R.color.holo_green_dark)
                        else
                            getColor(android.R.color.holo_red_dark)
                    )

                    positions = portfolio!!.evaluatedPositions
                    // Inject chart data only after the portfolio is ready
                    if (stocks.isNotEmpty()) {
                        injectChartFragment(stockApiClient.getBiweeklyChartData(stocks[0].symbol), stocks[0].name)
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to load portfolio", Toast.LENGTH_SHORT).show()
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

        if (authenticator.isAvailable()) {
            authenticator.authenticate()
        }


    }

    private fun injectChartFragment(chartData: List<Entry>, chartTitle: String) {
        val fragmentManager = supportFragmentManager
        val chartFragment = ChartFragment.newInstance(ArrayList(chartData), chartTitle)
        fragmentManager.beginTransaction()
            .replace(R.id.chartFragmentContainer, chartFragment)
            .commit()
    }

    private fun String.setLocale() {
        val locale = Locale(this)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        createConfigurationContext(config)
    }
}
