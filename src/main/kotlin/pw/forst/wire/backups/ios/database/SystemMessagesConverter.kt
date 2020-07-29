package pw.forst.wire.backups.ios.database

import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import pw.forst.wire.backups.android.database.converters.toExportDateFromIos
import pw.forst.wire.backups.ios.database.model.Messages
import pw.forst.wire.backups.ios.database.model.SYSTEM_MESSAGE_ENTITY_TYPE
import pw.forst.wire.backups.ios.database.model.SystemMessageType
import pw.forst.wire.backups.ios.database.model.SystemMessagesRelatedUsers
import pw.forst.wire.backups.ios.model.IosUserAddedToConversation
import pw.forst.wire.backups.ios.model.IosUserLeftConversation

@Suppress("unused") // ensure that it is running in the transaction
internal fun Transaction.getUserAddedToConversation(cache: EntityMappingCache): List<IosUserAddedToConversation> =
    userAddedEvents(cache)

@Suppress("unused") // ensure that it is running in the transaction
internal fun Transaction.getUserLeftConversation(cache: EntityMappingCache): List<IosUserLeftConversation> =
    userLeftEvent(cache)

private fun userAddedEvents(cache: EntityMappingCache): List<IosUserAddedToConversation> =
    (SystemMessagesRelatedUsers leftJoin Messages)
        .slice(Messages.senderId, SystemMessagesRelatedUsers.userId, Messages.conversationId, Messages.timestamp)
        .select {
            Messages.entityType.eq(SYSTEM_MESSAGE_ENTITY_TYPE) and
                    Messages.systemMessageType.eq(SystemMessageType.ZMSystemMessageTypeParticipantsAdded.ordinal)
        }.map {
            IosUserAddedToConversation(
                whoAddedUser = cache.getUsersUuid(
                    requireNotNull(it[Messages.senderId]) { "Sender was null!" }
                ),
                addedUser = cache.getUsersUuid(it[SystemMessagesRelatedUsers.userId]),
                conversation = cache.getConversationUuid(
                    requireNotNull(it[Messages.conversationId]) { "Conversation id was null!" }
                ),
                timestamp = it[Messages.timestamp].toExportDateFromIos()
            )
        }

private fun userLeftEvent(cache: EntityMappingCache): List<IosUserLeftConversation> =
    (SystemMessagesRelatedUsers leftJoin Messages)
        .slice(Messages.senderId, SystemMessagesRelatedUsers.userId, Messages.conversationId, Messages.timestamp)
        .select {
            Messages.entityType.eq(SYSTEM_MESSAGE_ENTITY_TYPE) and
                    Messages.systemMessageType.eq(SystemMessageType.ZMSystemMessageTypeParticipantsRemoved.ordinal)
        }.map {
            IosUserLeftConversation(
                userSendingLeftMessage = it[Messages.senderId]?.let { userId -> cache.getUsersUuid(userId) },
                leavingUser = cache.getUsersUuid(it[SystemMessagesRelatedUsers.userId]),
                conversation = cache.getConversationUuid(
                    requireNotNull(it[Messages.conversationId]) { "Conversation id was null!" }
                ),
                timestamp = it[Messages.timestamp].toExportDateFromIos()
            )
        }
