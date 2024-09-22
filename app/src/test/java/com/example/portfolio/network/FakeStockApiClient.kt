package com.example.portfolio.network

import com.example.portfolio.domain.StockApiClient
import com.github.mikephil.charting.data.Entry

class FakeStockApiClient: StockApiClient {
    override suspend fun getPriceFromSymbol(symbol: String): Double? {
        return 0.0
    }

    override suspend fun getBiweeklyChartData(symbol: String): List<Entry> {
       return emptyList()
    }
}