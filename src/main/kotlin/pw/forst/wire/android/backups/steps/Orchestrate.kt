package pw.forst.wire.android.backups.steps

import java.io.File
import java.util.UUID

/**
 * Decrypts the backup and export the database.
 * Returns database file or null when it was not possible to extract the database.
 */
fun decryptAndExtract(databaseFilePath: String, password: String, userId: String): File? =
    decryptAndExtract(
        File(databaseFilePath),
        password.toByteArray(),
        UUID.fromString(userId)
    )

/**
 * Decrypts the backup and export the database.
 * Returns database file or null when it was not possible to extract the database.
 */
fun decryptAndExtract(databaseFile: File, password: ByteArray, userId: UUID): File? =
    initSodium().let {
        decryptDatabase(databaseFile, password, userId)?.let {
            extractBackup(it, userId, "./")
        }
    }
