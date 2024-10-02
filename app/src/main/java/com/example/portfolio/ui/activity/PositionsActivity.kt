package com.example.portfolio.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.portfolio.R
import com.example.portfolio.domain.service.StockApiClient
import com.example.portfolio.application.network.YahooApiClient
import com.example.portfolio.domain.model.EvaluatedPosition
import com.example.portfolio.ui.adapter.PositionAdapter
import kotlinx.coroutines.launch

class PositionsActivity : AppCompatActivity() {

    private val stockApiClient: StockApiClient = YahooApiClient()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_positions)

        val localPositionsRecyclerView = findViewById<RecyclerView>(R.id.localPositionsRecyclerView)
        val foreignPositionsRecyclerView =
            findViewById<RecyclerView>(R.id.foreignPositionsRecyclerView)

        val addNewStockButton : ImageButton = findViewById(R.id.createStockIconButton)

        val (localPositions, foreignPositions) = (intent.getParcelableArrayListExtra<EvaluatedPosition>(
            "evaluatedPositions"
        ) ?: listOf()).partition { !it.isForeign() }

        val createAdapter = { positions: List<EvaluatedPosition> ->
            PositionAdapter(positions) { evaluatedPosition ->
                lifecycleScope.launch {
                    try {
                        val chartData =
                            stockApiClient.getDailyChartDate(evaluatedPosition.getStockSymbol())
                        val intent =
                            Intent(this@PositionsActivity, ChartActivity::class.java).apply {
                                putParcelableArrayListExtra("chartData", ArrayList(chartData))
                                putExtra("chartTitle", evaluatedPosition.getStockName())
                            }
                        startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@PositionsActivity,
                            "Failed to load chart data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        localPositionsRecyclerView.layoutManager = LinearLayoutManager(this)
        localPositionsRecyclerView.adapter = createAdapter(localPositions)
        localPositionsRecyclerView.setHasFixedSize(true)

        foreignPositionsRecyclerView.layoutManager = LinearLayoutManager(this)
        foreignPositionsRecyclerView.adapter = createAdapter(foreignPositions)

        addNewStockButton.setOnClickListener {
            val intent = Intent(this, CreatePositionActivity::class.java)
            startActivity(intent)
        }

    }
}
