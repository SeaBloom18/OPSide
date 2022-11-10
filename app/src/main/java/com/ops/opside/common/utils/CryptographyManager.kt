package com.ops.opside.common.utils

import android.content.Context
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.google.gson.Gson

import java.math.BigInteger
import java.nio.charset.Charset
import java.security.Key
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.util.Calendar
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.security.auth.x500.X500Principal

/**
 * Handles encryption and decryption
 */
interface CryptographyManager {

    fun getInitializedCipherForEncryption(context: Context): Cipher?

    fun getInitializedCipherForDecryption(context: Context, initializationVector: ByteArray): Cipher?

    fun encryptData(plaintext: String, cipher: Cipher): CipherTextWrapper

    fun decryptData(ciphertext: ByteArray, cipher: Cipher): String

    fun persistCiphertextWrapperToSharedPrefs(
        ciphertextWrapper: CipherTextWrapper,
        context: Context,
        filename: String,
        mode: Int,
        prefKey: String
    )

    fun getCiphertextWrapperFromSharedPrefs(
        context: Context,
        filename: String,
        mode: Int,
        prefKey: String
    ): CipherTextWrapper?

}

fun CryptographyManager(): CryptographyManager = CryptographyManagerImpl()

/**
 * To get an instance of this private CryptographyManagerImpl class, use the top-level function
 * fun CryptographyManager(): CryptographyManager = CryptographyManagerImpl()
 */
private class CryptographyManagerImpl : CryptographyManager {

    override fun getInitializedCipherForEncryption(context: Context): Cipher? {
        val cipher = getCipher()
        val secretKey = getOrCreateSecretKey(context)
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        } catch(e: Exception) {
            return null
        }

        return cipher
    }

    override fun getInitializedCipherForDecryption(
            context: Context,
        initializationVector: ByteArray
    ): Cipher? {
        val cipher = getCipher()
        val secretKey = getOrCreateSecretKey(context)
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, initializationVector))
        } catch (e: Exception) {
            return null
        }
        return cipher
    }

    override fun encryptData(plaintext: String, cipher: Cipher): CipherTextWrapper {
        val cipherText = cipher.doFinal(plaintext.toByteArray(Charset.forName("UTF-8")))
        return CipherTextWrapper(cipherText, cipher.iv)
    }

    override fun decryptData(ciphertext: ByteArray, cipher: Cipher): String {
        val plaintext = cipher.doFinal(ciphertext)
        return String(plaintext, Charset.forName("UTF-8"))
    }

    private fun getCipher(): Cipher {
        val transformation = "$ENCRYPTION_ALGORITHM/$ENCRYPTION_BLOCK_MODE/$ENCRYPTION_PADDING"
        return Cipher.getInstance(transformation)
    }

    private fun getOrCreateSecretKey(context: Context): Key {
        // If Secretkey was previously created for that keyName, then grab and return it.
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null) // Keystore must be loaded before it can be accessed
        keyStore.getKey(SB_BIO_KEYSTORE_ALIAS, null)?.let { return it as SecretKey }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // if you reach here, then a new SecretKey must be generated for that keyName
            val paramsBuilder = KeyGenParameterSpec.Builder(
                    SB_BIO_KEYSTORE_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
            paramsBuilder.apply {
                setBlockModes(ENCRYPTION_BLOCK_MODE)
                setEncryptionPaddings(ENCRYPTION_PADDING)
                setKeySize(KEY_SIZE)
                setUserAuthenticationRequired(true)
            }

            val keyGenParams = paramsBuilder.build()
            val keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES,
                    ANDROID_KEYSTORE
            )
            keyGenerator.init(keyGenParams)
            return keyGenerator.generateKey()

        } else {
            val generator = KeyPairGenerator.getInstance("RSA", ANDROID_KEYSTORE)

            val startDate = Calendar.getInstance()
            val endDate = Calendar.getInstance()
            endDate.add(Calendar.YEAR, 100)

            val builder = KeyPairGeneratorSpec.Builder(context)
                    .setAlias(SB_BIO_KEYSTORE_ALIAS)
                    .setSerialNumber(BigInteger.ONE)
                    .setSubject(X500Principal("CN=$SB_BIO_KEYSTORE_ALIAS CA Certificate"))
                    .setStartDate(startDate.time)
                    .setEndDate(endDate.time)

            generator.initialize(builder.build())
            val keyPair = generator.generateKeyPair()

            return keyPair.private
        }
    }

    override fun persistCiphertextWrapperToSharedPrefs(
        ciphertextWrapper: CipherTextWrapper,
        context: Context,
        filename: String,
        mode: Int,
        prefKey: String
    ) {
        val json = Gson().toJson(ciphertextWrapper)
        context.getSharedPreferences(filename, mode).edit().putString(prefKey, json).apply()
    }

    override fun getCiphertextWrapperFromSharedPrefs(
        context: Context,
        filename: String,
        mode: Int,
        prefKey: String
    ): CipherTextWrapper? {
        val json = context.getSharedPreferences(filename, mode).getString(prefKey, null)
        return Gson().fromJson(json, CipherTextWrapper::class.java)
    }

    companion object {
        private const val SB_BIO_KEYSTORE_ALIAS = "sb_bio_keystore_starbucks"
        private const val KEY_SIZE = 256
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val ENCRYPTION_BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
        private const val ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
        private const val ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
    }
}

data class CipherTextWrapper(val ciphertext: ByteArray, val initializationVector: ByteArray)