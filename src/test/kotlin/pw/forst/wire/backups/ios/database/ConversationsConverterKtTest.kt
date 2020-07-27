package pw.forst.wire.backups.ios.database

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.UUID

class ConversationsConverterKtTest {
    @Test
    @Disabled
    fun `test get conversations`() {
        val data = obtainIosConversations(
            "/Users/lukas/work/wire/android-db-decryption/ignored-assets/store.wiredatabase",
            UUID.randomUUID()
        )
        Assertions.assertTrue { data.isNotEmpty() }
        println(data)
        println(data.size)
    }
}
