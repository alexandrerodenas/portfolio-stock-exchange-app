package com.example.portfolio.application.network

interface StockApiClient {
    suspend fun getPriceFromSymbol(symbol: String): Double?
}