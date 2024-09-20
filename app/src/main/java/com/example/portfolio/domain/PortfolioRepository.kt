package com.example.portfolio.domain

interface PortfolioRepository {
    suspend fun getPortfolio(): Portfolio
}