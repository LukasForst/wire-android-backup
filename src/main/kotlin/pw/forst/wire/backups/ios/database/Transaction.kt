package pw.forst.wire.backups.ios.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import java.io.File

/**
 * Wrapper for [org.jetbrains.exposed.sql.transactions.transaction].
 */
internal fun <T> transaction(sqliteDatabase: File, statement: Transaction.() -> T) =
    transaction(sqliteDatabase.absolutePath, statement)


/**
 * Wrapper for [org.jetbrains.exposed.sql.transactions.transaction].
 */
internal fun <T> transaction(sqliteDatabasePath: String, statement: Transaction.() -> T) =
    org.jetbrains.exposed.sql.transactions.transaction(
        Database.connect("jdbc:sqlite:$sqliteDatabasePath"), statement
    )
