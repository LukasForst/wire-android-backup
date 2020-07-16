package pw.forst.wire.android.backups.database.converters

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import pw.forst.wire.android.backups.database.dto.MessageDto
import pw.forst.wire.android.backups.database.model.MessageContent
import pw.forst.wire.android.backups.database.model.Messages
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.UUID

private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    .withZone(ZoneOffset.UTC)


fun Transaction.getTextMessages() =
    (Messages leftJoin MessageContent).select {
        (Messages.messageType eq "Text") and
                (Messages.id eq MessageContent.messageId)
    }.map { row ->
        MessageDto(
            id = UUID.fromString(row[Messages.id]),
            conversationId = UUID.fromString(row[Messages.conversationId]),
            userId = UUID.fromString(row[Messages.userId]),
            time = Instant.ofEpochMilli(row[Messages.time]).let { dateFormatter.format(it) },
            content = row[MessageContent.content],
            quote = row[Messages.quote]?.let { quote -> UUID.fromString(quote) }
        )
    }


fun main() {
    val db = Database.connect(
        "jdbc:sqlite:/Users/lukas/work/wire/android-db-decryption/2f9e89c9-78a7-477d-8def-fbd7ca3846b5.sqlite"
    )

    val myId = "2f9e89c9-78a7-477d-8def-fbd7ca3846b5"

    transaction {
        println(getTextMessages().joinToString("\n"))
    }
}
