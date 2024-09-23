package com.example.portfolio.domain

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
class Position(
    val stockLegacy: StockLegacy,
    val number: Int,
    val buy: Double,
    val date: LocalDateTime
) : Parcelable {


    fun getStockName(): String {
        return this.stockLegacy.name
    }

    fun getStockSymbol(): String {
        return this.stockLegacy.symbol
    }

    fun isForeign(): Boolean{
        return this.stockLegacy.isForeign
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun combineWith(other: Position): Position {
        val combinedNumber = (this.number + other.number)
        val combinedBuy = (this.buy + other.buy)
        val latestDate = if (this.date.isAfter(other.date)) this.date else other.date

        return Position(stockLegacy, combinedNumber, combinedBuy, latestDate)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Position

        if (stockLegacy != other.stockLegacy) return false
        if (number != other.number) return false
        if (buy != other.buy) return false
        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        var result = stockLegacy.hashCode()
        result = 31 * result + number.hashCode()
        result = 31 * result + buy.hashCode()
        result = 31 * result + date.hashCode()
        return result
    }
}