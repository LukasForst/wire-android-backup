package pw.forst.wire.android.backups.database.dto

import pw.forst.wire.android.backups.database.converters.ExportDate
import java.util.UUID

data class MessageDto(
    val id: UUID,
    val conversationId: UUID,
    val userId: UUID,
    val time: ExportDate,
    val content: String,
    val edited: Boolean,
    val quote: UUID? = null
)
