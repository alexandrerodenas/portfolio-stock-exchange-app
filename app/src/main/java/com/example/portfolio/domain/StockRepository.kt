package com.example.portfolio.domain

interface StockRepository {
    suspend fun getAll(): List<StockModel>
}