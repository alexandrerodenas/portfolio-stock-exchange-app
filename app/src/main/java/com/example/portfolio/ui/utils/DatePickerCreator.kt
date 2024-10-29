package com.example.portfolio.ui.utils

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.portfolio.database.converter.DateConverter
import java.time.LocalDateTime
import java.util.Calendar

class DatePickerCreator {

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun getDateOfNow(): String {
            val currentDateTime = LocalDateTime.now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)

            return DateConverter().dateToString(currentDateTime)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun create(context: Context, onNewDate: (date: LocalDateTime) -> Unit): DatePickerDialog {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                context,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = LocalDateTime.of(
                        selectedYear,
                        selectedMonth + 1,
                        selectedDay,
                        0,
                        0,
                        0,
                        0
                    )
                    onNewDate(selectedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
            return datePickerDialog
        }
    }
}