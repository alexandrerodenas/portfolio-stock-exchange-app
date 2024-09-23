package com.example.portfolio.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StockDao {
    @Query("SELECT * FROM stocks")
    suspend fun getAll(): List<StockDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStocks(stocks: List<StockDB>)
}