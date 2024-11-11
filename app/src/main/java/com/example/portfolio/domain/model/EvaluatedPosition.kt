package com.example.portfolio.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class EvaluatedPosition(
    val position: Position,
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

}