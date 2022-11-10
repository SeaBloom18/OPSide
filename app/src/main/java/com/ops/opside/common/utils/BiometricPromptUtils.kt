package com.ops.opside.common.utils

import android.content.Context
import android.hardware.biometrics.BiometricPrompt.BIOMETRIC_ERROR_LOCKOUT
import android.hardware.biometrics.BiometricPrompt.BIOMETRIC_ERROR_USER_CANCELED
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ops.opside.R

object BiometricPromptUtils {
    fun createBiometricPrompt(
        fragment: Fragment,
        processSuccess: (BiometricPrompt.AuthenticationResult?) -> Unit
    ): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(fragment.requireContext())

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                processSuccess(result)
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                if (errorCode == BIOMETRIC_ERROR_USER_CANCELED || errorCode == BIOMETRIC_ERROR_LOCKOUT || errorCode == 13) {
                    processSuccess(null)
                }
            }
        }
        return BiometricPrompt(fragment, executor, callback)
    }

    fun createPromptInfo(fragment: Fragment): BiometricPrompt.PromptInfo =
        BiometricPrompt.PromptInfo.Builder().apply {
            setTitle("Confirma para continuar")
            setSubtitle("Por favor, pon tu dedo sobre el sensor")
            setConfirmationRequired(false)
            setNegativeButtonText(fragment.getString(R.string.common_cancel))
        }.build()


    fun canAuthWithBiometrics(context: Context): Boolean {
        return when(BiometricManager.from(context).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        }
    }
}