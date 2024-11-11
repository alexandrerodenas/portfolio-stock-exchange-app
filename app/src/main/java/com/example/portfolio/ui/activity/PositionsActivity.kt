package com.example.portfolio.ui.activity

import PortfolioServiceImpl
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.portfolio.R
import com.example.portfolio.domain.model.EvaluatedPosition
import com.example.portfolio.domain.model.Portfolio
import com.example.portfolio.domain.service.StockApiClient
import com.example.portfolio.application.network.YahooApiClient
import com.example.portfolio.database.AppDatabase
import com.example.portfolio.database.model.PositionDB
import com.example.portfolio.ui.activity.fragment.SellQuantityDialogFragment
import com.example.portfolio.ui.adapter.PositionAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PositionsActivity : AppCompatActivity() {

    private val stockApiClient: StockApiClient = YahooApiClient()
    private lateinit var db: AppDatabase
    private lateinit var localPositionsRecyclerView: RecyclerView
    private lateinit var foreignPositionsRecyclerView: RecyclerView

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_positions)
        localPositionsRecyclerView = findViewById(R.id.localPositionsRecyclerView)
        foreignPositionsRecyclerView = findViewById(R.id.foreignPositionsRecyclerView)


        val addNewStockButton: ImageButton = findViewById(R.id.createStockIconButton)

        val portfolio = intent.getParcelableExtra<Portfolio>("portfolio")

        if (portfolio != null) {
            setupRecyclerViews(portfolio, localPositionsRecyclerView, foreignPositionsRecyclerView)
        } else {
            lifecycleScope.launch {
                Toast.makeText(this@PositionsActivity, "Failed to load portfolio data", Toast.LENGTH_SHORT).show()
            }
        }

        addNewStockButton.setOnClickListener {
            val intent = Intent(this, CreatePositionActivity::class.java)
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupRecyclerViews(
        portfolio: Portfolio,
        localPositionsRecyclerView: RecyclerView,
        foreignPositionsRecyclerView: RecyclerView
    ) {
        val createAdapter = { positions: List<EvaluatedPosition> ->
            PositionAdapter(
                positions.sortedBy { it.getStockName() },
                onItemClicked = { evaluatedPosition ->
                    lifecycleScope.launch {
                        try {
                            val chartData = stockApiClient.getDailyChartDate(evaluatedPosition.getStockSymbol())
                            val intent = Intent(this@PositionsActivity, ChartActivity::class.java).apply {
                                putParcelableArrayListExtra("chartData", ArrayList(chartData))
                                putExtra("chartTitle", evaluatedPosition.getStockName())
                            }
                            startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(this@PositionsActivity, "Failed to load chart data", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onEditClicked = { evaluatedPosition ->
                    showEditQuantityPopup(evaluatedPosition)
                }
            )
        }

        localPositionsRecyclerView.layoutManager = LinearLayoutManager(this)
        localPositionsRecyclerView.adapter = createAdapter(portfolio.getLocalPositions())
        localPositionsRecyclerView.setHasFixedSize(true)

        foreignPositionsRecyclerView.layoutManager = LinearLayoutManager(this)
        foreignPositionsRecyclerView.adapter = createAdapter(portfolio.getForeignPositions())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showEditQuantityPopup(evaluatedPosition: EvaluatedPosition) {
        db = AppDatabase.getInstance(this)
        val portfolioServiceImpl = PortfolioServiceImpl(
            db.positionDao(),
            db.stockDao(),
            stockApiClient
        )

        SellQuantityDialogFragment(
            positionToSell = evaluatedPosition.position,
            onSubmit = { sellStockName, sellQuantity, sellPrice, sellDate ->
                lifecycleScope.launch(Dispatchers.IO) {
                    val positionToInsert = PositionDB(0, sellStockName, -sellQuantity, -sellPrice, sellDate)
                    db.positionDao().insert(listOf(positionToInsert))
                    portfolioServiceImpl.getPortfolio().first().let { updatedPortfolio ->
                        launch(Dispatchers.Main) {
                            setupRecyclerViews(updatedPortfolio, localPositionsRecyclerView, foreignPositionsRecyclerView)
                            Toast.makeText(this@PositionsActivity, "Vente de $sellQuantity action(s)", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }
        ).show(supportFragmentManager, "EditQuantityDialog")
    }
}
