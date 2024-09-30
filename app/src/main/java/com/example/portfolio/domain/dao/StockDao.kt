package com.example.portfolio.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.portfolio.database.model.StockDB
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {
    @Query("SELECT * FROM stocks")
    fun getAll(): Flow<List<StockDB>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(stocks: List<StockDB>)

    @Query("SELECT * FROM stocks WHERE symbol = :symbol LIMIT 1")
    fun getFromSymbol(symbol: String): Flow<StockDB?>
}