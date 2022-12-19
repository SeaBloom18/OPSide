package com.ops.opside.common.utils

import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment

class BiometricsManager (
    private val fragment: Fragment
) {
    private var cryptographyManager: CryptographyManager = CryptographyManager()
    private var cipherTextWrapper: CipherTextWrapper? = null
    private var _listener: BiometricListener? = null

    fun saveCredentials(credentials: String, listener: BiometricListener) {
        if (_listener == null) {
            _listener = listener
        }
        val cipher = cryptographyManager.getInitializedCipherForEncryption(fragment.requireContext())

        if (cipher == null) {
            _listener?.onKeyErrorDeleted()
        } else {
            val biometricPrompt =
                BiometricPromptUtils.createBiometricPrompt(fragment) { authenticationResult ->
                    if (authenticationResult == null) {
                        _listener?.onCredentialsError(true)
                    } else {
                        encryptAndStoreCred(
                            credentials,
                            authenticationResult
                        )
                    }
                }
            val promptInfo = BiometricPromptUtils.createPromptInfo(fragment)
            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
        }
    }

    private fun encryptAndStoreCred(
        authCred: String,
        authResult: BiometricPrompt.AuthenticationResult
    ) {
        authResult.cryptoObject?.cipher?.apply {

            val encryptedAuthCredWrapper = cryptographyManager.encryptData(authCred, this)
            cryptographyManager.persistCiphertextWrapperToSharedPrefs(
                encryptedAuthCredWrapper,
                fragment.requireContext(),
                SHARED_FILE,
                Context.MODE_PRIVATE,
                CIPHER_TEXT_WRAPPER
            )
            _listener?.onSaveFinished()
        }
    }

    fun loadCredentials(listener: BiometricListener) {
        if (_listener == null) {
            _listener = listener
        }

        cipherTextWrapper = cryptographyManager.getCiphertextWrapperFromSharedPrefs(
            fragment.requireContext(),
            SHARED_FILE,
            Context.MODE_PRIVATE,
            CIPHER_TEXT_WRAPPER
        )

        cipherTextWrapper?.let { textWrapper ->
            val cipher = cryptographyManager.getInitializedCipherForDecryption(
                fragment.requireContext(), textWrapper.initializationVector
            )

            if (cipher == null) {
                _listener?.onKeyErrorDeleted()
            } else {
                val biometricPrompt =
                    BiometricPromptUtils.createBiometricPrompt(
                        fragment,
                        ::decryptCredFromStorage
                    )
                val promptInfo = BiometricPromptUtils.createPromptInfo(fragment)
                biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
            }
        }
    }

    private fun decryptCredFromStorage(authResult: BiometricPrompt.AuthenticationResult?) {
        cipherTextWrapper?.let { textWrapper ->
            authResult?.cryptoObject?.cipher?.let {
                val plaintext =
                    cryptographyManager.decryptData(textWrapper.ciphertext, it)
                _listener?.onCredentialsLoaded(plaintext)
            }

            if (authResult == null) _listener?.onCredentialsError(true)
        }
    }

    interface BiometricListener {
        fun onSaveFinished()
        fun onCredentialsError(isSaved: Boolean)
        fun onKeyErrorDeleted()
        fun onCredentialsLoaded(credential: String)
    }
}

const val SHARED_FILE = "SB_SHARED_FILE"
const val CIPHER_TEXT_WRAPPER = "cipher_text_wrapper"