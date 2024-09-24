package com.example.portfolio

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
import com.example.portfolio.application.portfolio.StaticPortfolioRepositoryImpl
import com.example.portfolio.database.DataDatabase
import com.example.portfolio.domain.Authenticator
import com.example.portfolio.domain.EvaluatedPosition
import com.example.portfolio.domain.Portfolio
import com.example.portfolio.domain.StockApiClient
import com.example.portfolio.ui.activity.PositionsActivity
import com.example.portfolio.ui.activity.fragment.ChartFragment
import com.example.portfolio.ui.activity.fragment.GlobalPlusMinusValueFragment
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var db: DataDatabase

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

            var positions: List<EvaluatedPosition> = emptyList()

            db = DataDatabase.getInstance(this)

            lifecycleScope.launch {
                portfolio = portfolioRepository.getPortfolio()
                db.stockDao().getAll().collect { stocks ->
                    if (portfolio != null) {
                        injectGlobalPlusMinusValue(portfolio!!)
                        positions = portfolio!!.evaluatedPositions

                        if (stocks.isNotEmpty()) {
                            val cac40Stock = stocks.find { it.name == "CAC40" }
                            if(cac40Stock == null){
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
                            }
                        }
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Failed to load portfolio",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
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

    private fun String.setLocale() {
        val locale = Locale(this)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        createConfigurationContext(config)
    }
}
