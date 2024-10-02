package com.example.portfolio.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.portfolio.domain.model.Stock

@Entity(tableName = "stocks")
data class StockDB(
    @PrimaryKey val symbol: String,
    val name: String,
    val isForeign: Boolean,
    val isIndex: Boolean,
) {

    fun mapToDomain(): Stock {
        return Stock(
            name = this.name,
            symbol = this.symbol,
            isForeign = this.isForeign,
            isIndex = this.isIndex
        )
    }
}