import android.os.Build
import androidx.annotation.RequiresApi
import com.example.portfolio.domain.dao.PositionDao
import com.example.portfolio.domain.dao.StockDao
import com.example.portfolio.domain.model.EvaluatedPosition
import com.example.portfolio.domain.model.Portfolio
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
                positions.map { positionDB ->
                    stockDao.getFromSymbol(positionDB.stockSymbol)
                        .map { stockDB ->
                            if (stockDB == null) {
                                throw StockNotFoundException(positionDB.stockSymbol)
                            } else {
                                val currentPrice =
                                    stockApiClient.getPriceFromSymbol(positionDB.stockSymbol)
                                        ?: 0.0
                                EvaluatedPosition(
                                    position = positionDB.mapToDomain(stockDB, positionDB.buy),
                                    currentPrice = currentPrice
                                )
                            }
                        }.first()
                }.groupBy { it.getStockName() }
                    .map { (_, evaluatedPositions) ->
                        evaluatedPositions.reduce { acc, evaluatedPosition ->
                            acc.combineWith(
                                evaluatedPosition
                            )
                        }
                    }
                    .sortedBy { it.getStockName() }
            }
            .map { evaluatedPositions ->
                Portfolio(evaluatedPositions)
            }
    }
}
