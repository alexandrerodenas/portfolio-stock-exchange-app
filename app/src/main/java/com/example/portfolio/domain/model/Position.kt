package com.example.portfolio.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
class Position(
    val stock: Stock,
    val number: Int,
    val buy: Double,
    val date: LocalDateTime
) : Parcelable {


    fun getStockName(): String {
        return this.stock.name
    }

    fun getStockSymbol(): String {
        return this.stock.symbol
    }

    fun isForeign(): Boolean{
        return this.stock.isForeign
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Position

        if (stock != other.stock) return false
        if (number != other.number) return false
        if (buy != other.buy) return false
        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        var result = stock.hashCode()
        result = 31 * result + number.hashCode()
        result = 31 * result + buy.hashCode()
        result = 31 * result + date.hashCode()
        return result
    }
}