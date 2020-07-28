package pw.forst.wire.backups.ios.export

import net.lingala.zip4j.ZipFile
import java.io.File
import java.time.Instant

/**
 * Writes bytes to zip file and extracts them.
 */
internal fun extractDatabaseFiles(decryptedBytes: ByteArray, outputPath: String): Pair<File, File> =
    File.createTempFile("wire-ios-${Instant.now()}", "zip")
        .apply {
            deleteOnExit()
            writeBytes(decryptedBytes)
        }
        .let { ZipFile(it) }
        .apply {
            extractFile("data/store.wiredatabase", outputPath)
            extractFile("export.json", outputPath)
        }.let { File("$outputPath/export.json") to File("$outputPath/data/store.wiredatabase") }