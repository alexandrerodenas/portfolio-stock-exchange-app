package com.example.portfolio.application.network

import com.github.mikephil.charting.data.Entry
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

class YahooApiClient(private val baseUrl: String = "https://query1.finance.yahoo.com/") : StockApiClient {

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

    override suspend fun getChartData(symbol: String): List<Entry> {
        return try {
            val response = api.getStockPriceWithRange(symbol, range = "15d", interval = "1d")
            val chartResult = response.chart.result[0]

            val timestamps = chartResult.timestamp
            val closePrices = chartResult.indicators.quote[0].close

            val entries = mutableListOf<Entry>()
            for (i in timestamps.indices) {
                val time = timestamps[i].toFloat()
                val closePrice = closePrices[i].toFloat()
                entries.add(Entry(time, closePrice))
            }
            entries
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

    data class YahooFinanceResponse(
        val chart: Chart
    )

    data class Chart(
        val result: List<Result>
    )

    data class Result(
        val meta: Meta,
        val timestamp: List<Long>,
        val indicators: Indicators
    )

    data class Meta(
        val regularMarketPrice: Double
    )

    data class Indicators(
        val quote: List<Quote>
    )

    data class Quote(
        val close: List<Double>
    )
}
