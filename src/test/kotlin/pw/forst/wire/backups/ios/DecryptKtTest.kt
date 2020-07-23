package pw.forst.wire.backups.ios

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import pw.forst.wire.backups.android.steps.initSodium
import java.io.File
import java.nio.ByteBuffer
import java.util.Base64
import java.util.UUID

class DecryptKtTest {
    @BeforeEach
    fun init() = initSodium()

    @Test
    fun `test decryption works`() {
        val expectedMessage = "123456789".toByteArray()

        val passphrase = "1235678"
        val uuid = UUID.fromString("71DE4781-9EC7-4ED4-BADE-690C5A9732C6")
        val encrypted = Base64.getDecoder()
            .decode("V0JVSQAAAT5xxW76YX91IgLvJwXeC5x+q/8To15mBzbsA6rc5Dzf7xRyWH+LYv+bscKxj3c7Fl7trr/9qt78lgA5ZtyjK7d2ZBdSYl4HLskPjyUIseTjAZjGKt+7MEXp8aVBey8ooGep")

        val decrypted = decrypt(ByteBuffer.wrap(encrypted), passphrase, uuid)

        assertTrue { expectedMessage.contentEquals(decrypted) }

    }

    @Test
    @Disabled
    fun `test ios decryption dejan`() {
        val db = File("/Users/lukas/work/wire/android-db-decryption/ignored-assets/dejan.ios_wbu")
        val userId = UUID.fromString("a106fcd5-3146-4551-a870-9b13b125f376")
        val password = "Qwerty123!"
        val decrypted = decrypt(db, password, userId)

        print(decrypted)
    }

    @Test
    @Disabled
    fun `test ios decryption eva`() {
        val db = File("/Users/lukas/work/wire/android-db-decryption/ignored-assets/ios_backup.ios_wbu")
        val userId = UUID.fromString("e4d71ce0-eb3a-48f6-b319-d677a2dd23b1")
        val password = "Aa12345!"
        val decrypted = decrypt(db, password, userId)
        print(decrypted)
    }
}
