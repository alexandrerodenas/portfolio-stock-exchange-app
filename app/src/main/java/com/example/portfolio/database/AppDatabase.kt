package com.example.portfolio.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [StockDB::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stockDao(): StockDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "stocks_db"
                )
                    .addCallback(StockDatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class StockDatabaseCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Populate database on a background thread
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database.stockDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(stockDao: StockDao) {
            val stocks = listOf(
                StockDB("CAC40", "^FCHI", false),
                StockDB("Sanofi", "SAN.PA", false),
                StockDB("Accor", "AC.PA", false),
                StockDB("Orange", "ORA.PA", false),
                StockDB("BNP", "BNP.PA", false),
                StockDB("Stellantis", "STLAP.PA", true),
                StockDB("Atos", "ATO.PA", false),
                StockDB("Ubisoft", "UBI.PA", false),
                StockDB("Renault", "RNO.PA", false),
                StockDB("IShares", "WPEA.PA", true)
            )

            stockDao.insertStocks(stocks)
        }
    }
}
