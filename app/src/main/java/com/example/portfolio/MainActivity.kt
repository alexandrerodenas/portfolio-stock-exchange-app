package com.example.portfolio

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.example.portfolio.ui.activity.PositionsActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val openPositionsButton: Button = findViewById(R.id.openPositionsButton)
        openPositionsButton.setOnClickListener {
            val intent = Intent(this, PositionsActivity::class.java)
            startActivity(intent)
        }
    }
}
