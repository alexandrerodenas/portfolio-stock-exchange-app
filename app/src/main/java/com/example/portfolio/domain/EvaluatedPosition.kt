package com.example.portfolio.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class EvaluatedPosition(
    val position: Position,
    val currentPrice: Double
): Parcelable {

    fun getStockName(): String {
        return this.position.getStockName()
    }

    fun getStockSymbol(): String {
        return this.position.getStockSymbol()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EvaluatedPosition

        if (position != other.position) return false
        if (currentPrice != other.currentPrice) return false

        return true
    }

    override fun hashCode(): Int {
        var result = position.hashCode()
        result = 31 * result + currentPrice.hashCode()
        return result
    }
}