package pw.forst.wire.backups.ios.export

import ai.blindspot.ktoolz.extensions.parseJson
import java.io.File

/**
 * Reads metadata about database export.
 */
internal fun parseDatabaseMetadata(exportJson: File) =
    requireNotNull(parseJson<ExportFileMetadata>(exportJson.readText())) { "It was not possible to read the export metadata!" }

internal data class ExportFileMetadata(
    val modelVersion: String,
    val clientIdentifier: String,
    val userIdentifier: String,
    val appVersion: String,
    val creationTime: String,
    val platform: String
)
