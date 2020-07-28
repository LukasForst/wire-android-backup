package pw.forst.wire.backups.ios.model

import java.util.UUID

/**
 * Represents single reaction to something.
 */
data class ReactionDto(
    /**
     * Value of reaction.
     */
    val unicodeValue: String,
    /**
     * Who reacted.
     */
    val userId: UUID
)
