package pw.forst.wire.backups.ios.database.model

import org.jetbrains.exposed.sql.Table

internal object PrimaryKeys : Table("Z_PRIMARYKEY") {
    val entityKey = integer("Z_ENT")
    val name = varchar("Z_NAME", VARCHAR_DEFAULT_SIZE)
    override val primaryKey = PrimaryKey(entityKey)
}
