package com.example.portfolio.domain.model

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import kotlinx.parcelize.Parcelize

@Parcelize
class EvaluatedPosition(
    val position: Position,
    val currentPrice: Double
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

    fun getEstimation(): Double {
        return this.currentPrice * this.position.number.toFloat()
    }

    fun getPlusMinusValue(): Double {
        return this.getEstimation() - this.position.buy
    }

    fun getPlusMinusValueAsPercentage(): Double {
        return this.getPlusMinusValue() / this.getEstimation() * 100

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun combineWith(other: EvaluatedPosition): EvaluatedPosition {
        val position = other.position.combineWith(position)
        return EvaluatedPosition(position, currentPrice)
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