package com.example.portfolio

import PortfolioServiceImpl
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.portfolio.application.biometric.AuthenticatorFactory
import com.example.portfolio.application.network.YahooApiClient
import com.example.portfolio.database.AppDatabase
import com.example.portfolio.domain.model.Portfolio
import com.example.portfolio.domain.service.Authenticator
import com.example.portfolio.domain.service.StockApiClient
import com.example.portfolio.ui.activity.DividendsActivity
import com.example.portfolio.ui.activity.PositionsActivity
import com.example.portfolio.ui.activity.fragment.ChartFragment
import com.example.portfolio.ui.activity.fragment.GlobalPlusMinusValueFragment
import com.example.portfolio.ui.activity.fragment.TopFlopFragment
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        "fr".setLocale()
        setContentView(R.layout.activity_main)

        val stockApiClient: StockApiClient = YahooApiClient()

        val authenticator: Authenticator = AuthenticatorFactory().provide(this) {
            val openDividendsButton: Button = findViewById(R.id.openDividendsButton)
            openDividendsButton.setOnClickListener {
                val intent = Intent(this, DividendsActivity::class.java)
                startActivity(intent)
            }

            db = AppDatabase.getInstance(this)

            val portfolioService = PortfolioServiceImpl(
                positionDao = db.positionDao(),
                stockDao = db.stockDao(),
                stockApiClient = stockApiClient
            )

            val openPositionsButton: Button = findViewById(R.id.openPositionsButton)
            openPositionsButton.setOnClickListener {
                Toast.makeText(this, "Portfolio is still loading", Toast.LENGTH_SHORT).show()
            }

            lifecycleScope.launch {
                portfolioService.getPortfolio()
                    .filter { portfolio -> portfolio.isNotEmpty() }
                    .first()
                    .let { portfolio ->
                        injectGlobalPlusMinusValue(portfolio)

                        openPositionsButton.setOnClickListener {
                            val intent =
                                Intent(this@MainActivity, PositionsActivity::class.java).apply {
                                    putExtra("portfolio", portfolio)
                                }
                            startActivity(intent)
                        }

                        db.stockDao().getAll().collect { stocks ->
                            val cac40Stock = stocks.find { it.name == "CAC40" }
                            if (cac40Stock == null) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Failed to load CAC 40",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                injectChartFragment(
                                    stockApiClient.getBiweeklyChartData(cac40Stock.symbol),
                                    cac40Stock.name
                                )
                                injectTopFlopFragment(portfolio)
                            }
                        }
                    }
            }


        }

        if (authenticator.isAvailable()) {
            authenticator.authenticate()
        }
    }

    private fun injectChartFragment(chartData: List<Entry>, chartTitle: String) {
        val fragmentManager = supportFragmentManager
        val fragment = ChartFragment.newInstance(ArrayList(chartData), chartTitle)
        fragmentManager.beginTransaction()
            .replace(R.id.chartFragmentContainer, fragment)
            .commit()
    }

    private fun injectGlobalPlusMinusValue(portfolio: Portfolio) {
        val fragmentManager = supportFragmentManager
        val fragment = GlobalPlusMinusValueFragment.newInstance(portfolio)
        fragmentManager.beginTransaction()
            .replace(R.id.globalPlusMinusValueFragmentContainer, fragment)
            .commit()
    }

    private fun injectTopFlopFragment(portfolio: Portfolio) {
        val fragmentManager = supportFragmentManager
        val topFlopFragment = TopFlopFragment.newInstance(portfolio)
        fragmentManager.beginTransaction()
            .replace(R.id.topFlopFragmentContainer, topFlopFragment)
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
