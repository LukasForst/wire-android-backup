package pw.forst.wire.android.backups.database.dto

data class DatabaseDto(
    val namedConversations: List<NamedConversationDto>,
    val directConversations: List<DirectConversationDto>,
    val messages: List<MessageDto>
)
