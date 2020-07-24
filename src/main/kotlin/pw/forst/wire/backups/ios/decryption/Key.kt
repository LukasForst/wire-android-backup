package pw.forst.wire.backups.ios.decryption

import org.libsodium.jni.Sodium

/*
    Taken from original repo
    https://github.com/wireapp/wire-ios-cryptobox/blob/00ec8c7262d49814744c733c6eaa92e99bcd6b42/WireCryptobox/ChaCha20Encryption.swift#L187
 */

/**
 * Derives key for given password and salt.
 */
internal fun deriveKey(password: String, salt: ByteArray): ByteArray {
    val buffer = ByteArray(Sodium.crypto_secretstream_xchacha20poly1305_keybytes())
    val passwordBytes = password.toByteArray()
    val result = Sodium.crypto_pwhash(
        buffer, buffer.size, passwordBytes, passwordBytes.size, salt,
        crypto_pwhash_argon2i_OPSLIMIT_MODERATE,
        crypto_pwhash_argon2i_MEMLIMIT_MODERATE,
        crypto_pwhash_argon2i_ALG_ARGON2I13
    )

    require(result == 0) { "It was not possible to derive key!" }
    return buffer
}
