package com.example.portfolio.domain

enum class Stock(
    val displayName: String,
    val symbol: String,
    val isForeign: Boolean
) {
    CAC40("CAC40", "^FCHI", false),
    SANOFI("Sanofi", "SAN.PA", false),
    ACCOR("Accor", "AC.PA", false),
    ORANGE("Orange", "ORA.PA", false),
    BNP("BNP", "BNP.PA", false),
    STELLANTIS("Stellantis", "STLAP.PA", true),
    ATOS("Atos", "ATO.PA", false),
    UBISOFT("Ubisoft", "UBI.PA", false),
    RENAULT("Renault", "RNO.PA", false),
    ISHARES("IShares", "WPEA.PA", true)
}