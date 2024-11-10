package com.example.portfolio.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
class Dividend(
    private val stock: Stock,
    private val amount: Double,
    private val date: LocalDateTime
) : Parcelable