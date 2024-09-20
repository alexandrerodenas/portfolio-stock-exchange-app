package com.example.portfolio.application.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class YahooApiClient(private val baseUrl: String = "https://query1.finance.yahoo.com/") :
    StockApiClient {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val api: YahooFinanceApi by lazy {
        retrofit.create(YahooFinanceApi::class.java)
    }

    override suspend fun getPriceFromSymbol(symbol: String): Double? {
        return try {
            val response = api.getStockPrice(symbol)
            response.chart.result[0].meta.regularMarketPrice
        } catch (e: Exception) {
            null
        }
    }

    private interface YahooFinanceApi {
        @GET("v8/finance/chart/{symbol}")
        suspend fun getStockPrice(@Path("symbol") symbol: String): YahooFinanceResponse
    }

    data class YahooFinanceResponse(
        val chart: Chart
    )

    data class Chart(
        val result: List<Result>
    )

    data class Result(
        val meta: Meta
    )

    data class Meta(
        val regularMarketPrice: Double
    )
}