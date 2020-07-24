package pw.forst.wire.backups.ios

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import pw.forst.wire.backups.android.database.converters.toExportDate
import java.nio.ByteBuffer
import java.util.UUID

fun obtainIosMessages(pathToDatabaseFile: String): List<IosMessageDto> =
    transaction(Database.connect("jdbc:sqlite:$pathToDatabaseFile")) {
        genericMessages()
    }

private fun genericMessages(): List<IosMessageDto> {
    val conversations = conversationsMap()
    val userMap = usersMap()

    fun isValid(genericMessageId: Int, r: ResultRow): Boolean {
        val conversationId = r[Messages.conversationId]
        val userId = r[Messages.senderId]
        return when {
            userId == null -> false
                .also { println("Message $genericMessageId without sender id!") }
            !userMap.containsKey(userId) -> false
                .also { println("Message $genericMessageId have sender $userId which is not in the database!") }
            conversationId == null -> false
                .also { println("Message $genericMessageId does not have conversation id!") }
            !conversations.containsKey(conversationId) -> false
                .also { println("Message $genericMessageId have conversation $conversationId which is not in the database!") }
            else -> true
        }

    }

    return (GenericMessageData leftJoin Messages)
        .slice(
            Messages.id, GenericMessageData.id, Messages.senderId, Messages.conversationId,
            Messages.timestamp, GenericMessageData.proto
        )
        .selectAll()
        .filter { isValid(it[GenericMessageData.id], it) }
        .map {
            IosMessageDto(
                id = it[GenericMessageData.id],
                senderUUID = userMap.getValue(it[Messages.senderId]!!),
                conversationUUID = conversations.getValue(it[Messages.conversationId]!!),
                time = it[Messages.timestamp].epochSecond.toExportDate(),
                protobuf = it[GenericMessageData.proto].bytes
            )
        }
}

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


private fun ByteArray.toUuid(): UUID =
    ByteBuffer.wrap(this).let {
        UUID(it.long, it.long)
    }
