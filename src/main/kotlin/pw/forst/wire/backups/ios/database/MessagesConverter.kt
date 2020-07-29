package pw.forst.wire.backups.ios.database

import org.jetbrains.exposed.sql.ColumnSet
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import pw.forst.wire.backups.android.database.converters.toExportDateFromIos
import pw.forst.wire.backups.ios.database.model.GenericMessageData
import pw.forst.wire.backups.ios.database.model.Messages
import pw.forst.wire.backups.ios.model.IosMessageDto
import java.io.File

/**
 * Exports all messages from the given iOS database.
 */
@Suppress("unused") // used from the Java
fun obtainIosMessages(decryptedDatabaseFile: File): List<IosMessageDto> =
    obtainIosMessages(decryptedDatabaseFile.absolutePath)

/**
 * Exports all messages from the given iOS database.
 */
fun obtainIosMessages(decryptedDatabaseFile: String): List<IosMessageDto> =
    transaction(Database.connect("jdbc:sqlite:$decryptedDatabaseFile")) {
        getGenericMessages()
    }

internal fun Transaction.getGenericMessages(): List<IosMessageDto> =
    getGenericMessages(buildMappingCache())

@Suppress("unused") // ensure that it is running in the transaction
internal fun Transaction.getGenericMessages(cache: EntityMappingCache): List<IosMessageDto> =
    with(cache) {
        getMessages(this) + getAssets(this)
    }.sortedBy { it.time }

// TODO maybe to use entity mapping keys (Z_PRIMARYKEY data) to distinguish assets from messages
private fun getMessages(cache: EntityMappingCache): List<IosMessageDto> =
    GenericMessageData.join(Messages, JoinType.LEFT,
        onColumn = GenericMessageData.messageId,
        otherColumn = null,
        additionalConstraint = { GenericMessageData.messageId.isNotNull() }
    ).messagesSlice().select {
        GenericMessageData.messageId.isNotNull() and
                GenericMessageData.assetId.isNull() and
                GenericMessageData.messageId.eq(Messages.id) and
                Messages.conversationId.isNotNull()  // TODO verify this, but it seems to be temporal messages
    }.map { mapGenericMessage(it, cache) }

// TODO maybe to use entity mapping keys (Z_PRIMARYKEY data) to distinguish assets from messages
private fun getAssets(cache: EntityMappingCache): List<IosMessageDto> =
    GenericMessageData.join(Messages, JoinType.LEFT,
        onColumn = GenericMessageData.assetId,
        otherColumn = null,
        additionalConstraint = { GenericMessageData.assetId.isNotNull() }
    ).messagesSlice().select {
        GenericMessageData.messageId.isNull() and
                GenericMessageData.assetId.isNotNull() and
                GenericMessageData.assetId.eq(Messages.id) and
                Messages.conversationId.isNotNull() // TODO verify this, but it seems to be temporal messages
    }.map { mapGenericMessage(it, cache) }

private fun mapGenericMessage(it: ResultRow, cache: EntityMappingCache) =
    IosMessageDto(
        id = it[Messages.id],
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
