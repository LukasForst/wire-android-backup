package pw.forst.wire.backups.ios.database

import org.jetbrains.exposed.sql.ColumnSet
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import pw.forst.wire.backups.android.database.converters.toExportDateFromIos
import pw.forst.wire.backups.ios.database.model.GenericMessageData
import pw.forst.wire.backups.ios.database.model.Messages
import pw.forst.wire.backups.ios.model.IosMessageDto

/**
 * Exports all messages from the given decrypted iOS database.
 */
fun obtainIosMessages(decryptedDatabaseFile: String): List<IosMessageDto> =
    transaction(Database.connect("jdbc:sqlite:$decryptedDatabaseFile")) { getGenericMessages() }

internal fun Transaction.getGenericMessages(): List<IosMessageDto> =
    getGenericMessages(buildMappingCache())

@Suppress("unused") // ensure that it is running in the transaction
internal fun Transaction.getGenericMessages(cache: EntityMappingCache) =
    (getMessages(cache) + getAssets(cache)).sortedBy { it.time }

private fun getMessages(cache: EntityMappingCache) =
    GenericMessageData.join(Messages, JoinType.INNER,
        onColumn = GenericMessageData.messageId,
        otherColumn = Messages.id,
        additionalConstraint = { GenericMessageData.messageId.isNotNull() }
    ).messagesSlice()
        .select { Messages.conversationId.isNotNull() }
        .map { mapGenericMessage(it, cache) }

private fun getAssets(cache: EntityMappingCache) =
    GenericMessageData.join(Messages, JoinType.INNER,
        onColumn = GenericMessageData.assetId,
        otherColumn = Messages.id,
        additionalConstraint = { GenericMessageData.assetId.isNotNull() }
    ).messagesSlice()
        .select { Messages.conversationId.isNotNull() }
        .map { mapGenericMessage(it, cache) }

private fun mapGenericMessage(it: ResultRow, cache: EntityMappingCache) =
    IosMessageDto(
        id = it[Messages.id],
        // these requires should be always ok, if this throws exception, the database is inconsistent
        senderUUID = cache.getUsersUuid(requireNotNull(it[Messages.senderId]) { "Sender was null!" }),
        conversationUUID = cache.getConversationUuid(requireNotNull(it[Messages.conversationId]) { "Conversation was null!" }),
        time = it[Messages.timestamp].toExportDateFromIos(),
        protobuf = it[GenericMessageData.proto].bytes,
        wasEdited = it[Messages.updatedTimestamp] != null,
        reactions = cache.getReactionsForMessagePk(it[Messages.id])
    )

private fun ColumnSet.messagesSlice() =
    slice(
        Messages.id, Messages.senderId, Messages.conversationId, Messages.timestamp,
        GenericMessageData.proto, Messages.updatedTimestamp
    )
