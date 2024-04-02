package com.lovisgod.testVisaTTP.handlers

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import java.io.InputStream
import java.io.OutputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

@RequiresApi(Build.VERSION_CODES.M)
class AesCryptoManager {

    private val keystore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }
// cypher basicaly helps to tell how we want to encrypt and decrypt our data, the algorithm, block type, padding, transformation.

    private fun encryptCypher(keyx: SecretKey): Cipher? {
       return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, keyx) // specify the usefulness of the cypher you are creating
            // amd the key to use for the encryption
        }
    }

    private fun getDecryptCipherForIv(iv: ByteArray): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
        }
    }


    // this function should check if key is already created and use it, else it creates a new key
    private fun getKey() : SecretKey {
        val existingKey = keystore.getEntry("secretk", null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: generateKey()
    }

    fun encrypt(bytes: ByteArray, outputStream: OutputStream): ByteArray {
        val encrypter = encryptCypher(getKey())
        val encryptedBytes = encrypter?.doFinal(bytes)
        outputStream.use { // this open the stream for use and close it after being used
            it.write(encrypter!!.iv.size) // this tells the size of the iv to write
            it.write(encrypter.iv) // this writes the iv
            it.write(encryptedBytes!!.size) // this tells the size of the encrypted bytes
            it.write(encryptedBytes) //this writes the encrypted bytes to the stream
        }

        return encryptedBytes!!
    }

    fun decrypt(inputStream: InputStream): ByteArray {
        return inputStream.use {
            val ivSize = it.read() // reads the first value which is the iv size
            val iv  = ByteArray(ivSize)
            it.read(iv)

            val encryptedBytesSize = it.read() //reads the size as written in the outputStream
            val encryptedBytes = ByteArray(encryptedBytesSize)
            it.read(encryptedBytes) // reads the encryptedbytes

            getDecryptCipherForIv(iv).doFinal(encryptedBytes)
        }
    }


    private fun generateKey(): SecretKey{
        return KeyGenerator.getInstance(ALGORITHM).apply {
            init(
                KeyGenParameterSpec.Builder(
                    "secretk",
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(PADDING)
                    .setUserAuthenticationRequired(false)
                    .setRandomizedEncryptionRequired(true)
                    .build()
            )
        }.generateKey()
    }


   companion object {
       private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
       private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
       private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
       private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
   }


}