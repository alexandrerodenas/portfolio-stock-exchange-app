package com.example.portfolio.domain

import android.os.Parcelable

interface CAC40Repository {
    suspend fun getCAC40Chart(): List<Parcelable>
}