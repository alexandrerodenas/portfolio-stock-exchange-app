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
import com.example.portfolio.database.converter.PositionConverter
import com.example.portfolio.database.model.PositionDB
import com.example.portfolio.database.model.StockDB
import com.example.portfolio.domain.dao.PositionDao
import com.example.portfolio.domain.dao.StockDao
import java.io.InputStream
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
                    val inputStream: InputStream = context.assets.open("positions.yaml")
                    val positionsToInsert = PositionConverter.fromYaml(inputStream)
                    getInstance(context).stockDao().insert(STOCKS)
                    getInstance(context).positionDao().insert(positionsToInsert)
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


        fun ioThread(f: () -> Unit) {
            Executors.newSingleThreadExecutor().execute(f)
        }
    }


}
