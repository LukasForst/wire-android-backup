package pw.forst.wire.backups.ios

import java.nio.ByteBuffer
import java.nio.charset.Charset

/*
    Taken from original iOS repo:
    https://github.com/wireapp/wire-ios-cryptobox/blob/00ec8c7262d49814744c733c6eaa92e99bcd6b42/WireCryptobox/ChaCha20Encryption.swift#L88
 */
enum class HeaderParameters(
    val headerOrder: Int,
    val len: Int,
    val isValid: (Any) -> Boolean
) {
    PLATFORM(0, 4, { it == "WBUI" }),
    EMPTY_SPACE(1, 1, { true }),
    VERSION(2, 2, { it == 1.toShort() }),
    SALT(3, 16, { true }),
    UUID_HASH(4, 32, { true })
}

/**
 * Note that this function modifies the passed [buffer].
 */
fun readHeader(buffer: ByteBuffer): EncryptedBackupHeader {
    ByteArray(HeaderParameters.PLATFORM.len)
        .apply { buffer.get(this) }
        .toString(Charset.forName("UTF-8"))
        .also { require(HeaderParameters.PLATFORM.isValid(it)) { "Platform name not valid!" } }

    // skip null byte
    buffer.get()

    // check version
    require(HeaderParameters.VERSION.isValid(buffer.short)) { "Wrong version supplied!" }
    // parse header
    return EncryptedBackupHeader(
        salt = ByteArray(HeaderParameters.SALT.len).also { buffer.get(it) },
        uuidHash = ByteArray(HeaderParameters.UUID_HASH.len).also { buffer.get(it) }
    )
}

data class EncryptedBackupHeader(
    val salt: ByteArray,
    val uuidHash: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EncryptedBackupHeader

        if (!salt.contentEquals(other.salt)) return false
        if (!uuidHash.contentEquals(other.uuidHash)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = salt.contentHashCode()
        result = 31 * result + uuidHash.contentHashCode()
        return result
    }
}
