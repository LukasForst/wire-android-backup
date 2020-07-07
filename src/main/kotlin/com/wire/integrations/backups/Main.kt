package com.wire.integrations.backups

import com.wire.integrations.backups.utils.addLibraryPath
import java.io.File
import java.util.UUID

fun main() {
    addLibraryPath("/Users/lukas/work/wire/android-db-decryption/libs")
    initSodium()

    val db = File("/Users/lukas/work/wire/android-db-decryption/src/main/resources/backup.android_wbu")
    val userId = UUID.fromString("199a7b4b-4342-4de6-b4cb-f39852ee445b")
    val password = "a".toByteArray()
    val decrypted = decryptDatabase(db, password, userId) ?: return

    extractBackup(decrypted, userId, "/Users/lukas/work/wire/android-db-decryption")

    print("YES")
}
//-Djava.library.path=libs
