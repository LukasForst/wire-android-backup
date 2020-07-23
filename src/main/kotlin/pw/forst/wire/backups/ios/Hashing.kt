package pw.forst.wire.backups.ios

import org.libsodium.jni.Sodium
import java.nio.ByteBuffer
import java.util.UUID

/*
    Taken from original repo
    https://github.com/wireapp/wire-ios-cryptobox/blob/00ec8c7262d49814744c733c6eaa92e99bcd6b42/WireCryptobox/ChaCha20Encryption.swift#L145
 */

fun iosUuidHash(
    uuid: UUID,
    salt: ByteArray
): ByteArray {
    val uuidBytes = asIosBytes(uuid)
    val hash = ByteArray(Sodium.crypto_secretstream_xchacha20poly1305_keybytes())
    val ret = Sodium.crypto_pwhash(
        hash, hash.size, uuidBytes, uuidBytes.size, salt,
        crypto_pwhash_argon2i_OPSLIMIT_INTERACTIVE,
        crypto_pwhash_argon2i_MEMLIMIT_INTERACTIVE,
        crypto_pwhash_argon2i_ALG_ARGON2I13
    )
    require(ret == 0) { "It was not possible to create hash!" }
    return hash
}

private fun asIosBytes(uuid: UUID): ByteArray =
// because of the bug in the iOS, we need to use 128 bytes
    // https://github.com/wireapp/wire-ios-cryptobox/blob/00ec8c7262d49814744c733c6eaa92e99bcd6b42/WireCryptobox/ChaCha20Encryption.swift#L146
    ByteBuffer.wrap(ByteArray(128)).apply {
        putLong(uuid.mostSignificantBits)
        putLong(uuid.leastSignificantBits)
    }.array()
