package com.example.portfolio.domain

interface Authenticator {
    fun authenticate()
    fun isAvailable(): Boolean
}