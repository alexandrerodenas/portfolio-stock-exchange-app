package com.example.portfolio.ui

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val ONE_DAY_AS_SECONDS = 24 * 3600

class TimeRangeValueFormatter(private val timestamps: List<Long>) : ValueFormatter() {

    private val dailyFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val weeklyFormatter = SimpleDateFormat("dd MMM", Locale.getDefault())

    override fun getFormattedValue(value: Float): String {

        val date = Date(value.toLong() * 1000)
        val timeRange = determineTimeRange()

        return when (timeRange) {
            TimeRange.DAILY -> dailyFormatter.format(date)
            TimeRange.WEEKLY -> weeklyFormatter.format(date)
        }
    }

    private fun determineTimeRange(): TimeRange {
        val duration = timestamps.last() - timestamps.first()
        return when {
            duration <= ONE_DAY_AS_SECONDS -> TimeRange.DAILY
            else -> TimeRange.WEEKLY
        }
    }

    private enum class TimeRange {
        DAILY, WEEKLY
    }
}
