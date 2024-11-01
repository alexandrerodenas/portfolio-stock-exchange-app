// PositionsActivity.kt
package com.example.portfolio.ui.activity

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
import com.example.portfolio.ui.activity.fragment.SellQuantityDialogFragment
import com.example.portfolio.ui.adapter.PositionAdapter
import kotlinx.coroutines.launch

class PositionsActivity : AppCompatActivity() {

    private val stockApiClient: StockApiClient = YahooApiClient()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_positions)

        val localPositionsRecyclerView = findViewById<RecyclerView>(R.id.localPositionsRecyclerView)
        val foreignPositionsRecyclerView = findViewById<RecyclerView>(R.id.foreignPositionsRecyclerView)
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

    private fun setupRecyclerViews(
        portfolio: Portfolio,
        localPositionsRecyclerView: RecyclerView,
        foreignPositionsRecyclerView: RecyclerView
    ) {
        val createAdapter = { positions: List<EvaluatedPosition> ->
            PositionAdapter(
                positions,
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

    private fun showEditQuantityPopup(evaluatedPosition: EvaluatedPosition) {
        val initialQuantity = evaluatedPosition.getPositionCount()

        SellQuantityDialogFragment(
            maxQuantity = initialQuantity,
            currentPrice = evaluatedPosition.position.buy,
            onSubmit = { sellQuantity, sellPrice, sellDate ->
                Toast.makeText(this, "Vente de $sellQuantity action(s)", Toast.LENGTH_SHORT).show()
            }
        ).show(supportFragmentManager, "EditQuantityDialog")
    }
}
