import android.os.Build
import androidx.annotation.RequiresApi
import com.example.portfolio.database.converter.DateConverter
import com.example.portfolio.database.model.PositionDB
import com.example.portfolio.domain.dao.PositionDao
import com.example.portfolio.domain.dao.StockDao
import com.example.portfolio.domain.model.EvaluatedPosition
import com.example.portfolio.domain.model.Portfolio
import com.example.portfolio.domain.model.Position
import com.example.portfolio.domain.service.StockApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class StockNotFoundException(symbol: String) : Exception("Stock with symbol $symbol not found")


class PortfolioServiceImpl(
    private val positionDao: PositionDao,
    private val stockDao: StockDao,
    private val stockApiClient: StockApiClient
) {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getPortfolio(): Flow<Portfolio> {
        return positionDao.getAll()
            .map { positions ->
                positions.groupBy { it.stockSymbol }
                    .map { (stockSymbol, groupedPositions) ->
                        buildingEvaluatedPositions(stockSymbol, groupedPositions)
                    }.filter { it.hasAtLeastOneStock() }
                    .sortedBy { it.getStockName() }
            }.map { evaluatedPositions ->
                Portfolio(evaluatedPositions)
            }
    }

    private suspend fun buildingEvaluatedPositions(
        stockSymbol: String,
        groupedPositions: List<PositionDB>
    ) : EvaluatedPosition = stockDao.getFromSymbol(stockSymbol).map { stockDB ->
        if (stockDB == null) {
            throw StockNotFoundException(stockSymbol)
        } else {
            val currentPrice =
                stockApiClient.getPriceFromSymbol(stockSymbol)
                    ?: 0.0
            val combinedQuantity = groupedPositions.sumOf { it.number }
            val combinedTotalPrice = groupedPositions.sumOf { it.buy }
            val lastDate = groupedPositions.last().date // non sense
            EvaluatedPosition(
                position = Position(
                    stockDB.mapToDomain(),
                    combinedQuantity,
                    combinedTotalPrice,
                    DateConverter().stringToDate(lastDate)
                ),
                currentPrice = currentPrice
            )
        }
    }.first()
}
