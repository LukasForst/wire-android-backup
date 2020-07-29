package pw.forst.wire.backups.ios.database.model

import org.jetbrains.exposed.sql.Table

internal object SystemMessagesRelatedUsers : Table("Z_14USERS") {

    val userId = integer("Z_22USERS1") references Users.id
    val messageId = integer("Z_14SYSTEMMESSAGES") references Messages.id

    override val primaryKey = PrimaryKey(userId, messageId)
}
