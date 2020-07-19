package pw.forst.wire.android.backups.database.dto

import java.util.UUID

data class LikingsDto(
    val messageId: UUID,
    val userId: UUID,
    val conversationId: UUID,
    // yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
    val time: String
)
