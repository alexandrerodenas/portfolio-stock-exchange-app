package com.example.portfolio.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "positions",
    foreignKeys = [ForeignKey(
        entity = StockDB::class,
        parentColumns = ["symbol"],
        childColumns = ["stockSymbol"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["stockSymbol"])]
)
data class PositionDB(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val stockSymbol: String,
    val number: Int,
    val buy: Double,
    val date: String
) {
}