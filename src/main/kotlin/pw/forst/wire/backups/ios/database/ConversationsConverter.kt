package pw.forst.wire.backups.ios.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import pw.forst.wire.backups.ios.database.model.ConversationMembers
import pw.forst.wire.backups.ios.database.model.Conversations
import pw.forst.wire.backups.ios.database.model.Users
import pw.forst.wire.backups.ios.model.ConversationDto
import pw.forst.wire.backups.ios.toUuid
import java.io.File
import java.util.UUID

/**
 * Exports all conversations from the given iOS database.
 * [userId] is the id of current user (who created the backup).
 */
@Suppress("unused") // used from the Java
fun obtainIosConversations(decryptedDatabaseFile: File, userId: UUID) =
    obtainIosConversations(decryptedDatabaseFile.absolutePath, userId)

/**
 * Exports all conversations from the given iOS database.
 * [userId] is the id of current user (who created the backup).
 */
fun obtainIosConversations(decryptedDatabaseFile: String, userId: UUID): List<ConversationDto> =
    transaction(Database.connect("jdbc:sqlite:$decryptedDatabaseFile")) {
        getConversations(userId)
    }


private fun getConversations(userId: UUID) =
    ConversationMembers
        .leftJoin(Conversations)
        .leftJoin(Users)
        .slice(Conversations.remoteUuid, Conversations.name, Users.remoteUuid, Users.name)
        .select {
            (ConversationMembers.conversationId eq Conversations.id) and
                    (ConversationMembers.userId eq Users.id)
        }
        .groupBy(
            { it[Conversations.remoteUuid].bytes.toUuid() to it[Conversations.name] },
            { it[Users.remoteUuid].bytes.toUuid() to it[Users.name] }
        )
        .map { (idName, users) ->
            val (conversationId, conversationName) = idName
            ConversationDto(
                id = conversationId,
                name = conversationName
                    ?: users.firstOrNull { (id, _) -> id != userId }?.second
                    ?: users.firstOrNull()?.second
                    ?: "No name",
                members = users.map { (id, _) -> id }
            )
        }
