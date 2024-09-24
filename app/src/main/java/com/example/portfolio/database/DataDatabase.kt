package com.example.portfolio.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.portfolio.domain.StockDao
import java.util.concurrent.Executors

@Database(entities = [StockDB::class], version = 1)
abstract class DataDatabase : RoomDatabase() {
    abstract fun stockDao(): StockDao


    companion object {

        @Volatile private var INSTANCE: DataDatabase? = null

        fun getInstance(context: Context): DataDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                DataDatabase::class.java, "stock.db")
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        ioThread {
                            getInstance(context).stockDao().insertStocks(PREPOPULATE_DATA)
                        }
                    }
                })
                .build()

        val PREPOPULATE_DATA = listOf(
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

        fun ioThread(f : () -> Unit) {
            Executors.newSingleThreadExecutor().execute(f)
        }
    }



}
