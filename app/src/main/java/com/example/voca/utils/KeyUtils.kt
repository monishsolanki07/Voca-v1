package com.example.voca.utils

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PublicKey
import java.util.Base64

fun generateKeyPair(alias: String) {
    val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
    if (!keyStore.containsAlias(alias)) {
        val keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore")
        keyPairGenerator.initialize(
            KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY)
                .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                .setKeySize(2048)
                .build()
        )
        keyPairGenerator.generateKeyPair()
    }
}

fun getPublicKey(alias: String): PublicKey? {
    val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
    return keyStore.getCertificate(alias)?.publicKey
}

@RequiresApi(Build.VERSION_CODES.O)
fun encodePublicKeyToBase64(publicKey: PublicKey): String {
    return Base64.getEncoder().encodeToString(publicKey.encoded)
}
