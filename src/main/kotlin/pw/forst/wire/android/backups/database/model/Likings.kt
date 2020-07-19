package pw.forst.wire.android.backups.database.model

import org.jetbrains.exposed.sql.Table

object Likings : Table("Likings") {
    val messageId = text("message_id") references Messages.id
    val userId = text("user_id")
    val timestamp = long("timestamp")
    override val primaryKey = PrimaryKey(messageId, userId)
}
