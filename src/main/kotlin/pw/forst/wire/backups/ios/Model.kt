package pw.forst.wire.backups.ios

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.timestamp

const val VARCHAR_DEFAULT_SIZE = 1_000_000_000

object GenericMessageData : Table("ZGENERICMESSAGEDATA") {
    val id = integer("Z_PK")
    val proto = blob("ZDATA")
    val message = integer("ZMESSAGE") references Messages.id
    override val primaryKey = PrimaryKey(id)
}

object Messages : Table("ZMESSAGE") {
    val id = integer("Z_PK")
    val senderId = (integer("ZSENDER") references Users.id).nullable()
    val timestamp = timestamp("ZSERVERTIMESTAMP")
    val conversationId = (integer("ZVISIBLEINCONVERSATION") references Conversations.id).nullable()
    override val primaryKey = PrimaryKey(GenericMessageData.id)
}

object Conversations : Table("ZCONVERSATION") {
    val id = integer("Z_PK")
    val remoteUuid = blob("ZREMOTEIDENTIFIER_DATA")
    override val primaryKey = PrimaryKey(GenericMessageData.id)
}

object Users : Table("ZUSER") {
    val id = integer("Z_PK")
    val name = varchar("ZNAME", VARCHAR_DEFAULT_SIZE).nullable()
    val handle = varchar("ZHANDLE", VARCHAR_DEFAULT_SIZE).nullable()
    val remoteUuid = blob("ZREMOTEIDENTIFIER_DATA")
    override val primaryKey = PrimaryKey(GenericMessageData.id)
}
