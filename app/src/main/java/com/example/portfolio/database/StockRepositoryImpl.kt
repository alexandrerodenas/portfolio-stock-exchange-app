package com.example.portfolio.database

import com.example.portfolio.domain.StockModel
import com.example.portfolio.domain.StockRepository

class StockRepositoryImpl(private val stockDao: StockDao) : StockRepository {
    override suspend fun getAll(): List<StockModel> {
        return stockDao.getAll().map { it.toDomainModel() }
    }

    override suspend fun getCac40Stock(): StockModel {
        return stockDao.getCac40Stock().toDomainModel()
    }
}