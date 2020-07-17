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

data class ConversationsDataDto(
    val members: List<ConversationMembersDto>,
    val joined: List<ConversationAddMemberDto>,
    val left: List<ConversationLeaveMembersDto>
)

data class ConversationMembersDto(
    val conversationId: UUID,
    val currentMembers: List<UUID>
)

data class ConversationAddMemberDto(
    val addingUser: UUID,
    val conversationId: UUID,
    val addedUsers: List<UUID>,
    val timeStamp: String
)

data class ConversationLeaveMembersDto(
    val leavingMembers: List<UUID>,
    val conversationId: UUID,
    val timeStamp: String
)
