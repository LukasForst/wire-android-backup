package pw.forst.wire.android.backups.database.converters

import ai.blindspot.ktoolz.extensions.jacksonMapper
import ai.blindspot.ktoolz.extensions.whenNull
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import pw.forst.wire.android.backups.database.dto.MessageDto
import pw.forst.wire.android.backups.database.model.Messages
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.UUID

private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    .withZone(ZoneOffset.UTC)


fun Transaction.getTextMessages() =
    Messages.select {
        Messages.messageType eq "Text"
    }.map { row ->
        MessageDto(
            id = UUID.fromString(row[Messages.id]),
            conversationId = UUID.fromString(row[Messages.conversationId]),
            userId = UUID.fromString(row[Messages.userId]),
            time = Instant.ofEpochMilli(row[Messages.time].toLong()).let { dateFormatter.format(it) },
            content = parseContent(row[Messages.content]),
            quote = row[Messages.quote]?.let { quote -> UUID.fromString(quote) }
        )
    }


// TODO support for mentions
private fun parseContent(content: String?): String =
    content
        ?.let { jacksonMapper().readTree(content).get("content").asText() }
        .whenNull { print("No content! - $content") }
        ?: ""


fun main() {
    val db = Database.connect(
        "jdbc:sqlite:/Users/lukas/work/wire/android-db-decryption/2f9e89c9-78a7-477d-8def-fbd7ca3846b5.sqlite"
    )

    val myId = "2f9e89c9-78a7-477d-8def-fbd7ca3846b5"

    transaction {
        println(getTextMessages().joinToString("\n"))
    }
}
