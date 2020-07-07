package com.wire.integrations.backups

import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.nio.ByteBuffer

// some magic constants from the original repo https://github.com/wireapp/wire-android
private const val androidMagicNumber = "WBUA"
private const val currentVersion: Short = 2
private const val saltLength = 16
private const val uuidHashLength = 32

private const val androidMagicNumberLength = 4

/**
 * Length of the header in the encrypted file. Magic constant taken from https://github.com/wireapp/wire-android
 */
const val totalHeaderLength = androidMagicNumberLength + 1 + 2 + saltLength + uuidHashLength + 4 + 4

/**
 * Reads encrypted meta data, returns null if it was not possible to read them.
 */
fun readEncryptedMetadata(encryptedBackup: File): EncryptedBackupHeader? =
    when {
        encryptedBackup.length() > totalHeaderLength -> parse(readFileBytes(encryptedBackup, byteCount = totalHeaderLength))
        else -> null.also { print("Backup file header corrupted or invalid") }
    }

/**
 * Read file starting from [offset].
 */
fun readFileBytes(file: File, offset: Int = 0, byteCount: Int? = null): ByteArray {
    val outputSize = byteCount ?: file.length().toInt() - offset

    return ByteArray(outputSize).apply {
        BufferedInputStream(FileInputStream(file)).use {
            it.skip(offset.toLong())
            it.read(this)
        }
    }
}

private fun parse(bytes: ByteArray): EncryptedBackupHeader? {
    val buffer = ByteBuffer.wrap(bytes)

    if (bytes.size != totalHeaderLength)
        return null.also { print("Invalid header length") }

    val magicNumber = ByteArray(androidMagicNumberLength)
        .apply { buffer.get(this) }
        .joinToString("") { it.toChar().toString() }

    if (magicNumber != androidMagicNumber)
        return null.also { print("archive has incorrect magic number") }

    // skip null byte
    buffer.get()

    // check version
    if (buffer.short != currentVersion)
        return null.also { print("Unsupported backup version") }

    // parse header
    return with(buffer) {
        // create buffers fro results
        val salt = ByteArray(saltLength).also { get(it) }
        val uuidHash = ByteArray(uuidHashLength).also { get(it) }
        // read limits
        val opslimit = int
        val memlimit = int
        // create header
        EncryptedBackupHeader(salt, uuidHash, opslimit, memlimit, currentVersion)
    }
}


data class EncryptedBackupHeader(
    val salt: ByteArray,
    val uuidHash: ByteArray,
    val opslimit: Int,
    val memlimit: Int,
    val version: Short = currentVersion
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EncryptedBackupHeader

        if (!salt.contentEquals(other.salt)) return false
        if (!uuidHash.contentEquals(other.uuidHash)) return false
        if (opslimit != other.opslimit) return false
        if (memlimit != other.memlimit) return false
        if (version != other.version) return false

        return true
    }

    override fun hashCode(): Int {
        var result = salt.contentHashCode()
        result = 31 * result + uuidHash.contentHashCode()
        result = 31 * result + opslimit
        result = 31 * result + memlimit
        result = 31 * result + version
        return result
    }
}
