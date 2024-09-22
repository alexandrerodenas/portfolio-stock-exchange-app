package com.example.portfolio.application

import com.example.portfolio.application.network.StockApiClient
import com.example.portfolio.domain.ChartRepository
import com.github.mikephil.charting.data.Entry

class ChartRepositoryImpl(
    private val stockApiClient: StockApiClient
): ChartRepository {

    private val symbol = "^FCHI"

    override suspend fun getCAC40Chart(): List<Entry> {
        return stockApiClient.getChartData(symbol)
    }
}