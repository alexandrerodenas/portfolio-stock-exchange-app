package com.example.portfolio.domain

interface StockRepository {
    suspend fun getAll(): List<StockModel>
    suspend fun getCac40Stock(): StockModel
}