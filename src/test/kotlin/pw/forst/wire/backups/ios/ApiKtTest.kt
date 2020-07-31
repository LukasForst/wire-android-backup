package pw.forst.wire.backups.ios

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue


@Disabled
class ApiKtTest {

    private fun runTest(
        db: String, userId: String, password: String,
        name: String = "test",
        outputDirectory: String = "ignored-assets/$name"
    ) {
        val database = processIosBackup(
            encryptedBackupPath = db,
            password = password,
            userIdForBackup = userId,
            outputDirectory = outputDirectory
        )
        println(database)
        println(database.messages.size)
    }

    @Test
    fun `test dejan process backup`() {
        runTest(
            db = "ignored-assets/dejan.ios_wbu",
            userId = "a106fcd5-3146-4551-a870-9b13b125f376",
            password = "Qwerty123!",
            name = "dejan"
        )
    }

    @Test
    fun `test eva process backup`() {
        runTest(
            db = "/Users/lukas/work/wire/android-db-decryption/ignored-assets/ios_backup.ios_wbu",
            userId = "e4d71ce0-eb3a-48f6-b319-d677a2dd23b1",
            password = "Aa12345!",
            name = "eva"
        )
    }

    @Test
    fun `test dejan56 process backup`() {
        runTest(
            db = "/Users/lukas/work/wire/android-db-decryption/ignored-assets/dejan56.ios_wbu",
            userId = "a106fcd5-3146-4551-a870-9b13b125f376",
            password = "Qwerty123!",
            name = "dejan56"
        )
    }

    @Test
    fun `test berlinerzeitung process backup`() {
        // the kotlin version for some reason does not work
        val ex = Assertions.assertThrows(IllegalArgumentException::class.java) {
            runTest(
                db = "/Users/lukas/work/wire/android-db-decryption/ignored-assets/Wire-berlinerzeitung-Backup_20200730.ios_wbu",
                userId = "2bc78bf0-86ee-4701-9713-245216a60df9",
                password = "BackupFile1!",
                name = "berlinerzeitung"
            )
        }
        assertTrue { ex.message!!.startsWith("Unsupported version of export!") }
    }
}
