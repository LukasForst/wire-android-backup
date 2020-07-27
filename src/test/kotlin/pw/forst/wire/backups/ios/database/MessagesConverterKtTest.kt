package pw.forst.wire.backups.ios.database

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class MessagesConverterKtTest {
    @Test
    @Disabled
    fun `test get some data from db`() {
        val data = obtainIosMessages(
            "/Users/lukas/work/wire/android-db-decryption/ignored-assets/store.wiredatabase"
        )
        assertTrue { data.isNotEmpty() }
        println(data)
        println(data.size)
    }

}
