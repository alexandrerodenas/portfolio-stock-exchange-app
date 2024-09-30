package com.example.portfolio.domain.service

interface Authenticator {
    fun authenticate()
    fun isAvailable(): Boolean
}