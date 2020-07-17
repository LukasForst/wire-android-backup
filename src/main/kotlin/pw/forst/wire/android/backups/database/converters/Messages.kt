package pw.forst.wire.android.backups.database.converters

import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import pw.forst.wire.android.backups.database.dto.MessageDto
import pw.forst.wire.android.backups.database.model.MessageContent
import pw.forst.wire.android.backups.database.model.Messages


@Suppress("unused") // we need to force it to run inside transaction
fun Transaction.getTextMessages() =
    (Messages leftJoin MessageContent)
        .slice(
            Messages.messageType, Messages.id, Messages.conversationId, Messages.userId, Messages.time,
            Messages.quote, MessageContent.messageId, MessageContent.content
        ).select {
            (Messages.messageType eq "Text") and (Messages.id eq MessageContent.messageId)
        }.map { row ->
            MessageDto(
                id = row[Messages.id].toUuid(),
                conversationId = row[Messages.conversationId].toUuid(),
                userId = row[Messages.userId].toUuid(),
                time = convertToStringTime(row[Messages.time]),
                content = row[MessageContent.content],
                quote = row[Messages.quote]?.toUuid()
            )
        }
