package pw.forst.wire.backups.ios.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import pw.forst.wire.backups.android.database.converters.toExportDateFromIos
import pw.forst.wire.backups.ios.database.model.Conversations
import pw.forst.wire.backups.ios.database.model.GenericMessageData
import pw.forst.wire.backups.ios.database.model.Messages
import pw.forst.wire.backups.ios.database.model.Users
import pw.forst.wire.backups.ios.model.IosMessageDto
import pw.forst.wire.backups.ios.toUuid
import java.io.File
import java.util.UUID

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
        genericMessages()
    }

private fun genericMessages(): List<IosMessageDto> {
    val conversations = conversationsMap()
    val userMap = usersMap()

    return (getMessages(conversations, userMap) + getAssets(conversations, userMap))
        .sortedBy { it.time }
}

private fun getMessages(conversationMap: Map<Int, UUID>, userMap: Map<Int, UUID>): List<IosMessageDto> =
    GenericMessageData.join(Messages, JoinType.LEFT,
        onColumn = GenericMessageData.messageId,
        otherColumn = null,
        additionalConstraint = { GenericMessageData.messageId.isNotNull() }
    ).select {
        GenericMessageData.messageId.isNotNull() and
                GenericMessageData.assetId.isNull() and
                GenericMessageData.messageId.eq(Messages.id) and
                Messages.conversationId.isNotNull()  // TODO verify this, but it seems to be temporal messages
    }.map { mapGenericMessage(it, conversationMap, userMap) }

private fun getAssets(conversationMap: Map<Int, UUID>, userMap: Map<Int, UUID>): List<IosMessageDto> =
    GenericMessageData.join(Messages, JoinType.LEFT,
        onColumn = GenericMessageData.assetId,
        otherColumn = null,
        additionalConstraint = { GenericMessageData.assetId.isNotNull() }
    ).select {
        GenericMessageData.messageId.isNull() and
                GenericMessageData.assetId.isNotNull() and
                GenericMessageData.assetId.eq(Messages.id) and
                Messages.conversationId.isNotNull() // TODO verify this, but it seems to be temporal messages
    }.map { mapGenericMessage(it, conversationMap, userMap) }

private fun mapGenericMessage(it: ResultRow, conversationMap: Map<Int, UUID>, userMap: Map<Int, UUID>) =
    IosMessageDto(
        id = it[GenericMessageData.id],
        senderUUID = userMap.getValue(requireNotNull(it[Messages.senderId]) { "Sender was null!" }),
        conversationUUID = conversationMap.getValue(requireNotNull(it[Messages.conversationId]) { "Conversation was null!" }),
        time = it[Messages.timestamp].toExportDateFromIos(),
        protobuf = it[GenericMessageData.proto].bytes
    )

private fun conversationsMap(): Map<Int, UUID> =
    Conversations
        .slice(Conversations.id, Conversations.remoteUuid)
        .selectAll()
        .associate { it[Conversations.id] to it[Conversations.remoteUuid].bytes.toUuid() }


private fun usersMap(): Map<Int, UUID> =
    Users
        .slice(Users.id, Users.remoteUuid)
        .selectAll()
        .associate { it[Users.id] to it[Users.remoteUuid].bytes.toUuid() }
