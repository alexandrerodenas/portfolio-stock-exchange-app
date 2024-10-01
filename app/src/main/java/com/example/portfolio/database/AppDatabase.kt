package com.example.portfolio.database

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.portfolio.database.converter.DateConverter
import com.example.portfolio.database.model.PositionDB
import com.example.portfolio.database.model.StockDB
import com.example.portfolio.domain.dao.PositionDao
import com.example.portfolio.domain.dao.StockDao
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executors

@TypeConverters(DateConverter::class)
@Database(entities = [StockDB::class, PositionDB::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stockDao(): StockDao
    abstract fun positionDao(): PositionDao


    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, AppDatabase::class.java, "stock.db",
        ).addCallback(object : Callback() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                ioThread {
                    getInstance(context).stockDao().insert(STOCKS)
                    getInstance(context).positionDao().insert(POSITIONS)
                }
            }
        }).build()

        val STOCKS = listOf(
            StockDB("^FCHI", "CAC40", false),
            StockDB("SAN.PA", "Sanofi", false),
            StockDB("AC.PA", "Accor", false),
            StockDB("ORA.PA", "Orange", false),
            StockDB("BNP.PA", "BNP", false),
            StockDB("STLAP.PA", "Stellantis", true),
            StockDB("ATO.PA", "Atos", false),
            StockDB("UBI.PA", "Ubisoft", false),
            StockDB("RNO.PA", "Renault", false),
            StockDB("WPEA.PA", "IShares", true)
        )

        @RequiresApi(Build.VERSION_CODES.O)
        val formatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") // Define your date format


        @RequiresApi(Build.VERSION_CODES.O)
        val POSITIONS = listOf(
             PositionDB(
                id = 1,
                stockSymbol = "ATO.PA",
                number = 120,
                buy = 105.98,
                date = LocalDateTime.of(2024, 8, 6, 0, 0).format(formatter)
            ), PositionDB(
                id = 2,
                stockSymbol = "UBI.PA",
                number = 10,
                buy = 200.05,
                date = LocalDateTime.of(2024, 7, 22, 0, 0).format(formatter)
            ), PositionDB(
                id = 3,
                stockSymbol = "BNP.PA",
                number = 2,
                buy = 128.58,
                date = LocalDateTime.of(2024, 7, 4, 0, 0).format(formatter)
            ), PositionDB(
                id = 4,
                stockSymbol = "RNO.PA",
                number = 2,
                buy = 101.91,
                date = LocalDateTime.of(2024, 7, 4, 0, 0).format(formatter)
            ), PositionDB(
                id = 5,
                stockSymbol = "AC.PA",
                number = 4,
                buy = 168.84,
                date = LocalDateTime.of(2024, 5, 10, 0, 0).format(formatter)
            ), PositionDB(
                id = 6,
                stockSymbol = "ORA.PA",
                number = 15,
                buy = 160.93,
                date = LocalDateTime.of(2024, 5, 10, 0, 0).format(formatter)
            ), PositionDB(
                id = 7,
                stockSymbol = "STLAP.PA",
                number = 5,
                buy = 101.08,
                date = LocalDateTime.of(2024, 5, 6, 0, 0).format(formatter)
            ), PositionDB(
                id = 8,
                stockSymbol = "WPEA.PA",
                number = 44,
                buy = 228.82,
                date = LocalDateTime.of(2024, 8, 16, 0, 0).format(formatter)
            ), PositionDB(
                id = 9,
                stockSymbol = "WPEA.PA",
                number = 20,
                buy = 104.26,
                date = LocalDateTime.of(2024, 7, 22, 0, 0).format(formatter)
            ),
            PositionDB(
                id = 10,
                stockSymbol = "SAN.PA",
                number = 1,
                buy = 104.85,
                date = LocalDateTime.of(2024, 9, 3, 0, 0).format(formatter)
            ),
        )

        fun ioThread(f: () -> Unit) {
            Executors.newSingleThreadExecutor().execute(f)
        }
    }


}
