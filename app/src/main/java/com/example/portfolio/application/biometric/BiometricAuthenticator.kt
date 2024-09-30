package com.example.portfolio.application.biometric

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE
import androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED
import androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE
import androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executor
import androidx.biometric.BiometricPrompt
import com.example.portfolio.domain.service.Authenticator

class BiometricAuthenticator(
    private val context: Context,
    private val onAuthenticationSucceeded: () -> Unit
) : Authenticator {
    private val executor: Executor = ContextCompat.getMainExecutor(context)

    private val biometricPrompt: BiometricPrompt = BiometricPrompt(context as FragmentActivity, executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(context, "Authentication error: $errString", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(context, "Authentication succeeded!", Toast.LENGTH_SHORT).show()
                onAuthenticationSucceeded()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
            }
        })

    private val promptInfo: BiometricPrompt.PromptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric login")
        .setSubtitle("Log in using your fingerprint")
        .setNegativeButtonText("Cancel")
        .build()

    @RequiresApi(Build.VERSION_CODES.P)
    override fun authenticate() {
        biometricPrompt.authenticate(promptInfo)
    }

    override fun isAvailable(): Boolean {
        val biometricManager = BiometricManager.from(context)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BIOMETRIC_SUCCESS -> true
            BIOMETRIC_ERROR_NO_HARDWARE -> {
                Toast.makeText(context, "No biometric hardware available", Toast.LENGTH_SHORT).show()
                false
            }
            BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(context, "Biometric hardware is unavailable", Toast.LENGTH_SHORT).show()
                false
            }
            BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Toast.makeText(context, "No biometric data enrolled", Toast.LENGTH_SHORT).show()
                false
            }
            else -> false
        }
    }
}
