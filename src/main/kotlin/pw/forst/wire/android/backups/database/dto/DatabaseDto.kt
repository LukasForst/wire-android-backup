package pw.forst.wire.android.backups.database.dto

data class DatabaseDto(
    val metaData: DatabaseMetadata,
    val namedConversations: List<NamedConversationDto>,
    val directConversations: List<DirectConversationDto>,
    val messages: List<MessageDto>,
    val conversationsData: ConversationsDataDto,
    val attachments: List<AttachmentDto>,
    val likings: List<LikingsDto>
)
