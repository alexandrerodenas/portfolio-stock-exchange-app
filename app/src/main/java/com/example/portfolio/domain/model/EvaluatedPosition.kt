package com.example.portfolio.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class EvaluatedPosition(
    private val position: Position,
    private val currentPrice: Double
) : Parcelable {

    fun getStockName(): String {
        return this.position.getStockName()
    }

    fun getStockSymbol(): String {
        return this.position.getStockSymbol()
    }

    fun isForeign(): Boolean {
        return this.position.isForeign()
    }

    fun getPositionCount(): Int {
        return this.position.number
    }

    fun getPositionBuy(): Double {
        return this.position.buy
    }

    fun getCurrentPrice(): Double {
        return this.currentPrice
    }

    fun getEstimation(): Double {
        return this.currentPrice * this.position.number.toFloat()
    }

    fun getPlusMinusValue(): Double {
        return this.getEstimation() - this.position.buy
    }

    fun getPlusMinusValueAsPercentage(): Double {
        return this.getPlusMinusValue() / this.getEstimation() * 100
    }

    fun hasAtLeastOneStock(): Boolean {
        return this.position.number > 0;
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