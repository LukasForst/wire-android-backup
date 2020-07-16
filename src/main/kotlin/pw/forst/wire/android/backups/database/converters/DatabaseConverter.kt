package pw.forst.wire.android.backups.database.converters

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import pw.forst.wire.android.backups.database.dto.DatabaseDto
import java.io.File
import java.util.UUID

fun extractDatabase(userId: UUID, databaseFile: File) = extractDatabase(userId, databaseFile.absolutePath)

fun extractDatabase(userId: UUID, databasePath: String): DatabaseDto {
    Database.connect("jdbc:sqlite:$databasePath")
    return extractDatabase(userId)
}

fun extractDatabase(userId: UUID) = transaction {
    DatabaseDto(
        getDatabaseMetadata(userId),
        getNamedConversations(),
        getDirectMessages(userId),
        getTextMessages()
    )
}
