package com.example.portfolio.domain.model

import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.example.portfolio.database.converter.DateConverter
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
class Dividend(
    val stock: Stock,
    val amount: Double,
    val date: LocalDateTime
) : Parcelable {

    @RequiresApi(value = 26)
    fun readableDate(): String {
        return DateConverter().dateToString(this.date)
    }
}