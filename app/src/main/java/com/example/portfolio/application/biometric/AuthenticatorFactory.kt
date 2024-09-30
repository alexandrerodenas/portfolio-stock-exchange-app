package com.example.portfolio.application.biometric

import android.content.Context
import com.example.portfolio.BuildConfig
import com.example.portfolio.domain.service.Authenticator

class AuthenticatorFactory {

    fun provide(context: Context, onAuthenticationSucceeded: () -> Unit): Authenticator {
        return if (BuildConfig.DEBUG) {
            FakeBiometricAuthenticator(context, onAuthenticationSucceeded)
        } else {
            BiometricAuthenticator(context, onAuthenticationSucceeded)
        }
    }
}