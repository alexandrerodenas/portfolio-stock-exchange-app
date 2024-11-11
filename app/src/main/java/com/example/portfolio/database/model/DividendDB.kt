package com.example.portfolio.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.portfolio.database.converter.DateConverter
import com.example.portfolio.domain.model.Dividend
import com.example.portfolio.domain.model.Stock

@Entity(
    tableName = "dividends",
    foreignKeys = [ForeignKey(
        entity = StockDB::class,
        parentColumns = ["symbol"],
        childColumns = ["stockSymbol"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["stockSymbol"])]
)data class DividendDB(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val stockSymbol: String,
    val amount: Double,
    val date: String
)