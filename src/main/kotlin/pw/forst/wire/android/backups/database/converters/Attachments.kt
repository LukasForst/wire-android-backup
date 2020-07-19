package pw.forst.wire.android.backups.database.converters

import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.selectAll
import pw.forst.wire.android.backups.database.dto.AttachmentDto
import pw.forst.wire.android.backups.database.model.Assets2
import pw.forst.wire.android.backups.database.model.Messages

@Suppress("unused") // because we need it to run inside transaction
fun Transaction.getAttachments(): List<AttachmentDto> =
    (Assets2 leftJoin Messages).selectAll().map {
        AttachmentDto(
            id = it[Messages.id].toUuid(),
            conversationId = it[Messages.conversationId].toUuid(),
            name = it[Assets2.name],
            sender = it[Messages.userId].toUuid(),
            timestamp = convertToStringTime(it[Messages.time]),
            contentLength = it[Assets2.size],
            mimeType = it[Assets2.mime],
            assetToken = requireNotNull(it[Assets2.token]) { "Asset token was null!" },
            assetKey = it[Assets2.id],
            sha = it[Assets2.sha].bytes,
            decryptionKey = it[Assets2.encryption].trim(),
            protobuf = requireNotNull(it[Messages.protos]?.bytes) { "Protobuf for asset was null!" }
        )
    }

