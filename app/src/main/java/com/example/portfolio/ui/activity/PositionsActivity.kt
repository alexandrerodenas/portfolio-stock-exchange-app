package com.example.portfolio.ui.activity

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.portfolio.R
import com.example.portfolio.application.StaticPositionRepository
import com.example.portfolio.domain.EvaluatedPosition
import com.example.portfolio.ui.adapter.PositionAdapter
import kotlinx.coroutines.launch

class PositionsActivity : ComponentActivity() {

    private lateinit var positionsRecyclerView: RecyclerView
    private lateinit var positionRepository: StaticPositionRepository

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_positions)

        positionsRecyclerView = findViewById(R.id.positionsRecyclerView)

        // Create the repository using the static method
        positionRepository = StaticPositionRepository.createFromResources(this)

        // Launch a coroutine to fetch the evaluated positions
        lifecycleScope.launch {
            // Fetch evaluated positions asynchronously
            val evaluatedPositions: List<EvaluatedPosition> = positionRepository.getEvaluatedPositions()

            // Set up the RecyclerView
            positionsRecyclerView.layoutManager = LinearLayoutManager(this@PositionsActivity)
            positionsRecyclerView.adapter = PositionAdapter(evaluatedPositions)
        }
    }
}
