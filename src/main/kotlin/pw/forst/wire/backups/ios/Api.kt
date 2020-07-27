package pw.forst.wire.backups.ios

import pw.forst.wire.backups.ios.database.obtainIosConversations
import pw.forst.wire.backups.ios.database.obtainIosMessages
import pw.forst.wire.backups.ios.export.exportIosDatabase
import pw.forst.wire.backups.ios.model.IosDatabaseExportDto
import java.util.UUID

/**
 * Decrypts and extracts iOS database and loads all information that are necessary for the exports.
 */
@Suppress("unused") // used in Java
fun processIosBackup(
    encryptedBackupPath: String,
    password: String,
    userIdForBackup: String
): IosDatabaseExportDto =
    processIosBackup(
        encryptedBackupPath = encryptedBackupPath,
        password = password,
        userIdForBackup = userIdForBackup,
        outputDirectory = "./ios-backup-export"
    )

/**
 * Decrypts and extracts iOS database and loads all information that are necessary for the exports.
 */
fun processIosBackup(
    encryptedBackupPath: String,
    password: String,
    userIdForBackup: String,
    outputDirectory: String
): IosDatabaseExportDto =
    processIosBackup(
        encryptedBackupFile = encryptedBackupPath,
        password = password,
        userIdForBackup = UUID.fromString(userIdForBackup),
        outputDirectory = outputDirectory
    )

/**
 * Decrypts and extracts iOS database and loads all information that are necessary for the exports.
 */
fun processIosBackup(
    encryptedBackupFile: String,
    password: String,
    userIdForBackup: UUID,
    outputDirectory: String
): IosDatabaseExportDto =
    exportIosDatabase(
        inputFile = encryptedBackupFile,
        password = password,
        userId = userIdForBackup,
        outputPath = outputDirectory
    ).let { database ->
        IosDatabaseExportDto(
            metadata = database,
            messages = obtainIosMessages(decryptedDatabaseFile = database.databaseFile),
            conversations = obtainIosConversations(decryptedDatabaseFile = database.databaseFile, userId = userIdForBackup)
        )
    }
