package pw.forst.wire.backups.ios

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@Disabled
class ApiKtTest {

    private fun runTest(db: String, userId: String, password: String) {
        val database = processIosBackup(
            encryptedBackupPath = db,
            password = password,
            userIdForBackup = userId,
            outputDirectory = "/Users/lukas/work/wire/android-db-decryption/ignored-assets/test"
        )
        println(database)
        println(database.messages.size)
    }

    @Test
    fun `test dejan process backup`() {
        runTest(
            db = "/Users/lukas/work/wire/android-db-decryption/ignored-assets/dejan.ios_wbu",
            userId = "a106fcd5-3146-4551-a870-9b13b125f376",
            password = "Qwerty123!"
        )
    }

    @Test
    fun `test eva process backup`() {
        runTest(
            db = "/Users/lukas/work/wire/android-db-decryption/ignored-assets/ios_backup.ios_wbu",
            userId = "e4d71ce0-eb3a-48f6-b319-d677a2dd23b1",
            password = "Aa12345!"
        )
    }
}
