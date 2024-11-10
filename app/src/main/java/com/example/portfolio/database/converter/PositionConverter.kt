package com.example.portfolio.database.converter

import com.example.portfolio.database.model.PositionDB
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import java.io.InputStream

class PositionConverter {

    companion object {
        fun fromYaml(inputStream: InputStream): List<PositionDB> {
            val yaml = Yaml(Constructor(List::class.java))
            val positions = yaml.load(inputStream) as List<Map<String, Any>>

            return positions.map {
                PositionDB(
                    id = (it["id"] as Int),
                    stockSymbol = it["stockSymbol"] as String,
                    number = (it["number"] as Int),
                    buy = (it["buy"] as Double),
                    date = it["date"] as String
                )
            }
        }
    }
}