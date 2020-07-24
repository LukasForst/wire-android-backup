package pw.forst.wire.backups.ios

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@Disabled
class ConverterKtTest {
    @Test
    fun `test get some data from db`() {
        val data = obtainIosMessages(
            "/Users/lukas/work/wire/android-db-decryption/ignored-assets/store.wiredatabase"
        )
        assertTrue { data.isNotEmpty() }
        print(data)
    }
}
