package com.example.portfolio.ui.activity

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.portfolio.R
import com.example.portfolio.domain.EvaluatedPosition
import com.example.portfolio.ui.adapter.PositionAdapter
import kotlinx.coroutines.launch

class PositionsActivity : ComponentActivity() {

    private lateinit var positionsRecyclerView: RecyclerView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_positions)

        positionsRecyclerView = findViewById(R.id.positionsRecyclerView)

        lifecycleScope.launch {
            val evaluatedPositions: List<EvaluatedPosition>? = intent.getParcelableArrayListExtra(
                "evaluatedPositions"
            )

            positionsRecyclerView.layoutManager = LinearLayoutManager(this@PositionsActivity)
            positionsRecyclerView.adapter = PositionAdapter(evaluatedPositions ?: emptyList())
        }
    }

}
