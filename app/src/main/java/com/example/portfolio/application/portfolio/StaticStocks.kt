package com.example.portfolio.application.portfolio

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.portfolio.domain.Position
import com.example.portfolio.domain.Stock
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class StaticStocks @JsonCreator constructor(
    @JsonProperty("stocks") val stocks: Map<String, List<StockData>>
)

data class StockData @JsonCreator constructor(
    @JsonProperty("number") val number: Int,
    @JsonProperty("buy") val buy: Float,
    @JsonProperty("date") val date: String
) {
    @RequiresApi(Build.VERSION_CODES.O)
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")


    @RequiresApi(Build.VERSION_CODES.O)
    fun mapToPosition(name: String): Position {
        return Position(
            stock = Stock.valueOf(name.uppercase()),
            number = this.number,
            buy = this.buy.toDouble(),
            date = LocalDate.parse(this.date, formatter).atStartOfDay()
        )
    }
}
