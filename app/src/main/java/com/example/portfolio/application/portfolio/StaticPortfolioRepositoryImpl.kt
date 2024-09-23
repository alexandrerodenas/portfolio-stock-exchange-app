package com.example.portfolio.application.portfolio

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.portfolio.R
import com.example.portfolio.domain.StockApiClient
import com.example.portfolio.application.network.YahooApiClient
import com.example.portfolio.domain.EvaluatedPosition
import com.example.portfolio.domain.Portfolio
import com.example.portfolio.domain.Position
import com.example.portfolio.domain.PortfolioRepository
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.InputStream

class StaticPortfolioRepositoryImpl(
    private val positionFile: String,
    private val stockApiClient: StockApiClient
) : PortfolioRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getPortfolio(): Portfolio {
        return Portfolio(this.readPositions().map { position: Position ->
            EvaluatedPosition(
                position = position,
                currentPrice = stockApiClient.getPriceFromSymbol(position.stock.symbol) ?: 0.0
            )
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun readPositions(): List<Position> {
        val yamlMapper = YAMLMapper()

        val staticStocks: StaticStocks = yamlMapper.readValue(this.positionFile)

        return staticStocks.stocks.flatMap { (name, stockDataList) ->
            stockDataList.map { data -> data.mapToPosition(name) }
        }.groupBy { it.stock.name }
            .map { (_, positions) ->
                positions.reduce { acc, position -> acc.combineWith(position) }
            }
            .sortedBy { it.stock.name }
    }

    companion object {
        fun createFromResources(context: Context): StaticPortfolioRepositoryImpl {
            val inputStream: InputStream = context.resources.openRawResource(R.raw.positions)
            val positionContent = inputStream.bufferedReader().use { it.readText() }
            return StaticPortfolioRepositoryImpl(positionContent, YahooApiClient())
        }
    }
}