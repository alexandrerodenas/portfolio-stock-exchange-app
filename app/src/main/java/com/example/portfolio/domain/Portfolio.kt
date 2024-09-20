package com.example.portfolio.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Portfolio(
    val evaluatedPositions: List<EvaluatedPosition>
) : Parcelable {
    fun getTotalEstimation(): Double {
        return this.evaluatedPositions.sumOf {
            evaluatedPosition: EvaluatedPosition -> evaluatedPosition.currentPrice * evaluatedPosition.position.number
        }
    }

    fun getTotalInvestment(): Double {
        return this.evaluatedPositions.sumOf {
                evaluatedPosition: EvaluatedPosition -> evaluatedPosition.position.buy
        }
    }
}