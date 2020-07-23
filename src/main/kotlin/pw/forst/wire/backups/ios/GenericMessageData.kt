package pw.forst.wire.backups.ios

import org.jetbrains.exposed.sql.Table


object GenericMessageData : Table("ZGENERICMESSAGEDATA") {
    val id = integer("Z_PK")
    val proto = blob("ZDATA")

    override val primaryKey = PrimaryKey(id)
}
