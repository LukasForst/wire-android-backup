package com.wire.integrations.backups

import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.nio.ByteBuffer

const val androidMagicNumber = "WBUA"
const val currentVersion: Short = 2
const val saltLength = 16
const val uuidHashLength = 32

const val androidMagicNumberLength = 4
const val totalHeaderLength = androidMagicNumberLength + 1 + 2 + saltLength + uuidHashLength + 4 + 4

data class EncryptedBackupHeader(
    val salt: ByteArray,
    val uuidHash: ByteArray,
    val opslimit: Int,
    val memlimit: Int,
    val version: Short = currentVersion
)

fun readEncryptedMetadata(encryptedBackup: File): EncryptedBackupHeader? =
    if (encryptedBackup.length() > totalHeaderLength) {
        val encryptedMetadataBytes = readFileBytes(encryptedBackup, byteCount = totalHeaderLength)
        parse(encryptedMetadataBytes)
    } else {
        print("Backup file header corrupted or invalid")
        null
    }

fun readFileBytes(file: File, offset: Int = 0, byteCount: Int? = null): ByteArray {
    val outputSize = byteCount ?: file.length().toInt() - offset
    val result = ByteArray(outputSize)
    BufferedInputStream(FileInputStream(file)).use {
        it.skip(offset.toLong())
        it.read(result)
    }
    return result
}

fun parse(bytes: ByteArray): EncryptedBackupHeader? {
    val buffer = ByteBuffer.wrap(bytes)

    return if (bytes.size == totalHeaderLength) {
        val magicNumber = ByteArray(androidMagicNumberLength)
        buffer.get(magicNumber)

        if (magicNumber.joinToString("") { it.toChar().toString() } == androidMagicNumber) {
            buffer.get() //skip null byte
            val version = buffer.short
            if (version == currentVersion) {
                val salt = ByteArray(saltLength)
                val uuidHash = ByteArray(uuidHashLength)
                buffer.get(salt)
                buffer.get(uuidHash)
                val opslimit = buffer.int
                val memlimit = buffer.int
                EncryptedBackupHeader(salt, uuidHash, opslimit, memlimit, currentVersion)
            } else {
                print("Unsupported backup version")
                null
            }
        } else {
            print("archive has incorrect magic number")
            null
        }
    } else {
        print("Invalid header length")
        null
    }
}
