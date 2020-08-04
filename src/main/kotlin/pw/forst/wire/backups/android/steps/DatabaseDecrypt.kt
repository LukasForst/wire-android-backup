package pw.forst.wire.backups.android.steps

import com.goterl.lazycode.lazysodium.SodiumJava
import com.goterl.lazycode.lazysodium.utils.LibraryLoader
import java.io.File
import java.util.UUID

/**
 * Decrypts given database file for given user.
 */
fun decryptDatabase(databaseFile: File, password: ByteArray, userId: UUID): File {
    // read metadata
    val metadata = readEncryptedMetadata(databaseFile)
    // init sodium lib
    val sodium = SodiumJava(LibraryLoader.Mode.PREFER_SYSTEM)

    val hash = sodium.hash(userId.toString().toByteArray(), metadata.salt, metadata.opslimit, metadata.memlimit)
    require(hash.contentEquals(metadata.uuidHash)) { "Uuid hashes don't match" }

    val encryptedBackupBytes = readFileBytes(
        databaseFile,
        totalHeaderLength
    )
    val decrypted = sodium.decrypt(encryptedBackupBytes, password, metadata)

    return File.createTempFile("wire_backup", ".zip")
        .apply {
            deleteOnExit()
            writeBytes(decrypted)
        }
}
