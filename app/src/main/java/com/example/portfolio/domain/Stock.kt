package com.example.portfolio.domain

enum class Stock(
    val displayName: String,
    val symbol: String,
) {
    SANOFI("Sanofi", "SAN.PA"),
    ACCOR("Accor", "AC.PA"),
    ORANGE("Orange", "ORA.PA"),
    BNP("BNP", "BNP.PA"),
    STELLANTIS("Stellantis", "STLAP.PA"),
    ATOS("Atos", "ATO.PA"),
    UBISOFT("Ubisoft", "UBI.PA"),
    RENAULT("Renault", "RNO.PA"),
    ISHARES("IShares", "WPEA.PA")
}