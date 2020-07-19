package pw.forst.wire.android.backups.database.dto

import java.util.UUID

data class MessageDto(
    val id: UUID,
    val conversationId: UUID,
    val userId: UUID,
    // yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
    val time: String,
    val content: String,
    val edited: Boolean,
    val quote: UUID? = null
)
