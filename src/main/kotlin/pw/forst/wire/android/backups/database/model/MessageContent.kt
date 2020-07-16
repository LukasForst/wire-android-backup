package pw.forst.wire.android.backups.database.model

import org.jetbrains.exposed.sql.Table

object MessageContent : Table("MessageContentIndex_content") {
    val id = integer("docid")
    val conversationId = text("c1conv_id") references Conversations.id
    val messageId = text("c0message_id") references Messages.id
    val content = text("c2content")
    override val primaryKey = PrimaryKey(id)
}
