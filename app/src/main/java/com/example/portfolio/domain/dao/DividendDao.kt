package com.example.portfolio.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.portfolio.database.model.DividendDB
import kotlinx.coroutines.flow.Flow

@Dao
interface DividendDao {
    @Query("SELECT * FROM dividends")
    fun getAll(): Flow<List<DividendDB>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(positions: List<DividendDB>)
}