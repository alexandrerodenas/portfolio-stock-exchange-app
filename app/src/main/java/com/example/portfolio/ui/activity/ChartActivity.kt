package com.example.portfolio.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.portfolio.R
import com.example.portfolio.ui.fragment.ChartFragment
import com.github.mikephil.charting.data.Entry

class ChartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        val chartData = intent.getParcelableArrayListExtra("chartData", Entry::class.java) ?: arrayListOf()
        val chartTitle = intent.getStringExtra("chartTitle") ?: "Chart"

        supportFragmentManager.beginTransaction()
            .replace(R.id.chartFragmentContainer, ChartFragment.newInstance(chartData, chartTitle))
            .commit()
    }
}
