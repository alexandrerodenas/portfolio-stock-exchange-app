package com.example.portfolio.ui.activity

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.portfolio.R
import com.example.portfolio.ui.activity.fragment.ChartFragment
import com.github.mikephil.charting.data.Entry

class ChartActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)


        val chartDataArg =
            intent.getParcelableArrayListExtra("chartData", Entry::class.java) ?: arrayListOf()
        val chartTitleArg = intent.getStringExtra("chartTitle") ?: "Chart"

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.chartFragmentContainer,
                ChartFragment.newInstance(chartDataArg, chartTitleArg)
            )
            .commit()
    }
}
