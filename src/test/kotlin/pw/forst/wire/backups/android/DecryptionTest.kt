package pw.forst.wire.backups.android

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import pw.forst.wire.backups.android.steps.decryptDatabase
import pw.forst.wire.backups.android.steps.extractBackup
import pw.forst.wire.backups.android.steps.initSodium
import pw.forst.wire.backups.utils.addLibraryPath
import java.io.File
import java.util.UUID
import kotlin.test.assertNotNull

@Disabled
class DecryptionTest {
    @Test
    fun `test decryption`() {
        addLibraryPath("libs")
        initSodium()

        val db = File("ignored-assets/Wire-maciek102-Backup_20200708.android_wbu")
        val userId = UUID.fromString("2f9e89c9-78a7-477d-8def-fbd7ca3846b5")
        val password = "Qwerty1!".toByteArray()
        val decrypted = decryptDatabase(db, password, userId) ?: return

        val (metadata, file) = extractBackup(decrypted, userId, ".")
        assertNotNull(file)
        print(metadata)
    }
}
