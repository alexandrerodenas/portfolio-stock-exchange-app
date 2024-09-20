package com.example.portfolio

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.example.portfolio.application.StaticPositionRepository
import com.example.portfolio.domain.EvaluatedPosition
import com.example.portfolio.ui.activity.PositionsActivity
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val positionRepository = StaticPositionRepository.createFromResources(this)
        var evaluatedPositions: List<EvaluatedPosition> = emptyList()

        lifecycleScope.launch {
            evaluatedPositions = positionRepository.getEvaluatedPositions()

        }

        val openPositionsButton: Button = findViewById(R.id.openPositionsButton)
        openPositionsButton.setOnClickListener {
            if (evaluatedPositions.isNotEmpty()) {
                val intent = Intent(this, PositionsActivity::class.java).apply {
                    putParcelableArrayListExtra("evaluatedPositions", ArrayList(evaluatedPositions))
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Positions are still loading", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
