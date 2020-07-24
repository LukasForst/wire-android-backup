package pw.forst.wire.backups.ios.export

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import pw.forst.wire.backups.utils.initSodium


class DatabaseExportKtTest {
    @BeforeEach
    fun init() = initSodium()

    @Test
    @Disabled
    fun `test database export`() {
        val db = "/Users/lukas/work/wire/android-db-decryption/ignored-assets/dejan.ios_wbu"
        val userId = "a106fcd5-3146-4551-a870-9b13b125f376"
        val password = "Qwerty123!"
        val decrypted = exportIosDatabase(
            db, password, userId,
            outputPath = "/Users/lukas/work/wire/android-db-decryption/ignored-assets/test"
        )
        print(decrypted)
    }
}
