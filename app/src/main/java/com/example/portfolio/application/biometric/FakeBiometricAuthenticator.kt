package com.example.portfolio.application.biometric

import android.content.Context
import android.widget.Toast
import com.example.portfolio.domain.service.Authenticator

class FakeBiometricAuthenticator(
    private val context: Context,
    private val onAuthenticationSucceeded: () -> Unit
) : Authenticator {

    override fun authenticate() {
        Toast.makeText(context, "Mock: Authentication succeeded!", Toast.LENGTH_SHORT).show()
        onAuthenticationSucceeded()
    }

    override fun isAvailable(): Boolean {
        Toast.makeText(context, "Mock: Biometrics available", Toast.LENGTH_SHORT).show()
        return true
    }
}