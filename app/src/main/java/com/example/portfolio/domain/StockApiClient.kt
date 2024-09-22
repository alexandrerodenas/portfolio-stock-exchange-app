package com.example.portfolio.domain

import com.github.mikephil.charting.data.Entry

interface StockApiClient {
    suspend fun getPriceFromSymbol(symbol: String): Double?
    suspend fun getChartData(symbol: String): List<Entry>
}