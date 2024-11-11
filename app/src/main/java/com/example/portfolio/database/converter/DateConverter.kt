package com.example.portfolio.database.converter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss"
private const val DATE_PATTERN = "yyyy-MM-dd"

class DateConverter {
    @RequiresApi(Build.VERSION_CODES.O)
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)
    @RequiresApi(Build.VERSION_CODES.O)
    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN)

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun stringToDate(value: String): LocalDateTime {
        return value.let {
            LocalDateTime.parse(it, dateTimeFormatter)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fullDateToString(date: LocalDateTime): String {
        return date.format(dateTimeFormatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dateToString(date: LocalDateTime): String {
        return date.format(dateFormatter)
    }
}