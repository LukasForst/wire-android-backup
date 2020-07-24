package pw.forst.wire.backups.ios.model

import pw.forst.wire.backups.android.database.converters.ExportDate
import java.io.File
import java.util.UUID

data class IosDatabase(
    val userId: UUID,
    val clientIdentifier: String,
    val modelVersion: String,
    val creationTime: ExportDate,
    val platform: String,
    val databaseFile: File
)
