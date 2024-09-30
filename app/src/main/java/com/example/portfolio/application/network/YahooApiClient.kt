package com.example.portfolio.application.network

import com.example.portfolio.domain.service.StockApiClient
import com.github.mikephil.charting.data.Entry
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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

    override suspend fun getBiweeklyChartData(symbol: String): List<Entry> {
        return getChartData(symbol, "15d", "1d")
    }

    override suspend fun getDailyChartDate(symbol: String): List<Entry> {
        return getChartData(symbol, "1d", "5m")
    }

    private suspend fun getChartData(
        symbol: String,
        timeRange: String,
        timeInterval: String
    ): List<Entry> {
        return try {
            val response = api.getStockPriceWithRange(
                symbol,
                range = timeRange,
                interval = timeInterval
            )

            val chartResult = response.chart.result[0]

            chartResult.timestamp.indices.map { index ->
                Entry(
                    chartResult.timestamp[index].toFloat(),
                    chartResult.indicators.quote[0].close[index].toFloat()
                )
            }
        } catch (e: Exception) {
            emptyList()
        }

    }

    private interface YahooFinanceApi {
        @GET("v8/finance/chart/{symbol}")
        suspend fun getStockPrice(@Path("symbol") symbol: String): YahooFinanceResponse

        @GET("v8/finance/chart/{symbol}")
        suspend fun getStockPriceWithRange(
            @Path("symbol") symbol: String,
            @Query("range") range: String,
            @Query("interval") interval: String
        ): YahooFinanceResponse
    }

    private data class YahooFinanceResponse(
        val chart: Chart
    )

    private data class Chart(
        val result: List<Result>
    )

    private data class Result(
        val meta: Meta,
        val timestamp: List<Long>,
        val indicators: Indicators
    )

    private data class Meta(
        val regularMarketPrice: Double
    )

    private data class Indicators(
        val quote: List<Quote>
    )

    private data class Quote(
        val close: List<Double>
    )
}
