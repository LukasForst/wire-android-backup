package pw.forst.wire.android.backups.database.converters

import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.UUID

internal val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    .withZone(ZoneOffset.UTC)

internal fun convertToStringTime(millis: Long) = Instant.ofEpochMilli(millis).let { dateFormatter.format(it) }

internal fun String.toUuid(): UUID = UUID.fromString(this)
