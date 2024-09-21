package com.example.portfolio.application.network

import com.github.mikephil.charting.data.Entry

interface StockApiClient {
    suspend fun getPriceFromSymbol(symbol: String): Double?
    suspend fun getChartData(symbol: String): List<Entry>
}