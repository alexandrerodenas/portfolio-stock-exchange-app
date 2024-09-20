package com.example.portfolio.domain

interface PositionRepository {
    suspend fun getEvaluatedPositions(): List<EvaluatedPosition>
}