package com.example.portfolio.database.converter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val PATTERN = "yyyy-MM-dd HH:mm:ss"

class DateConverter {
    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN)

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromTimestamp(value: String): LocalDateTime {
        return value.let {
            LocalDateTime.parse(it, formatter)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun dateToString(date: LocalDateTime): String {
        return date.format(formatter)
    }
}