package pw.forst.wire.backups.ios.database.model

import org.jetbrains.exposed.sql.Table

internal object UsersReactions : Table("Z_18USERS") {
    val reactionId = integer("Z_18REACTIONS") references Reactions.id
    val userId = integer("Z_22USERS") references Users.id
    override val primaryKey = PrimaryKey(reactionId, userId)
}
