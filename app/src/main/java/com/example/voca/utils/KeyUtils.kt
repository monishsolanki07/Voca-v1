package com.example.voca.utils

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import okio.ByteString.Companion.encode
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PublicKey
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.crypto.Cipher

/**
 * Generates an RSA key pair for encryption/decryption using OAEP padding.
 */
fun generateKeyPair(alias: String) {
    val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
    if (!keyStore.containsAlias(alias)) {
        val keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore")
        keyPairGenerator.initialize(
            KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                .setKeySize(4096)
                .build()
        )
        keyPairGenerator.generateKeyPair()
    }
}

/**
 * Encrypts a message using the provided public key with RSA OAEP (SHA-256) padding.
 * This function is equivalent to the following Python:
 *
 * def encrypt_message(message, public_key):
 *     encrypted_message = public_key.encrypt(
 *         message.encode(),
 *         padding.OAEP(
 *             mgf=padding.MGF1(algorithm=hashes.SHA256()),
 *             algorithm=hashes.SHA256(),
 *             label=None,
 *         ),
 *     )
 *     return base64.b64encode(encrypted_message).decode()
 *
 * Requires Android O (API 26) or later.
 */

@RequiresApi(Build.VERSION_CODES.O)
fun encryptMessage(message: String, publicKey: PublicKey): String {
    val cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding")
    cipher.init(Cipher.ENCRYPT_MODE, publicKey)
    val encryptedBytes = cipher.doFinal(message.toByteArray(Charsets.UTF_8))
    return Base64.getEncoder().encodeToString(encryptedBytes)
}

/**
 * Retrieves the public key from the Android KeyStore for the given alias.
 */
fun getPublicKey(alias: String): PublicKey? {
    val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
    return keyStore.getCertificate(alias)?.publicKey
}

/**
 * Returns the current UTC timestamp in ISO 8601 format.
 */
fun getCurrentTimestamp(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.format(Date())
}

/**
 * Encodes the provided public key to a Base64 string.
 * Requires Android O (API 26) or later.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun encodePublicKeyToPEM(publicKey: PublicKey): String {
    // First, get the base64-encoded DER representation of the public key.
    val base64Der = Base64.getEncoder().encodeToString(publicKey.encoded)
    // Create the PEM-format string.
    val pem = "-----BEGIN PUBLIC KEY-----\n$base64Der\n-----END PUBLIC KEY-----"
    // Now, base64-encode the PEM string so that Django’s b64decode will yield valid UTF-8.
    return Base64.getEncoder().encodeToString(pem.toByteArray(Charsets.UTF_8))
}
