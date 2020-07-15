package pw.forst.wire.android.backups.database.dto

import java.util.UUID

data class NamedConversationDto(
    val id: UUID,
    val name: String
)

data class DirectConversationDto(
    val id: UUID,
    val otherUser: UUID
)
