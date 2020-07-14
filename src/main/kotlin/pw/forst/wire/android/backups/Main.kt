package pw.forst.wire.android.backups

import pw.forst.wire.android.backups.steps.decryptAndExtract
import pw.forst.wire.android.backups.steps.decryptDatabase
import pw.forst.wire.android.backups.steps.extractBackup
import pw.forst.wire.android.backups.steps.initSodium
import pw.forst.wire.android.backups.utils.addLibraryPath
import java.io.File
import java.util.UUID

fun main() {
    addLibraryPath("libs")
    initSodium()

    val db = File("Wire-maciek102-Backup_20200708.android_wbu")
    val userId = UUID.fromString("2f9e89c9-78a7-477d-8def-fbd7ca3846b5")
    val password = "Qwerty1!".toByteArray()
    val decrypted = decryptDatabase(db, password, userId) ?: return

    extractBackup(decrypted, userId, ".")
}

fun main2(args: Array<String>) {
    addLibraryPath("libs")
    initSodium()

    val dbFile = args.firstOrNull() ?: throw IllegalArgumentException("No db file set!")
    val password = args.getOrNull(1) ?: throw IllegalArgumentException("No password set!")
    val userId = args.getOrNull(2) ?: throw IllegalArgumentException("No user id set!")

    decryptAndExtract(dbFile, password, userId) ?: throw IllegalStateException("It was not possible to decrypt the database!")
}
