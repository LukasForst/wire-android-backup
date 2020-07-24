package pw.forst.wire.backups.ios.database.model

import org.jetbrains.exposed.sql.Table

internal object Conversations : Table("ZCONVERSATION") {
    val id = integer("Z_PK")
    val remoteUuid = blob("ZREMOTEIDENTIFIER_DATA")
    override val primaryKey = PrimaryKey(GenericMessageData.id)
}
