package pw.forst.wire.backups.ios.database.model

import org.jetbrains.exposed.sql.Table

internal object GenericMessageData : Table("ZGENERICMESSAGEDATA") {
    val id = integer("Z_PK")
    val proto = blob("ZDATA")
    val message = integer("ZMESSAGE") references Messages.id
    override val primaryKey = PrimaryKey(id)
}
