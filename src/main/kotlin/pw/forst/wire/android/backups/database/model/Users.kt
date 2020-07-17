package pw.forst.wire.android.backups.database.model

import org.jetbrains.exposed.sql.Table

object Users : Table("Users") {
    val id = text("_id")
    val name = text("name")
    val email = text("email")
    val handle = text("handle")
    override val primaryKey = PrimaryKey(id)
}
