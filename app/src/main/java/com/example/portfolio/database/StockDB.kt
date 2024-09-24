package com.example.portfolio.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stocks")
data class StockDB(
    @PrimaryKey val symbol: String,
    val name: String,
    val isForeign: Boolean
) {

}