package pw.forst.wire.android.backups.database.converters

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

@Disabled
class Messages {
    @Test
    fun `test getMessages`() {
        Database.connect(
            "jdbc:sqlite:/Users/lukas/work/wire/android-db-decryption/2f9e89c9-78a7-477d-8def-fbd7ca3846b5.sqlite"
        )

        val messages = transaction {
            getTextMessages()
        }

        assertTrue { messages.isNotEmpty() }
        print(messages.joinToString("\n"))
    }
}
