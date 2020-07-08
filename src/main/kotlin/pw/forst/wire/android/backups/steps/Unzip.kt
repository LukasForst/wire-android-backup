package pw.forst.wire.android.backups.steps

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
    return File("$pathToNewFile${File.separator}$backupName").apply {
        renameTo(File("$pathToNewFile${File.separator}$backupName.sqlite"))
    }
}
