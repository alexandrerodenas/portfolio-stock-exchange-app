package com.example.portfolio.network

import com.example.portfolio.application.network.StockApiClient

class FakeStockApiClient: StockApiClient {
    override suspend fun getPriceFromSymbol(symbol: String): Double? {
        return 0.0
    }
}