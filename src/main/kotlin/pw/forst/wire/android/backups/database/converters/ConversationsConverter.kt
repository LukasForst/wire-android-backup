package pw.forst.wire.android.backups.database.converters

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import pw.forst.wire.android.backups.database.dto.DatabaseMetadata
import pw.forst.wire.android.backups.database.dto.DirectConversationDto
import pw.forst.wire.android.backups.database.dto.NamedConversationDto
import pw.forst.wire.android.backups.database.model.ConversationMembers
import pw.forst.wire.android.backups.database.model.Conversations
import pw.forst.wire.android.backups.database.model.Users
import java.util.UUID

fun Transaction.getNamedConversations() =
    Conversations.slice(Conversations.id, Conversations.name)
        .select { Conversations.name.isNotNull() }
        .map {
            NamedConversationDto(
                UUID.fromString(it[Conversations.id]),
                requireNotNull(it[Conversations.name]) { "Name must be not null!" }
            )
        }

fun Transaction.getDirectMessages(myId: UUID) =
    (Conversations leftJoin ConversationMembers)
        .slice(Conversations.id, Conversations.name, ConversationMembers.userId)
        .select {
            (Conversations.name.isNull()) and
                    (Conversations.id eq ConversationMembers.conversationId) and
                    (ConversationMembers.userId neq myId.toString())
        }.map {
            DirectConversationDto(
                UUID.fromString(it[Conversations.id]),
                UUID.fromString(it[ConversationMembers.userId])
            )
        }

fun Transaction.getDatabaseMetadata(myId: UUID) =
    Users.select {
        Users.id eq myId.toString()
    }.first().let {
        DatabaseMetadata(
            userId = UUID.fromString(it[Users.id]),
            name = it[Users.name],
            handle = it[Users.handle],
            email = it[Users.email]
        )
    }

fun main() {
    val db = Database.connect(
        "jdbc:sqlite:/Users/lukas/work/wire/android-db-decryption/2f9e89c9-78a7-477d-8def-fbd7ca3846b5.sqlite"
    )

    val myId = UUID.fromString("2f9e89c9-78a7-477d-8def-fbd7ca3846b5")

    transaction {
        println(getNamedConversations())
        println(getDirectMessages(myId))


    }
}
