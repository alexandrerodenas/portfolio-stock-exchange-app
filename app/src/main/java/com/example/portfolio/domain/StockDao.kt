package com.example.portfolio.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.portfolio.database.StockDB
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {
    @Query("SELECT * FROM stocks")
    fun getAll(): Flow<List<StockDB>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStocks(stocks: List<StockDB>)
}