package pw.forst.wire.backups.ios.model

/**
 * Data extracted from the database including metadata and messages.
 */
data class IosDatabaseExportDto(
    /**
     * Information about export and the database.
     */
    val metadata: IosDatabaseDto,
    /**
     * Parsed messages from the database.
     */
    val messages: List<IosMessageDto>,
    /**
     * All conversations in the database.
     */
    val conversations: List<ConversationDto>
)
