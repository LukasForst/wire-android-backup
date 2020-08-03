package pw.forst.wire.backups.ios.database

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Disabled
class ConversationsConverterKtTest {
    @Test
    fun `test get conversations`() {
        val data = withDatabase(
            "/Users/lukas/work/wire/android-db-decryption/ignored-assets/store.wiredatabase"
        ) {
            getConversations(UUID.randomUUID())
        }
        assertTrue { data.isNotEmpty() }
        println(data)
        println(data.size)
        assertEquals(140, data.size)
    }
}
