package pw.forst.wire.backups.ios.database

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ConversationsConverterKtTest {
    @Test
    @Disabled
    fun `test get conversations`() {
        val data = obtainIosConversations(
            "/Users/lukas/work/wire/android-db-decryption/ignored-assets/store.wiredatabase",
            UUID.randomUUID()
        )
        assertTrue { data.isNotEmpty() }
        println(data)
        println(data.size)
        assertEquals(140, data.size)
    }
}
