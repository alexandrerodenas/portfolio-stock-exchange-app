package com.example.portfolio.domain

import android.os.Parcelable

interface ChartRepository {
    suspend fun getCAC40Chart(): List<Parcelable>
}