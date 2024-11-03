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
import java.util.concurrent.Executors

@TypeConverters(DateConverter::class)
@Database(entities = [StockDB::class, PositionDB::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stockDao(): StockDao
    abstract fun positionDao(): PositionDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val dateConverter: DateConverter = DateConverter()

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
            StockDB("^FCHI", "CAC40", isForeign = false, isIndex = true),
            StockDB("SAN.PA", "Sanofi", isForeign = false, isIndex = false),
            StockDB("AC.PA", "Accor", isForeign = false, isIndex = false),
            StockDB("ORA.PA", "Orange", isForeign = false, isIndex = false),
            StockDB("BNP.PA", "BNP", isForeign = false, isIndex = false),
            StockDB("STLAP.PA", "Stellantis", isForeign = true, isIndex = false),
            StockDB("ATO.PA", "Atos", isForeign = false, isIndex = false),
            StockDB("UBI.PA", "Ubisoft", isForeign = false, isIndex = false),
            StockDB("RNO.PA", "Renault", isForeign = false, isIndex = false),
            StockDB("WPEA.PA", "IShares", isForeign = true, isIndex = false),
            StockDB("ESE.PA", "BNP S&P500", isForeign = true, isIndex = false),
            StockDB("DG.PA", "Vinci", isForeign = false, isIndex = false),
            StockDB("TTE.PA", "Total Energies", isForeign = false, isIndex = false)
        )


        @RequiresApi(Build.VERSION_CODES.O)
        val POSITIONS = listOf(
             PositionDB(
                id = 1,
                stockSymbol = "ATO.PA",
                number = 10,
                buy = 10.0,
                date = dateConverter.dateToString(LocalDateTime.of(2024, 8, 6, 0, 0))
            ),
            PositionDB(
                id = 2,
                stockSymbol = "ATO.PA",
                number = 1,
                buy = 12.0,
                date = dateConverter.dateToString(LocalDateTime.of(2024, 9, 6, 0, 0))
            ),
        )

        fun ioThread(f: () -> Unit) {
            Executors.newSingleThreadExecutor().execute(f)
        }
    }


}
