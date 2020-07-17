package pw.forst.wire.android.backups.database.converters

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.Ignore
import kotlin.test.Test

@Ignore
class Messages {
    @Test
    fun `test getMessages`() {
        Database.connect(
            "jdbc:sqlite:/Users/lukas/work/wire/android-db-decryption/2f9e89c9-78a7-477d-8def-fbd7ca3846b5.sqlite"
        )

        transaction {
            println(getTextMessages().joinToString("\n"))
        }
    }
}
