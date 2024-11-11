package com.example.portfolio.application.dividend

import StockNotFoundException
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.portfolio.database.converter.DateConverter
import com.example.portfolio.database.model.DividendDB
import com.example.portfolio.domain.dao.DividendDao
import com.example.portfolio.domain.dao.StockDao
import com.example.portfolio.domain.model.Dividend
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DividendRepositoryImpl(
    private val dividendDao: DividendDao,
    private val stockDao: StockDao
) {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getDividends(): Flow<List<Dividend>> {
        return dividendDao.getAll()
            .map { dividends ->
                dividends.groupBy { it.stockSymbol }
                    .map { (stockSymbol, groupedPositions) ->
                        buildingDividends(stockSymbol, groupedPositions)
                    }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun buildingDividends(
        stockSymbol: String,
        groupedDividends: List<DividendDB>
    ): Dividend = stockDao.getFromSymbol(stockSymbol).map { stockDB ->
        if (stockDB == null) {
            throw StockNotFoundException(stockSymbol)
        } else {
            val sumAmount = groupedDividends.sumOf { it.amount }
            val lastDate = groupedDividends.last().date // non sense
            Dividend(
                date = DateConverter().stringToDate(lastDate),
                stock = stockDB.mapToDomain(),
                amount = sumAmount
            )
        }
    }.first()
}