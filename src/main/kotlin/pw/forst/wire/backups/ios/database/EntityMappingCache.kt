package pw.forst.wire.backups.ios.database

import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.selectAll
import pw.forst.wire.backups.ios.database.model.Conversations
import pw.forst.wire.backups.ios.database.model.Reactions
import pw.forst.wire.backups.ios.database.model.Users
import pw.forst.wire.backups.ios.database.model.UsersReactions
import pw.forst.wire.backups.ios.model.ReactionDto
import pw.forst.wire.backups.ios.toUuid
import java.util.UUID

internal class EntityMappingCache(
    private val userMap: Map<Int, UUID>,
    private val conversationMap: Map<Int, UUID>,
    private val reactionsMap: Map<Int, List<Pair<Int, String>>>
) {
    fun getUsersUuid(primaryKey: Int) = userMap.getValue(primaryKey)

    fun getConversationUuid(primaryKey: Int) = conversationMap.getValue(primaryKey)

    fun getReactionsForMessagePk(primaryKey: Int) =
        reactionsMap.getOrDefault(primaryKey, emptyList())
            .let {
                it.map { (userPk, reaction) ->
                    ReactionDto(
                        userId = getUsersUuid(userPk),
                        unicodeValue = reaction
                    )
                }
            }
}

@Suppress("unused") // we want to force to run it inside transaction
internal fun Transaction.buildMappingCache() =
    EntityMappingCache(
        userMap = usersMap(),
        conversationMap = conversationsMap(),
        reactionsMap = getReactionsMap()
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

internal fun getReactionsMap(): Map<Int, List<Pair<Int, String>>> =
    UsersReactions
        .innerJoin(Reactions)
        .slice(Reactions.unicodeValue, Reactions.messageId, UsersReactions.userId)
        .selectAll()
        .map {
            object {
                val unicodeValue = it[Reactions.unicodeValue]
                val messageId = it[Reactions.messageId]
                val userId = it[UsersReactions.userId]
            }
        }.groupBy { it.messageId }
        .mapValues { (_, value) -> value.map { it.userId to it.unicodeValue } }
