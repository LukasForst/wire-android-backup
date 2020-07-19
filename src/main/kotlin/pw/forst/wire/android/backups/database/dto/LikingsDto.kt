package pw.forst.wire.android.backups.database.dto

import pw.forst.wire.android.backups.database.converters.ExportDate
import java.util.UUID

data class LikingsDto(
    val messageId: UUID,
    val userId: UUID,
    val conversationId: UUID,
    val time: ExportDate
)
