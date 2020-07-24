package pw.forst.wire.backups.ios

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class ApiKtTest {
    @Test
    @Disabled
    fun `test processIosBackup`() {
        val db = "/Users/lukas/work/wire/android-db-decryption/ignored-assets/dejan.ios_wbu"
        val userId = "a106fcd5-3146-4551-a870-9b13b125f376"
        val password = "Qwerty123!"
        val decrypted = processIosBackup(
            encryptedBackupPath = db,
            password = password,
            userIdForBackup = userId,
            outputDirectory = "/Users/lukas/work/wire/android-db-decryption/ignored-assets/test"
        )
        print(decrypted)
    }
}
