package com.wire.integrations.backups

import com.wire.integrations.backups.steps.decryptAndExtract
import com.wire.integrations.backups.steps.decryptDatabase
import com.wire.integrations.backups.steps.extractBackup
import com.wire.integrations.backups.steps.initSodium
import com.wire.integrations.backups.utils.addLibraryPath
import java.io.File
import java.util.UUID

fun main() {
    addLibraryPath("libs")
    initSodium()

    val db = File("testing-assets/backup.android_wbu")
    val userId = UUID.fromString("199a7b4b-4342-4de6-b4cb-f39852ee445b")
    val password = "a".toByteArray()
    val decrypted = decryptDatabase(db, password, userId) ?: return

    extractBackup(decrypted, userId, ".")
}

fun main3(args: Array<String>) {
    addLibraryPath("libs")
    initSodium()

    val dbFile = args.firstOrNull() ?: throw IllegalArgumentException("No db file set!")
    val password = args.getOrNull(1) ?: throw IllegalArgumentException("No password set!")
    val userId = args.getOrNull(2) ?: throw IllegalArgumentException("No user id set!")

    decryptAndExtract(dbFile, password, userId) ?: throw IllegalStateException("It was not possible to decrypt the database!")
}
