package pw.forst.wire.backups.ios.database.model

import org.jetbrains.exposed.sql.Table

internal object UsersReactions : Table("Z_${REACTION_ENTITY_TYPE}USERS") {
    val reactionId = integer("Z_${REACTION_ENTITY_TYPE}REACTIONS") references Reactions.id
    val userId = integer("Z_${USER_ENTITY_TYPE}USERS") references Users.id
    override val primaryKey = PrimaryKey(reactionId, userId)
}
