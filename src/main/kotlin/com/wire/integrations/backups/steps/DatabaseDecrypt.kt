package com.wire.integrations.backups.steps

import java.io.File
import java.util.UUID

/**
 * Decrypts given database file for given user.
 */
fun decryptDatabase(databaseFile: File, password: ByteArray, userId: UUID): File? {
    val metadata = readEncryptedMetadata(databaseFile)
        ?: return null.also { print("metadata could not be read") }
    val hash = hash(userId.toString().toByteArray(), metadata.salt)
        ?: return null.also { print("Uuid hashing failed") }

    if (!hash.contentEquals(metadata.uuidHash)) {
        return null.also { print("Uuid hashes don't match") }
    }

    val encryptedBackupBytes = readFileBytes(
        databaseFile,
        totalHeaderLength
    )
    val decrypted = decrypt(encryptedBackupBytes, password, metadata.salt)
        ?: return null.also { print("backup decryption failed") }

    return File.createTempFile("wire_backup", ".zip")
        .also {
            it.deleteOnExit()
            it.writeBytes(decrypted)
        }
}
