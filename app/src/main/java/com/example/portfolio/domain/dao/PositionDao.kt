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

    @Query("DELETE FROM positions WHERE id = :positionId")
    fun deletePosition(positionId: Int)

    @Query("UPDATE positions SET number = :newQuantity WHERE id = :positionId")
    fun updatePositionQuantity(positionId: Int, newQuantity: Int)
}