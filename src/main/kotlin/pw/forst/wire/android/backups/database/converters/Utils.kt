package pw.forst.wire.android.backups.database.converters

import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.UUID

/**
 * String date format in yyyy-MM-dd'T'HH:mm:ss.SSS'Z' format.
 */
typealias ExportDate = String

internal fun Long.toExportDate(): ExportDate = Instant.ofEpochMilli(this).let { dateFormatter.format(it) }

internal fun String.toUuid(): UUID = UUID.fromString(this)

private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    .withZone(ZoneOffset.UTC)
