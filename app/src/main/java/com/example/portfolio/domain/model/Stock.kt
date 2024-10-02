package com.example.portfolio.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Stock (
    val name: String,
    val symbol: String,
    val isForeign: Boolean,
    val isIndex: Boolean
) : Parcelable