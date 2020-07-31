package pw.forst.wire.backups.ios.database.model

import org.jetbrains.exposed.sql.Table

internal object SystemMessagesRelatedUsers : Table("Z_${SYSTEM_MESSAGE_ENTITY_TYPE}USERS") {
    val userId = integer("Z_${USER_ENTITY_TYPE}USERS1") references Users.id
    val messageId = integer("Z_${SYSTEM_MESSAGE_ENTITY_TYPE}SYSTEMMESSAGES") references Messages.id

    override val primaryKey = PrimaryKey(userId, messageId)
}
