package com.example.portfolio.application

import com.example.portfolio.application.network.StockApiClient
import com.example.portfolio.domain.CAC40Repository
import com.github.mikephil.charting.data.Entry

class CAC40RepositoryImpl(
    private val stockApiClient: StockApiClient
): CAC40Repository {

    private val symbol = "^FCHI"

    override suspend fun getCAC40Chart(): List<Entry> {
        return stockApiClient.getChartData(symbol)
    }
}