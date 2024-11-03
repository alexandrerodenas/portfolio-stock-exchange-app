package com.example.portfolio.database.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.portfolio.database.converter.DateConverter
import com.example.portfolio.domain.model.Position

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapToDomain(stockDB: StockDB, buy: Double): Position {
        return Position(
            stock = stockDB.mapToDomain(),
            buy = buy,
            date = DateConverter().stringToDate(this.date),
            number = this.number
        )
    }
}