package pw.forst.wire.android.backups.database.model

import org.jetbrains.exposed.sql.Table

object Messages: Table("Messages") {
    val id = text("_id")
    val conversationId = text("conv_id") references Conversations.id
    val messageType = text("msg_type")
    val userId = text("user_id")
    val content = text("content").nullable()
    val protos = blob("protos").nullable()
    val time = long("time")
    val firstMessage = integer("first_msg")
    val members = text("members").nullable()
    val recipient = text("recipient").nullable()
    val email = text("email").nullable()
    val name = text("name").nullable()
    val messageState = text("msg_state")
    val contentSize = integer("content_size")
    val localTime = long("local_time")
    val editTime = long("edit_time")
    val ephemeral = integer("ephemeral").nullable()
    val expiryTime = long("expiry_time").nullable()
    val expired = integer("expired")
    val duration = integer("duration").nullable()
    val quote = text("quote").nullable()
    val quoteValidity = integer("quote_validity").nullable()
    val forceReadReceipts = integer("force_read_receipts").nullable()
    val assetId = (text("asset_id") references Assets2.id).nullable()

    override val primaryKey = PrimaryKey(id)
}
