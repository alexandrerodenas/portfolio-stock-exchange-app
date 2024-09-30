package com.example.portfolio.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.portfolio.database.model.PositionDB
import kotlinx.coroutines.flow.Flow

@Dao
interface PositionDao {
    @Query("SELECT * FROM positions")
    fun getAll(): Flow<List<PositionDB>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(positions: List<PositionDB>)
}