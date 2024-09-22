package com.example.portfolio.application

import com.example.portfolio.domain.EvaluatedPosition
import com.example.portfolio.domain.Position
import com.example.portfolio.domain.Stock
import com.example.portfolio.network.FakeStockApiClient
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import java.time.LocalDateTime

class StaticPortfolioRepositoryImplTest {

    @Test
    fun `Can get positions`() = runBlocking {
        val yaml = """
              stocks:
                Sanofi:
                  - number: 1
                    buy: 104.85
                    date: 03/09/2024
                Atos:
                  - number: 120
                    buy: 105.98
                    date: 06/08/2024
        """.trimIndent()
        val staticPositionRepository = StaticPortfolioRepositoryImpl(yaml, FakeStockApiClient())

        val portfolio = staticPositionRepository.getPortfolio()

        assertEquals(
            listOf(
                EvaluatedPosition(
                    position = Position(
                        stock = Stock.SANOFI,
                        number = 1,
                        date = LocalDateTime.of(2024, 9, 3, 0, 0),
                        buy = 104.85
                    ),
                    currentPrice = 0.0
                ),
                EvaluatedPosition(
                    position = Position(
                        stock = Stock.ATOS,
                        number = 120,
                        date = LocalDateTime.of(2024, 8, 6, 0, 0),
                        buy = 105.98
                    ),
                    currentPrice = 0.0
                )
            ),
            portfolio
        )

    }
}

