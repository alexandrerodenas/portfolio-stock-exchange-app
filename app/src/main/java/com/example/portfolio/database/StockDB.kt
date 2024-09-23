package com.example.portfolio.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.portfolio.domain.StockModel

@Entity(tableName = "stocks")
data class StockDB(
    @PrimaryKey val symbol: String,
    val name: String,
    val isForeign: Boolean
) {
    fun toDomainModel(): StockModel {
        return StockModel(
            name = this.name,
            symbol = this.symbol,
            isForeign = this.isForeign
        )
    }
}