package com.wire.integrations.backups

import org.libsodium.jni.NaCl
import org.libsodium.jni.Sodium


private val streamHeaderLength by lazy { Sodium.crypto_secretstream_xchacha20poly1305_headerbytes() }

/**
 * Loads necessary JNI libraries. Must be called before using Sodium.
 */
fun initSodium() {
    NaCl.sodium() // dynamically load the libsodium library
}

/**
 * Decrypts given [input].
 */
fun decrypt(input: ByteArray, password: ByteArray, salt: ByteArray): ByteArray? {
    val key = hash(password, salt)
        ?: return null.also { print("Couldn't derive key from password") }

    if (key.size != Sodium.crypto_secretstream_xchacha20poly1305_keybytes()) {
        print("Key length invalid: ${key.size} did not match ${Sodium.crypto_secretstream_xchacha20poly1305_keybytes()}")
        return null
    }

    val header = input.take(streamHeaderLength).toByteArray()
    val cipherText = input.drop(streamHeaderLength).toByteArray()

    val state = initPull(key, header)
        ?: return null.also { print("Failed to init decrypt") }
    // output if decryption was success
    val decrypted = ByteArray(cipherText.size + Sodium.crypto_secretstream_xchacha20poly1305_abytes())
    // decrypt data
    val returnCode = Sodium.crypto_secretstream_xchacha20poly1305_pull(
        state, decrypted, IntArray(0), ByteArray(1), cipherText, cipherText.size,
        ByteArray(0), 0
    )

    return if (returnCode == 0) decrypted else null.also { print("Failed to decrypt backup, got code $returnCode") }
}

/**
 * Creates hash from the given input.
 */
fun hash(
    passBytes: ByteArray,
    salt: ByteArray,
    opslimit: Int = Sodium.crypto_pwhash_opslimit_interactive(),
    memlimit: Int = Sodium.crypto_pwhash_memlimit_interactive()
): ByteArray? {
    val outputLength = Sodium.crypto_secretstream_xchacha20poly1305_keybytes()
    val output = ByteArray(outputLength)

    val ret = Sodium.crypto_pwhash(
        output, output.size, passBytes, passBytes.size, salt,
        opslimit,
        memlimit,
        Sodium.crypto_pwhash_alg_default()
    )

    return if (ret == 0) output else null
}


private fun initPull(key: ByteArray, header: ByteArray): ByteArray? =
    initializeState(key, header) { s, k, h -> Sodium.crypto_secretstream_xchacha20poly1305_init_pull(s, k, h) }

private fun initializeState(key: ByteArray, header: ByteArray, init: (ByteArray, ByteArray, ByteArray) -> Int): ByteArray? {
    //Got this magic number from https://github.com/joshjdevl/libsodium-jni/blob/master/src/test/java/org/libsodium/jni/crypto/SecretStreamTest.java#L48
    val state = ByteArray(52)
    return when {
        header.size != Sodium.crypto_secretstream_xchacha20poly1305_headerbytes() -> null.also { print("Invalid header length") }
        key.size != Sodium.crypto_secretstream_xchacha20poly1305_keybytes() -> null.also { print("Invalid key length") }
        init(state, header, key) != 0 -> null.also { print("error whilst initializing push") }
        else -> state
    }
}
