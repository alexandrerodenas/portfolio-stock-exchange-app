package com.example.portfolio.domain

class EvaluatedPosition(
    val position: Position,
    val currentPrice: Double
){

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