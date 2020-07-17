package pw.forst.wire.android.backups.database
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction


object Assets2: Table("Assets2") {
    val id = text("_id")
}

fun main() {
    val db = Database.connect(
        "jdbc:sqlite:/Users/lukas/work/wire/android-db-decryption/2f9e89c9-78a7-477d-8def-fbd7ca3846b5.sqlite"
    )
    transaction {
        print(Assets2.selectAll().map { it[Assets2.id] })
    }
}
