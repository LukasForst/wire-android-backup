package pw.forst.wire.android.backups.database.converters

import ai.blindspot.ktoolz.extensions.whenNull
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import pw.forst.wire.android.backups.database.dto.MessageDto
import pw.forst.wire.android.backups.database.model.Messages
import java.util.UUID

fun Transaction.getTextMessages() =
    Messages.select {
        Messages.messageType eq "Text"
    }.map {
        MessageDto(
            id = UUID.fromString(it[Messages.id]),
            conversationId = UUID.fromString(it[Messages.conversationId]),
            userId = UUID.fromString(it[Messages.userId]),
            // TODO god knows what this is
            time = it[Messages.time].toString(),
            // TODO what is the formatting?
            content = it[Messages.content].whenNull { print("No content!") } ?: "",
            quote = it[Messages.quote]?.let { quote -> UUID.fromString(quote) }
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
