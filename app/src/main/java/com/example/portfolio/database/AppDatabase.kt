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
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database.stockDao())
                    }
                }
            }
        }


        private suspend fun populateDatabase(stockDao: StockDao) {
            val stocks = listOf(
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
            stockDao.insertStocks(stocks)
        }
    }
}
