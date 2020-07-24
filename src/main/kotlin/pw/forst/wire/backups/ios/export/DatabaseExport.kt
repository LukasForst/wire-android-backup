package pw.forst.wire.backups.ios.export

import pw.forst.wire.backups.ios.decryption.decryptIosBackup
import pw.forst.wire.backups.ios.model.IosDatabase
import java.io.File
import java.util.UUID

/**
 * Decrypts database and returns database metadata and database file.
 */
@Suppress("unused") // used by the Java APi
fun exportIosDatabase(inputFile: String, password: String, userId: String) =
// we could use default parameters value, but Java wouldn't be able to use it,
// thus overloading
    exportIosDatabase(inputFile, password, userId, "./")


/**
 * Decrypts database and returns database metadata and database file.
 */
fun exportIosDatabase(inputFile: String, password: String, userId: String, outputPath: String): IosDatabase {
    val decryptedBytes = decryptIosBackup(
        File(inputFile),
        password = password,
        userId = UUID.fromString(userId)
    )

    val (exportJson, databaseFile) = extractDatabaseFiles(decryptedBytes, outputPath)
    val metadata = parseDatabaseMetadata(exportJson)
    return IosDatabase(
        userId = UUID.fromString(metadata.userIdentifier),
        clientIdentifier = metadata.clientIdentifier,
        modelVersion = metadata.modelVersion,
        creationTime = metadata.creationTime,
        platform = metadata.platform,
        databaseFile = databaseFile
    )
}
