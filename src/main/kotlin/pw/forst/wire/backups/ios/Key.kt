package pw.forst.wire.backups.ios

import org.libsodium.jni.Sodium

fun deriveKey(password: String, salt: ByteArray): ByteArray {
    val buffer = ByteArray(Sodium.crypto_secretstream_xchacha20poly1305_keybytes())
    // TODO maybe this part is wrong? there's nothing explicit in the iOS code
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
