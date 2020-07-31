package pw.forst.wire.backups.ios.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import pw.forst.wire.backups.ios.database.model.ConversationMembers
import pw.forst.wire.backups.ios.database.model.Conversations
import pw.forst.wire.backups.ios.database.model.Users
import pw.forst.wire.backups.ios.model.ConversationDto
import pw.forst.wire.backups.ios.toUuid
import java.util.UUID

/**
 * Exports all conversations from the given iOS database.
 * [userId] is the id of current user (who created the backup).
 */
fun obtainIosConversations(decryptedDatabaseFile: String, userId: UUID): List<ConversationDto> =
    transaction(Database.connect("jdbc:sqlite:$decryptedDatabaseFile")) {
        getConversations(userId)
    }

@Suppress("unused") // forcing it to run inside the transaction
internal fun Transaction.getConversations(userId: UUID) =
    ConversationMembers
        .innerJoin(Conversations)
        .innerJoin(Users)
        .slice(Conversations.remoteUuid, Conversations.name, Users.remoteUuid, Users.name)
        .selectAll()
        .groupBy(
            { it[Conversations.remoteUuid].bytes.toUuid() to it[Conversations.name] },
            { it[Users.remoteUuid].bytes.toUuid() to it[Users.name] }
        )
        .map { (idName, users) ->
            val (conversationId, conversationName) = idName
            ConversationDto(
                id = conversationId,
                name = conversationName
                // direct message to some other person
                    ?: users.firstOrNull { (id, _) -> id != userId }?.second
                    // direct message to myself
                    ?: users.firstOrNull()?.second
                    // some weird state, but we don't really care about name
                    ?: "No name",
                members = users.map { (id, _) -> id }
            )
        }
