package com.example.portfolio.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.portfolio.R
import com.example.portfolio.application.dividend.DividendRepositoryImpl
import com.example.portfolio.database.AppDatabase
import com.example.portfolio.domain.model.Dividend
import com.example.portfolio.ui.adapter.DividendsAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DividendsActivity : AppCompatActivity() {

    private lateinit var dividendsRecyclerView: RecyclerView
    private lateinit var dividendRepositoryImpl: DividendRepositoryImpl

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dividends)

        dividendsRecyclerView = findViewById(R.id.dividendsRecyclerView)
        dividendsRecyclerView.layoutManager = LinearLayoutManager(this)

        val db = AppDatabase.getInstance(this)

        dividendRepositoryImpl = DividendRepositoryImpl(
            db.dividendDao(),
            db.stockDao()
        )

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                dividendRepositoryImpl.getDividends().collect { dividendsList: List<Dividend> ->
                    withContext(Dispatchers.Main) {
                        val adapter = DividendsAdapter(dividendsList.sortedBy { it.date })
                        dividendsRecyclerView.adapter = adapter
                    }
                }
            }
        }
    }
}
