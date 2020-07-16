package pw.forst.wire.android.backups.steps

import ai.blindspot.ktoolz.extensions.whenFalse
import net.lingala.zip4j.ZipFile
import java.io.File
import java.util.UUID

/**
 * Extracts SQLite database file.
 */
fun extractBackup(decryptedBackupZip: File, userId: UUID, pathToNewFile: String): File {
    val backupName = userId.toString()
    // extract file
    ZipFile(decryptedBackupZip).extractFile(backupName, pathToNewFile)
    // rename file
    File("$pathToNewFile${File.separator}$backupName")
        .renameTo(File("$pathToNewFile${File.separator}$backupName.sqlite"))
        .whenFalse { throw IllegalStateException("It was not possible to rename the file!") }
    return File("$pathToNewFile${File.separator}$backupName.sqlite")
}
