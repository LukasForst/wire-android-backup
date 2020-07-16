package pw.forst.wire.android.backups.steps

import java.io.File
import java.util.UUID

/**
 * Decrypts the backup and export the database.
 * Returns database file or null when it was not possible to extract the database.
 */
fun decryptAndExtract(databaseFilePath: String, password: String, userId: String): DecryptionResult? =
    decryptAndExtract(
        File(databaseFilePath),
        password.toByteArray(),
        UUID.fromString(userId)
    )

/**
 * Decrypts the backup and export the database.
 * Returns database file or null when it was not possible to extract the database.
 */
fun decryptAndExtract(databaseFile: File, password: ByteArray, userId: UUID): DecryptionResult? =
    initSodium().let {
        decryptDatabase(databaseFile, password, userId)?.let {
            val (metadata, db) = extractBackup(it, userId, "tmp")
            DecryptionResult(metadata, db)
        }
    }

data class DecryptionResult(
    val metadata: ExportMetadata,
    val databaseFile: File
)
