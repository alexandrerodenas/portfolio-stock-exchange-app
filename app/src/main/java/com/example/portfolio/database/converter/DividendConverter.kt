package com.example.portfolio.database.converter

import com.example.portfolio.database.model.DividendDB
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import java.io.InputStream

class DividendConverter {
    companion object {
        fun fromYaml(inputStream: InputStream): List<DividendDB> {
            val yaml = Yaml(Constructor(List::class.java))
            val dividends = yaml.load(inputStream) as List<Map<String, Any>>

            return dividends.map {
                DividendDB(
                    id = (it["id"] as Int),
                    stockSymbol = it["stockSymbol"] as String,
                    amount = (it["amount"] as Double),
                    date = it["date"] as String
                )
            }
        }
    }
}