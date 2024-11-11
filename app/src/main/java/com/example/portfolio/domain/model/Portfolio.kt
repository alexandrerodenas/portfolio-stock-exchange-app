package com.example.portfolio.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Portfolio(
    private val evaluatedPositions: List<EvaluatedPosition>
) : Parcelable {

    fun isNotEmpty(): Boolean {
        return this.evaluatedPositions.isNotEmpty();
    }

    fun getLocalPositions(): List<EvaluatedPosition>{
        return this.evaluatedPositions.filter { !it.isForeign() }
    }

    fun getForeignPositions(): List<EvaluatedPosition>{
        return this.evaluatedPositions.filter { it.isForeign() }
    }

    fun getTotalEstimation(): Double {
        return this.evaluatedPositions.sumOf {
            evaluatedPosition: EvaluatedPosition -> evaluatedPosition.getEstimation()
        }
    }

    fun getTotalInvestment(): Double {
        return this.evaluatedPositions.sumOf {
                evaluatedPosition: EvaluatedPosition -> evaluatedPosition.getPositionBuy()
        }
    }

    fun getPendingPlusMinusValue(): Double {
        return this.getTotalEstimation() - this.getTotalInvestment()
    }

    fun getTop3(): List<EvaluatedPosition> {
        return evaluatedPositions
            .sortedByDescending { it.getPlusMinusValueAsPercentage() }
            .take(3)
    }

    fun getFlop3(): List<EvaluatedPosition> {
        return evaluatedPositions
            .sortedBy { it.getPlusMinusValueAsPercentage() }
            .take(3)
    }
}